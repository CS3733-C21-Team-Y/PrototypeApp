package edu.wpi.cs3733.c21.teamY.dataops;

import edu.wpi.cs3733.c21.teamY.entity.Edge;
import edu.wpi.cs3733.c21.teamY.entity.Employee;
import edu.wpi.cs3733.c21.teamY.entity.Node;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import org.apache.derby.iapi.error.StandardException;
import org.apache.derby.iapi.sql.ResultSet;

public class JDBCUtils {
  private static Connection conn;

  static {
    // credentials
    String user = "admin";
    String password = "admin";

    String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    String connectionURL = "jdbc:derby:DB";

    // Checking for Driver
    try {
      Class.forName(driver);
    } catch (ClassNotFoundException e) {
      System.out.println("Where is your Derby JDBC Driver?");

      e.printStackTrace();
    }

    // Attempting Connection
    try {
      conn = DriverManager.getConnection(connectionURL + ";create=true;", user, password);
    } catch (Exception e) {
      // catching failed connection
      System.out.println("Connection Failed! Check output console");

      e.printStackTrace();
    }
    try {
      Statement stmt = conn.createStatement();
      String sqlNode =
          "create table Node(nodeID varchar(20) PRIMARY KEY ,\n"
              + "nodeType varchar(8) not null ,\n"
              + "xcoord varchar(8) not null ,\n"
              + "ycoord varchar(8) not null ,\n"
              + "floor varchar(2) not null ,\n"
              + "building varchar(20) not null ,\n"
              + "longName varchar(100) not null ,\n"
              + "shortName varchar(50) not null ,\n"
              + "teamAssigned char not null )";

      stmt.executeUpdate(sqlNode);

      String sqlEdge =
          "create table Edge(edgeID varchar(40) PRIMARY KEY NOT NULL ,\n"
              + "startNode varchar(30) not null ,\n"
              + "endNode varchar(30) not null )";

      stmt.executeUpdate(sqlEdge);

      String sqlService =
          "create table Service(serviceID int PRIMARY KEY , type varchar(20) not null ,"
              + "description varchar(255) , location varchar(30), category varchar(20), "
              + "urgency varchar(10), date varchar(20),status int,check ( status=-1 OR status =0 OR status=1 ))";
      stmt.executeUpdate(sqlService);

      String sqlEmployee =
          "create table Employee(firstName varchar(30) not null, lastName varchar(30) not null, employeeID varchar(30) PRIMARY KEY not null, "
              + "accessLevel int not null, primaryWorkspace varchar(30))";
      stmt.executeUpdate(sqlEmployee);

    } catch (SQLException ignored) {
      // ignored.printStackTrace();
    }
  }

  /*
  get connection
   */
  public static Connection getConn() throws SQLException {
    return DriverManager.getConnection("jdbc:derby:DB;");
  }
  /*
  release resources and close connection in case there is no result set
   */

  // parameter as null if there is no such thing to close

