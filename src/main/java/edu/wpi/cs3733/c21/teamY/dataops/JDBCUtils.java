package edu.wpi.cs3733.c21.teamY.dataops;

import edu.wpi.cs3733.c21.teamY.entity.*;
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;

public class JDBCUtils {
  private static Connection conn;

  /**
   * Initializes the database by creating the tables and filling them from the CSV's if the DB
   * tables don't already exist
   */
  public static void initDB() {
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
              + "endNode varchar(30) not null)";

      stmt.executeUpdate(sqlEdge);

      String sqlService =
          "create table Service(serviceID int PRIMARY KEY , type varchar(20),"
              + "description varchar(255) , location varchar(30), category varchar(20), "
              + "urgency varchar(10), date varchar(20), additionalInfo varchar(255), requester varchar(30) not null, status int,"
              + " employee varchar(30) DEFAULT 'admin',"
              + "constraint FK_Requester_ID FOREIGN KEY (requester) REFERENCES ADMIN.EMPLOYEE (EMPLOYEEID) ON DELETE CASCADE,"
              + "constraint FK_Employee FOREIGN KEY (employee) REFERENCES ADMIN.EMPLOYEE (EMPLOYEEID) ON DELETE CASCADE,"
              + " check( status=-1 OR status =0 OR status=1))";

      String sqlEmployee =
          "create table Employee(firstName varchar(30) not null, lastName varchar(30) not null, employeeID varchar(30) PRIMARY KEY not null, "
              + "password varchar(40), email varchar(50), accessLevel int not null, primaryWorkspace varchar(30))";

      String sqlParkingLot =
          "create table ParkingLot(nodeID varchar(20) , userName varchar(30) PRIMARY KEY, "
              + "constraint FK_UserName FOREIGN KEY(userName) REFERENCES ADMIN.EMPLOYEE(EMPLOYEEID) ON DELETE CASCADE,"
              + "constraint FK_NodeID FOREIGN KEY(nodeID) REFERENCES ADMIN.Node(NODEID) ON DELETE CASCADE)";
      stmt.executeUpdate(sqlEmployee);
      stmt.executeUpdate(sqlService);
      stmt.executeUpdate(sqlParkingLot);

      JDBCUtils.fillTablesFromCSV();
    } catch (SQLException ignored) {
      // ignored.printStackTrace();
    } catch (IllegalAccessException | IOException e) {
      e.printStackTrace();
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
   * @param closeables: object to close
   */
  public static void close(Closeable... closeables) throws IOException {
    try {
      for (Closeable c : closeables) {
        c.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
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

    // creates the prepared statement inserting with tableName and the arguments stringBuilder
    PreparedStatement psInsert =
        JDBCUtils.getConn()
            .prepareStatement(
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
   */
  public static void insert(int numArgs, Object object, String tableName) {
    PreparedStatement statement;
    try {
      statement = createPreparedStatementInsert(numArgs, object, tableName);
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
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    try {
      close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Takes in an ArrayList<Node> and inputs them (in total) into the "Node" table
   *
   * @param nodes represents the nodes to be inserted
   */
  public static void insertArrayListNode(ArrayList<Node> nodes) {

    for (Node node : nodes) {
      JDBCUtils.insert(9, node, "Node");
    }
  }

  // INTRODUCE BATCHING FOR PERFORMANCE

  /**
   * Takes in an ArrayList<Edge> and one by one inserts them into the Edge Table
   *
   * @param edges represents the edges that will be inserted into the "Edge" table
   */
  public static void insertArrayListEdge(ArrayList<Edge> edges) {

    for (Edge edge : edges) {
      JDBCUtils.insert(3, edge, "Edge");
    }
  }

  /**
   * <<<<<<< HEAD Fills both the Node and Edge table with the data in the CSV files that store Node
   * and Edge data respectively
   *
   * @throws IllegalAccessException if access is blocked when retrieving information
   * @throws SQLException if there is a duplicate key in the table or other syntax SQL exceptions
   */
  public static void fillTablesFromCSV() throws IllegalAccessException, IOException, SQLException {
    CSV.getNodesCSV();
    CSV.getEdgesCSV();
    CSV.getEmployeesCSV();
    CSV.getServiceCSV();
  }

  /**
   * Updates the specified Node with its matching nodeID in the Node table
   *
   * @param node represents the node to be updated within the DB table
   */
  public static void update(Node node) {
    try {
      Connection connection = getConn();
      PreparedStatement stmt =
          connection.prepareStatement(
              "update Admin.NODE set "
                  + "NODETYPE = (?), "
                  + "XCOORD = (?), "
                  + "YCOORD = (?), "
                  + "FLOOR = (?), "
                  + "BUILDING = (?), "
                  + "LONGNAME = (?), "
                  + "SHORTNAME = (?), "
                  + "TEAMASSIGNED = (?) "
                  + "where NODEID = (?)");
      stmt.setString(1, node.nodeType);
      stmt.setString(2, String.valueOf(node.xcoord));
      stmt.setString(3, String.valueOf(node.ycoord));
      stmt.setString(4, node.floor);
      stmt.setString(5, node.building);
      stmt.setString(6, node.longName);
      stmt.setString(7, node.shortName);
      stmt.setString(8, String.valueOf(node.teamAssigned));
      stmt.setString(9, node.nodeID);
      stmt.executeUpdate();
      stmt.closeOnCompletion();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Updates the specified Edge with its matching edgeID in the Edge table
   *
   * @param edge represents the edge to be updated in the DB table "Edge"
   */
  public static void update(Edge edge) {
    try {
      Connection connection = getConn();
      PreparedStatement stmt =
          connection.prepareStatement(
              "update Admin.EDGE set "
                  + "EDGEID = (?), "
                  + "STARTNODE = (?), "
                  + "ENDNODE = (?) "
                  + "where EDGEID = (?)");
      stmt.setString(1, edge.edgeID);
      stmt.setString(2, String.valueOf(edge.startNodeID));
      stmt.setString(3, String.valueOf(edge.endNodeID));
      stmt.setString(4, edge.edgeID);
      stmt.executeUpdate();
      stmt.closeOnCompletion();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Deletes the inputted Node's matching entry in the Node table
   *
   * @param nodeID is the ID of the node to be deleted from the Node table
   */
  public static void deleteNode(String nodeID) {
    try {
      Connection connection = getConn();
      PreparedStatement stmt =
          connection.prepareStatement("DELETE FROM ADMIN.NODE WHERE NODEID = (?)");
      stmt.setString(1, nodeID);
      stmt.executeUpdate();
      stmt.closeOnCompletion();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Deletes the inputted Edge's matching entry in the Edge table
   *
   * @param edgeID@throws SQLException
   */
  public static void deleteEdge(String edgeID) throws SQLException {
    Connection connection = getConn();
    PreparedStatement stmt =
        connection.prepareStatement("DELETE FROM ADMIN.EDGE WHERE EDGEID = (?)");
    stmt.setString(1, edgeID);
    stmt.executeUpdate();
    stmt.closeOnCompletion();
  }

  /**
   * Updates only a node's coordinates that has the given nodeID
   *
   * @param nodeID the ID of the node to update
   * @param xcoord new xcoord
   * @param ycoord new ycoord
   */
  public static void updateNodeCoordsOnly(String nodeID, double xcoord, double ycoord) {

    try {
      PreparedStatement stmt =
          getConn()
              .prepareStatement(
                  "update Admin.NODE set "
                      + "XCOORD = (?), "
                      + "YCOORD = (?)"
                      + "where NODEID = (?)");
      stmt.setString(1, String.valueOf(xcoord));
      stmt.setString(2, String.valueOf(ycoord));
      stmt.setString(3, nodeID);
      stmt.executeUpdate();
      stmt.closeOnCompletion();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * inserts the given service into the Service table in the Database
   *
   * @param service a service to be saved to DB
   */
  public static void insert(Service service) {
    try {
      Connection connection = getConn();
      PreparedStatement stmt =
          connection.prepareStatement(
              "INSERT INTO ADMIN.SERVICE VALUES ((?),(?),(?),(?),(?),(?),(?),(?),(?),(?),(?))");
      stmt.setString(1, String.valueOf(service.getServiceID()));
      stmt.setString(2, service.getType());
      stmt.setString(3, service.getDescription());
      stmt.setString(4, service.getLocation());
      stmt.setString(5, service.getCategory());
      stmt.setString(6, service.getUrgency());
      stmt.setString(7, service.getDate());
      stmt.setString(8, service.getAdditionalInfo());
      stmt.setString(9, service.getRequester());
      stmt.setString(10, String.valueOf(service.getStatus()));
      stmt.setString(11, service.getEmployee());
      stmt.executeUpdate();
      stmt.closeOnCompletion();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * updates only the AdditionalInfo of the node with the given ID
   *
   * @param serviceID is the ID of the service to update
   * @param newInfo the new value of the AdditionalInfo of the service to be updated
   */
  public static void updateServiceAdditionalInfoOnly(int serviceID, String newInfo) {
    try {

      PreparedStatement stmt =
          getConn()
              .prepareStatement(
                  "update Admin.Service set "
                      + " ADDITIONALINFO = (?) "
                      + " where SERVICEid = (?)");
      stmt.setString(1, newInfo);
      stmt.setString(2, String.valueOf(serviceID));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * @param serviceType type of service to be exported. Leave as empty string if ny preference type
   * @return a list of services
   * @throws SQLException if there is a duplicate key in the table or other syntax SQL exceptions
   */
  public static ArrayList<Service> exportService(String serviceType, String Requester)
      throws SQLException {
    ArrayList<Service> services = new ArrayList<>();
    String string;
    if (serviceType.equals("") && Requester.equals("")) {
      string = "select * from ADMIN.Service";
    } else if (!serviceType.equals("") && !Requester.equals("")) {
      string =
          "select * from ADMIN.Service where type ='"
              + serviceType
              + "'"
              + "AND requester="
              + Requester;
    } else if (!serviceType.equals("")) {
      string = "select * from ADMIN.Service where type =" + serviceType;
    } else {
      string = "select * from ADMIN.Service where requester =" + Requester;
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
    String requester;
    int status;
    String employee;
    String additionalInfo;
    while (resultSet.next()) {
      serviceID = resultSet.getInt(1);
      type = resultSet.getString(2);
      description = resultSet.getString(3);
      location = resultSet.getString(4);
      category = resultSet.getString(5);
      urgency = resultSet.getString(6);
      date = resultSet.getString(7);
      additionalInfo = resultSet.getString(8);
      requester = resultSet.getString(9);
      status = resultSet.getInt(10);
      employee = resultSet.getString(11);
      Service service =
          new Service(
              serviceID,
              type,
              description,
              location,
              category,
              urgency,
              date,
              additionalInfo,
              requester,
              status,
              employee);
      services.add(service);
    }
    resultSet.close();
    try {
      close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return services;
  }

  /**
   * remove a service from database
   *
   * @param ID service ID of the service to be removed
   */
  public static void delete(int ID) {
    int numRows = 0;
    try {
      PreparedStatement stmt =
          getConn()
              .prepareStatement("delete from ADMIN.Service where ADMIN.Service.serviceID= (?)");
      stmt.setString(1, String.valueOf(ID));
      numRows = stmt.executeUpdate();
      stmt.closeOnCompletion();
    } catch (SQLException e) {
      e.printStackTrace();
    }

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
   */
  public static void updateServiceStatus(Service service, int status) {
    int rowsAffected = 0;
    try {
      PreparedStatement stmt =
          getConn()
              .prepareStatement(
                  "update ADMIN.SERVICE set status= (?) " + " where ADMIN.SERVICE.SERVICEID= (?)");
      stmt.setString(1, String.valueOf(status));
      stmt.setString(2, String.valueOf(service.getServiceID()));
      rowsAffected = stmt.executeUpdate();
      stmt.closeOnCompletion();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    if (rowsAffected == 0) {
      System.out.println("update failed");
    } else {
      System.out.println("update successful");
    }
  }

  /**
   * update a service's assigned employee
   *
   * @param service a service to be updated
   * @param employee the new employee assigned
   */
  public static void updateServiceAssignedEmployee(Service service, String employee) {
    int rowsAffected = 0;
    try {
      PreparedStatement stmt =
          getConn()
              .prepareStatement(
                  "update ADMIN.SERVICE set employee = (?)"
                      + "where ADMIN.SERVICE.SERVICEID = (?)");
      stmt.setString(1, employee);
      stmt.setString(2, String.valueOf(service.getServiceID()));
      rowsAffected = stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }

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
      preparedStatement.setString(8, service.getAdditionalInfo());
      preparedStatement.setString(9, service.getRequester());
      preparedStatement.setInt(10, service.getStatus());
      preparedStatement.setString(11, service.getEmployee());

      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      System.out.print("It seems there is an error in the SQL syntax");
    }
  }

  /**
   * inserts the given employee into the employee table
   *
   * @param employee to be inserted
   */
  public static void insert(Employee employee) {
    try {
      Connection connection = JDBCUtils.getConn();
      PreparedStatement stmt =
          connection.prepareStatement(
              "insert into ADMIN.EMPLOYEE values ((?),(?),(?),(?),(?),(?),(?))");
      stmt.setString(1, employee.getFirstName());
      stmt.setString(2, employee.getLastName());
      stmt.setString(3, employee.getEmployeeID());
      stmt.setString(4, employee.getPassword());
      stmt.setString(5, employee.getEmail());
      stmt.setInt(6, employee.getAccessLevel());
      stmt.setString(7, employee.getPrimaryWorkspace());
      stmt.executeUpdate();
      stmt.closeOnCompletion();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * updates the table with the information from the given employee
   *
   * @param employee the new information to update the employee with the same ID with
   */
  public static void update(Employee employee) {
    try {
      PreparedStatement stmt =
          getConn()
              .prepareStatement(
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
                      + "ADMIN.EMPLOYEE.PASSWORD = '"
                      + employee.getPassword()
                      + "', "
                      + "ADMIN.EMPLOYEE.EMAIL = '"
                      + employee.getEmail()
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
      stmt.executeUpdate();
      stmt.closeOnCompletion();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Removes the given employee from the Employee table
   *
   * @param employee to be deleted from table
   */
  public static void delete(Employee employee) {
    try {
      PreparedStatement stmt =
          getConn().prepareStatement("DELETE FROM ADMIN.EMPLOYEE WHERE EMPLOYEEID = (?)");
      stmt.setString(1, employee.getEmployeeID());
      stmt.executeUpdate();
      stmt.closeOnCompletion();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  /**
   * Removes the given employee from the Employee table
   *
   * @param employeeID ID of employee to be deleted from table
   */
  public static void deleteEmployee(String employeeID) {
    try {
      PreparedStatement stmt =
          getConn().prepareStatement("DELETE FROM ADMIN.EMPLOYEE WHERE EMPLOYEEID = (?)");
      stmt.setString(1, employeeID);
      stmt.executeUpdate();
      stmt.closeOnCompletion();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Exports the employees contained in the Employee table as an Arraylist<Employee></Employee>
   *
   * @return ArrayList<Employee> containing the employees from the Employee table </Employee>
   * @throws SQLException upon SQL error communicating with the DB
   */
  public static ArrayList<Employee> exportListOfEmployee() throws SQLException {
    ArrayList<Employee> employees = new ArrayList<>();
    String s = "select * from ADMIN.EMPLOYEE";
    Statement statement = getConn().createStatement();
    java.sql.ResultSet resultSet = statement.executeQuery(s);
    String firstName;
    String lastName;
    String employeeID;
    String password;
    String email;
    int accessLevel;
    String primaryWorkspace;
    while (resultSet.next()) {
      firstName = resultSet.getString(1);
      lastName = resultSet.getString(2);
      employeeID = resultSet.getString(3);
      password = resultSet.getString(4);
      email = resultSet.getString(5);
      accessLevel = resultSet.getInt(6);
      primaryWorkspace = resultSet.getString(7);
      Employee employee =
          new Employee(
              firstName, lastName, employeeID, password, email, accessLevel, primaryWorkspace);
      employees.add(employee);
    }
    resultSet.close();

    try {
      close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return employees;
  }

  /**
   * Determines if there is a user in the table with a given username and password
   *
   * @param username the username of the desired user
   * @param password the password of the desired user
   * @return boolean indicating whether the user was found or not
   * @throws SQLException upon SQL error communicating with the DB
   */
  public static boolean findUser(String username, String password) throws SQLException {
    PreparedStatement query =
        getConn()
            .prepareStatement(
                "Select ADMIN.EMPLOYEE.ACCESSLEVEL, "
                    + "ADMIN.EMPLOYEE.EMPLOYEEID, ADMIN.EMPLOYEE.PASSWORD "
                    + "FROM ADMIN.EMPLOYEE "
                    + "WHERE EMPLOYEEID = (?) "
                    + "AND PASSWORD = (?)");

    query.setString(1, username);
    query.setString(2, password);

    java.sql.ResultSet resultSet = query.executeQuery();

    if (!resultSet.next()) {
      resultSet.close();
      return false;
    } else {
      Settings settings = Settings.getSettings();
      // resultSet.next();
      settings.loginSuccess(username, resultSet.getInt(1));
      resultSet.close();
      return true;
    }
  }

  /**
   * determines if there is a User in the Employee table with a given email
   *
   * @param email to be searched for
   * @return the employeeID of the found user or "false" if none were found
   */
  public static String findUserByEmail(String email) {
    try {
      PreparedStatement query =
          getConn()
              .prepareStatement(
                  "Select ADMIN.EMPLOYEE.EMPLOYEEID "
                      + "FROM ADMIN.EMPLOYEE "
                      + "WHERE ADMIN.EMPLOYEE.EMAIL = (?)");
      query.setString(1, email);

      java.sql.ResultSet resultSet = query.executeQuery();
      if (resultSet.next()) {
        return resultSet.getString(1);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return "false";
  }

  /**
   * @param employee an employee object
   * @return true if created successful
   * @throws SQLException sql exception
   */
  public static boolean createUserAccount(Employee employee) throws SQLException {
    String createAccount = "insert into ADMIN.EMPLOYEE values(?,?,?,?,?,?,?)";
    PreparedStatement preparedStatement = getConn().prepareStatement(createAccount);
    preparedStatement.setString(1, employee.getFirstName());
    preparedStatement.setString(2, employee.getLastName());
    preparedStatement.setString(3, employee.getEmployeeID());
    preparedStatement.setString(4, employee.getPassword());
    preparedStatement.setString(5, employee.getEmail());
    preparedStatement.setInt(6, employee.getAccessLevel());
    preparedStatement.setString(7, employee.getPrimaryWorkspace());
    int check = preparedStatement.executeUpdate();
    return check != 0;
  }

  /**
   * updated the password of the User with the specified userID
   *
   * @param userID of the user who's password is to be changed
   * @param newPassWord to change password to
   * @return boolean for whether reset was successful
   * @throws SQLException upon sql error communicating with the database
   */
  public static boolean updateUserPassword(String userID, String newPassWord) throws SQLException {
    String update = "update ADMIN.EMPLOYEE set PASSWORD= (?) WHERE EMPLOYEEID=(?)";
    PreparedStatement preparedStatement = getConn().prepareStatement(update);
    preparedStatement.setString(1, newPassWord);
    preparedStatement.setString(2, userID);
    int check = preparedStatement.executeUpdate();
    preparedStatement.close();
    return check != 0;
  }

  public static boolean removeAccount(String employeeID) throws SQLException {
    String remove = "delete from ADMIN.EMPLOYEE where EMPLOYID= (?)";
    PreparedStatement preparedStatement = getConn().prepareStatement(remove);
    preparedStatement.setString(1, employeeID);
    int check = preparedStatement.executeUpdate();
    preparedStatement.close();
    return check != 0;
  }

  /**
   * @param employeeID employee to be assigned's ID
   * @param serviceID corresponding service ID
   * @return true if update successful
   * @throws SQLException something went wrong with DB
   */
  public static boolean assignEmployeeToRequest(String employeeID, int serviceID)
      throws SQLException {
    String assign = "update ADMIN.SERVICE set EMPLOYEE=(?) where SERVICEID=(?)";
    PreparedStatement preparedStatement = getConn().prepareStatement(assign);
    preparedStatement.setString(1, employeeID);
    preparedStatement.setInt(2, serviceID);
    int check = preparedStatement.executeUpdate();
    preparedStatement.close();

    return check != 0;
  }

  public static String findCarLocation(String ID) throws SQLException {
    String select =
        "select ADMIN.PARKINGLOT.NODEID from ADMIN.PARKINGLOT where ADMIN.PARKINGLOT.USERNAME=? ";
    PreparedStatement preparedStatement = getConn().prepareStatement(select);
    preparedStatement.setString(1, ID);
    ResultSet resultSet = preparedStatement.executeQuery();
    String rt = "";
    while (resultSet.next()) {
      rt = resultSet.getString(1);
    }
    return rt;
  }

  public static boolean saveParkingSpot(String nodeID, String userID) throws SQLException {
    String insert = "insert into ADMIN.PARKINGLOT VALUES(?,?)";
    PreparedStatement preparedStatement = getConn().prepareStatement(insert);
    preparedStatement.setString(1, nodeID);
    preparedStatement.setString(2, userID);
    int check = preparedStatement.executeUpdate();

    return check != 0;
  }
}