  /**
   * Closes the given PreparedStatement, ResultSet, Statement, from a given connection
   *
   * @param ps the prepared statement to close
   * @param rs the result set to close
   * @param stmt the statement to close
   * @param conn the database connection to close
   */
  public static void close(PreparedStatement ps, ResultSet rs, Statement stmt, Connection conn) {
    if (ps != null) {

      try {
        ps.close();
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }
    }
    if (rs != null) {
      try {
        rs.close();
      } catch (StandardException e) {
        e.printStackTrace();
      }
    }

    if (stmt != null) {
      try {
        stmt.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Creates a prepared statement for either an "insert into" or "update" query type of Node or Edge
   *
   * @param numArguments number of arguments for the given entity in the table
   * @param object either an object of type Node or Edge to be inserted into the table
   * @param tableName the name of the table to insert into ("Node" or "Edge")
   * @throws SQLException handling exception regarding sql syntax, duplicate keys, etc.
   * @throws IllegalAccessException if access is blocked when retrieving information
   */
  public static PreparedStatement createPreparedStatementInsert(
      int numArguments, Object object, String tableName)
      throws SQLException, IllegalAccessException {
    // Building the portion of the query statement that handles the (?), (?), ... for the values
    // being inserted
    StringBuilder arguments = new StringBuilder();

    // appending ' (?), ' onto the string builder accounting for all but the last
    for (int i = 0; i < numArguments - 1; i++) {
      arguments.append("(?), ");
    }
    // adding the last without the comma
    arguments.append("(?)");

    // creates the prepared statement inserting with tableName and the arguments stringbuilder
    PreparedStatement psInsert =
        JDBCUtils.conn.prepareStatement(
            "insert into ADMIN." + tableName + " values(" + arguments.toString() + ")");
    Field[] fields = object.getClass().getDeclaredFields();
    int parameterCounter = 0;

    // This portion of the code will fill the (?) with the field values retrieved from the object
    for (Field field : fields) {
      String fieldName = field.getName();
      parameterCounter++;
      // for some reason the first "field" of node is neighbors lmao
      if (fieldName.equals("neighbors")
          || fieldName.equals("$jacocoData")
          || fieldName.equals("room")) {
        parameterCounter--;
        continue;
      }

      field.setAccessible(true);

      String param = String.valueOf(field.get(object));
      // System.out.println(param + " counter value " + parameterCounter);
      psInsert.setString(parameterCounter, param);
    }

    return psInsert;
  }

  /**
   * Inserts into specified table the inputted object
   *
   * @param numArgs numArguments number of arguments for the given entity in the table
   * @param object object either an object of type Node or Edge to be inserted into the table
   * @param tableName tableName the name of the table to insert into ("Node" or "Edge")
   * @throws SQLException if there is a duplicate key and updates instead
   * @throws IllegalAccessException if access is blocked when retrieving information
   */
  public static void insert(int numArgs, Object object, String tableName)
      throws SQLException, IllegalAccessException {
    PreparedStatement statement = createPreparedStatementInsert(numArgs, object, tableName);
    try {
      statement.execute();
      statement.closeOnCompletion();

    } catch (SQLException e) {

      // updates given object value in table if the PK already exists w/in it
      if (e.getErrorCode() == 30000) {
        // e.printStackTrace();
        if (object instanceof Node) {
          JDBCUtils.update((Node) object);
        } else if (object instanceof Edge) {
          JDBCUtils.update((Edge) object);
        }
      }
    }
    close(statement, null, null, null);
  }

  /**
   * Takes in an ArrayList<Node> and inputs them (in total) into the "Node" table
   *
   * @param nodes represents the nodes to be inserted
   * @throws SQLException if there is a duplicate key in the table or other syntax SQL exceptions
   * @throws IllegalAccessException if access is blocked when retrieving information
   */
  public static void insertArrayListNode(ArrayList<Node> nodes)
      throws SQLException, IllegalAccessException {

    for (Node node : nodes) {
      JDBCUtils.insert(9, node, "Node");
    }
  }

  // INTRODUCE BATCHING FOR PERFORMANCE
  /**
   * Takes in an ArrayList<Edge> and one by one inserts them into the Edge Table
   *
   * @param edges represents the edges that will be inserted into the "Edge" table
   * @throws SQLException if there is a duplicate key in the table or other syntax SQL exceptions
   * @throws IllegalAccessException if access is blocked when retrieving information
   */
  public static void insertArrayListEdge(ArrayList<Edge> edges)
      throws SQLException, IllegalAccessException {

    for (Edge edge : edges) {
      JDBCUtils.insert(3, edge, "Edge");
    }
  }

  /**
   * <<<<<<< HEAD Fills both the Node and Edge table with the data in the CSV files that store Node
   * and Edge data respectively
   *
   * @throws IllegalAccessException if access is blocked when retrieving information
   * @throws NoSuchFieldException if the field of an object cannot be found
   * @throws SQLException if there is a duplicate key in the table or other syntax SQL exceptions
   */
  public static void fillTablesFromCSV() throws IllegalAccessException, IOException, SQLException {
    ArrayList<Node> nodes = CSV.getNodesCSV();
    ArrayList<Edge> edges = CSV.getEdgesCSV();
    insertArrayListNode(nodes);
    insertArrayListEdge(edges);
  }

  /**
   * Creates the prepared statement that will be executed by the update method for an update on the
   * Node table
   *
   * @param node represents the node in which to update the table with
   * @throws SQLException if there is a duplicate key in the table or other syntax SQL exceptions
   * @return a PreparedStatement to be used by the update method
   */
  public static PreparedStatement createPreparedStatementUpdate(Node node) throws SQLException {
    Connection connection = getConn();
    return connection.prepareStatement(
        "update Admin.NODE set "
            + "NODETYPE = '"
            + node.nodeType
            + "', XCOORD = '"
            + node.xcoord
            + "', YCOORD = '"
            + node.ycoord
            + "', FLOOR = '"
            + node.floor
            + "', BUILDING = '"
            + node.building
            + "', LONGNAME = '"
            + node.longName
            + "', SHORTNAME = '"
            + node.shortName
            + "', TEAMASSIGNED = '"
            + node.teamAssigned
            + "' where NODEID = '"
            + node.nodeID
            + "'");
  }

  /**
   * Creates the prepared statement that will be executed by the update method for an update on the
   * Edge table
   *
   * @param edge represents the edge to update
   * @throws SQLException if there is a duplicate key in the table or other syntax SQL exceptions
   * @return a PreparedStatement to be used by the update method
   */
  public static PreparedStatement createPreparedStatementUpdate(Edge edge) throws SQLException {
    Connection connection = getConn();
    return connection.prepareStatement(
        "update ADMIN.EDGE set "
            // + "EDGEID = '"
            // + edge.edgeID
            + "STARTNODE = '"
            + edge.startNodeID
            + "', ENDNODE = '"
            + edge.endNodeID
            + "' where edgeID = '"
            + edge.edgeID
            + "'");
  }

  /**
   * Updates the specified Node with its matching nodeID in the Node table
   *
   * @param node represents the node to be updated within the DB table
   * @throws SQLException if there is a duplicate key in the table or other syntax SQL exceptions
   */
  public static void update(Node node) throws SQLException {
    try {
      PreparedStatement statement = createPreparedStatementUpdate(node);
      statement.executeUpdate();
      getConn().commit();
      statement.closeOnCompletion();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Updates the specified Edge with its matching edgeID in the Edge table
   *
   * @param edge represents the edge to be updated in the DB table "Edge"
   * @throws SQLException if there is a duplicate key in the table or other syntax SQL exceptions
   */
  public static void update(Edge edge) throws SQLException {
    PreparedStatement statement = createPreparedStatementUpdate(edge);
    statement.executeUpdate();
    getConn().commit();
    statement.closeOnCompletion();
  }

  /**
   * Functionality to create a resultSet of a select statement from a given table
   *
   * @param tableName represents the table in which to update into
   * @throws SQLException if there is a duplicate key in the table or other syntax SQL exceptions
   */
  public static void selectQuery(String tableName) throws SQLException {

    String sql = "SELECT * FROM ADMIN." + tableName;
    Statement stmt;
    stmt = conn.createStatement();
    stmt.executeQuery(sql);
    stmt.close();
  }

  /**
   * Creates the prepared statement that will be executed by the delete method for an delete on the
   * Edge table
   *
   * @param nodeID the ID of the node to be deleted from the table
   * @return a PreparedStatement to be used by the delete method
   * @throws SQLException if there is a duplicate key in the table or other syntax SQL exceptions
   */
  public static PreparedStatement createPreparedStatementDeleteNode(String nodeID)
      throws SQLException {
    Connection connection = getConn();
    return connection.prepareStatement("DELETE FROM ADMIN.NODE WHERE NODEID = '" + nodeID + "'");
  }

  /**
   * Creates the prepared statement that will be executed by the delete method for a delete on the
   * Edge table
   *
   * @param edgeID the ID of the edge to be deleted from the table
   * @return a PreparedStatement to be used in the delete method
   * @throws SQLException if there is a duplicate key in the table or other syntax SQL exceptions
   */
  public static PreparedStatement createPreparedStatementDeleteEdge(String edgeID)
      throws SQLException {
    Connection connection = getConn();
    return connection.prepareStatement("DELETE FROM ADMIN.EDGE WHERE EDGEID = '" + edgeID + "'");
  }

  /**
   * Deletes the inputted Node's matching entry in the Node table
   *
   * @param nodeID@throws SQLException
   */
  public static void deleteNode(String nodeID) throws SQLException {
    PreparedStatement statement = createPreparedStatementDeleteNode(nodeID);
    statement.execute();
    statement.closeOnCompletion();
  }

  /**
   * Deletes the inputted Edge's matching entry in the Edge table
   *
   * @param edgeID@throws SQLException
   */
  public static void deleteEdge(String edgeID) throws SQLException {
    PreparedStatement statement = createPreparedStatementDeleteEdge(edgeID);
    statement.execute();
    statement.closeOnCompletion();
  }

  public static PreparedStatement createPreparedStatementUpdateCoordsOnly(
      String nodeID, double xcoord, double ycoord) throws SQLException {
    Connection connection = getConn();
    return connection.prepareStatement(
        "update Admin.NODE set "
            + " XCOORD = '"
            + xcoord
            + "', YCOORD = '"
            + ycoord
            + "' where NODEID = '"
            + nodeID
            + "'");
  }

  public static void updateNodeCoordsOnly(String nodeID, double xcoord, double ycoord) {
    try {
      PreparedStatement statement = createPreparedStatementUpdateCoordsOnly(nodeID, xcoord, ycoord);
      statement.executeUpdate();
      getConn().commit();
      statement.closeOnCompletion();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * @param service a service to be saved to DB
   * @throws SQLException if there is a duplicate key in the table or other syntax SQL exceptions
   * @throws IllegalAccessException if access is denied
   */
  public static void saveService(Service service) throws SQLException, IllegalAccessException {
    insert(8, service, "Service"); // save to database
    // Used to save to CSV as well but marked deprecated - look into
  }

  /**
   * @param serviceType type of service to be exported. Leave as empty string if ny preference type
   * @return a list of services
   * @throws SQLException if there is a duplicate key in the table or other syntax SQL exceptions
   */
  public static ArrayList<Service> exportService(String serviceType) throws SQLException {
    ArrayList<Service> services = new ArrayList<>();
    String string;
    if (serviceType.equals("")) {
      string = "select * from ADMIN.Service";
    } else {
      string = "select * from ADMIN.Service where type=" + serviceType;
    }
    Connection conn = getConn();
    Statement statement = conn.createStatement();
    java.sql.ResultSet resultSet = statement.executeQuery(string);
    int serviceID;
    String type;
    String description;
    String location;
    String category;
    String urgency;
    String date;
    int status;
    while (resultSet.next()) {
      serviceID = resultSet.getInt(1);
      type = resultSet.getString(2);
      description = resultSet.getString(3);
      location = resultSet.getString(4);
      category = resultSet.getString(5);
      urgency = resultSet.getString(6);
      date = resultSet.getString(7);
      status = resultSet.getInt(8);
      Service service =
          new Service(serviceID, type, description, location, category, urgency, date, status);
      services.add(service);
    }
    resultSet.close();
    close(null, null, statement, conn);
    return services;
  }

  /**
   * remove a service from database
   *
   * @param ID service ID of the service to be removed
   * @throws SQLException if there is a duplicate key in the table or other syntax SQL exceptions
   */
  public static void removeService(int ID) throws SQLException {
    String string = "delete from ADMIN.Service where ADMIN.Service.serviceID=" + ID;
    Connection conn = getConn();
    Statement statement = conn.createStatement();
    int numRows = statement.executeUpdate(string);
    close(null, null, statement, conn);
    if (numRows == 0) {
      System.out.println("no rows have been deleted");
    } else {
      System.out.println("delete successful");
    }
  }

  /**
   * update a service's status code
   *
   * @param service a service to be updated
   * @param status the new status
   * @throws SQLException if there is a duplicate key in the table or other syntax SQL exceptions
   */
  public static void updateServiceStatus(Service service, int status) throws SQLException {
    String string =
        "update ADMIN.SERVICE set status= "
            + status
            + " where ADMIN.SERVICE.SERVICEID="
            + service.getServiceID();
    Connection conn = getConn();
    Statement statement = conn.createStatement();
    int rowsAffected = statement.executeUpdate(string);
    close(null, null, statement, conn);
    if (rowsAffected == 0) {
      System.out.println("update failed");
    } else {
      System.out.println("update successful");
    }
  }

  /**
   * @param service a service to be inserted into DB
   * @param preparedStatement prepare statement
   */
  public static void createPreparedStatementInsert(
      Service service, PreparedStatement preparedStatement) {

    try {

      preparedStatement.setInt(1, service.getServiceID());
      preparedStatement.setString(2, service.getType());
      preparedStatement.setString(3, service.getDescription());
      preparedStatement.setString(4, service.getLocation());
      preparedStatement.setString(5, service.getCategory());
      preparedStatement.setString(6, service.getUrgency());
      preparedStatement.setString(7, service.getDate());
      preparedStatement.setInt(8, service.getStatus());
      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      System.out.print("It seems there is an error in the SQL syntax");
    }
  }

  public static PreparedStatement createPreparedStatementInsert(Employee employee)
      throws SQLException {
    Connection connection = JDBCUtils.getConn();
    PreparedStatement stmt =
        connection.prepareStatement("insert into ADMIN.EMPLOYEE values ((?),(?),(?),(?),(?))");
    stmt.setString(1, employee.getFirstName());
    stmt.setString(2, employee.getLastName());
    stmt.setString(3, employee.getEmployeeID());
    stmt.setInt(4, employee.getAccessLevel());
    stmt.setString(5, employee.getPrimaryWorkspace());
    return stmt;
  }

  public static void insert(Employee employee) throws SQLException {
    PreparedStatement stmt = createPreparedStatementInsert(employee);

    try {
      stmt.execute();
      stmt.closeOnCompletion();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static PreparedStatement createPreparedStatementUpdate(Employee employee)
      throws SQLException {
    Connection connection = JDBCUtils.getConn();
    return connection.prepareStatement(
        "UPDATE ADMIN.EMPLOYEE set "
            + "ADMIN.EMPLOYEE.FIRSTNAME = '"
            + employee.getFirstName()
            + "', "
            + "ADMIN.EMPLOYEE.LASTNAME = '"
            + employee.getLastName()
            + "', "
            + "ADMIN.EMPLOYEE.EMPLOYEEID = '"
            + employee.getEmployeeID()
            + "', "
            + "ADMIN.EMPLOYEE.ACCESSLEVEL = "
            + employee.getAccessLevel()
            + ", "
            + "ADMIN.EMPLOYEE.PRIMARYWORKSPACE = '"
            + employee.getPrimaryWorkspace()
            + "' "
            + "WHERE ADMIN.EMPLOYEE.EMPLOYEEID = '"
            + employee.getEmployeeID()
            + "'");
  }

  public static void update(Employee employee) throws SQLException {
    PreparedStatement stmt = createPreparedStatementUpdate(employee);
    try {
      stmt.executeUpdate();
      stmt.closeOnCompletion();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static PreparedStatement createPreparedStatementDeleteEmployee(String employeeID)
      throws SQLException {
    Connection connection = getConn();
    return connection.prepareStatement(
        "DELETE FROM ADMIN.EMPLOYEE WHERE EMPLOYEEID = '" + employeeID + "'");
  }

  public static void deleteEmployee(Employee employee) throws SQLException {
    PreparedStatement stmt = createPreparedStatementDeleteEmployee(employee.getEmployeeID());
    stmt.executeUpdate();
    stmt.closeOnCompletion();
  }

  public static void deleteEmployee(String employeeID) throws SQLException {
    PreparedStatement stmt = createPreparedStatementDeleteEmployee(employeeID);
    stmt.executeUpdate();
    stmt.closeOnCompletion();
  }
}
