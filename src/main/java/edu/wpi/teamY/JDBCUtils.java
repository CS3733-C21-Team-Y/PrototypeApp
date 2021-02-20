package edu.wpi.teamY;

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
      Statement stmt = conn.createStatement();
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
              + "longName varchar(40) not null ,\n"
              + "shortName varchar(10) not null ,\n"
              + "teamAssigned char not null )";
      stmt.executeUpdate(sqlNode);

      String sqlEdge =
          "create table Edge(edgeID varchar(40) PRIMARY KEY NOT NULL ,\n"
              + "startNode varchar(30) not null ,\n"
              + "endNode varchar(30) not null )";

      stmt.executeUpdate(sqlEdge);
    } catch (SQLException ignored) {

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
   * @param ps
   * @param rs
   * @param stmt
   * @param conn
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
   * @param numArguments
   * @param object
   * @param tableName
   * @return
   * @throws SQLException
   * @throws NoSuchFieldException
   * @throws ClassNotFoundException
   * @throws IllegalAccessException
   * @throws InstantiationException
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
      psInsert.setString(parameterCounter, param);
    }

    return psInsert;
  }

  /**
   * Inserts into specified table the inputted object
   *
   * @param numArgs
   * @param object
   * @param tableName
   * @throws SQLException if there is a duplicate key and updates instead
   * @throws ClassNotFoundException
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws NoSuchFieldException
   */
  public static void insert(int numArgs, Object object, String tableName)
      throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException,
          NoSuchFieldException {
    PreparedStatement statement = createPreparedStatementInsert(numArgs, object, tableName);
    try {
      statement.execute();
      statement.closeOnCompletion();

    } catch (SQLException e) {

      // updates given object value in table if the PK already exists w/in it
      if (e.getErrorCode() == 30000) {
        if (object instanceof Node) {
          JDBCUtils.update((Node) object);
        } else {
          JDBCUtils.update((Edge) object);
        }
      }
    }
    close(statement, null, null, null);
  }

  /**
   * Takes in an arrayList<Node> and one by one inserts them into the Node Table
   *
   * @throws SQLException
   * @throws ClassNotFoundException
   * @throws NoSuchFieldException
   * @throws InstantiationException
   * @throws IllegalAccessException
   */
  public static void insertArrayListNode(ArrayList<Node> nodes)
      throws SQLException, ClassNotFoundException, NoSuchFieldException, InstantiationException,
          IllegalAccessException {

    int size = nodes.size();
    for (Node node : nodes) {
      JDBCUtils.insert(9, node, "Node");
    }
  }

  // INTRODUCE BATCHING FOR PERFORMANCE
  /**
   * Takes in an ArrayList<Edge> and one by one inserts them into the Edge Table
   *
   * @param edges
   * @throws SQLException
   * @throws ClassNotFoundException
   * @throws NoSuchFieldException
   * @throws InstantiationException
   * @throws IllegalAccessException
   */
  public static void insertArrayListEdge(ArrayList<Edge> edges)
      throws SQLException, ClassNotFoundException, NoSuchFieldException, InstantiationException,
          IllegalAccessException {

    int size = edges.size();
    for (Edge edge : edges) {
      JDBCUtils.insert(3, edge, "Edge");
    }
  }

  /**
   * Fills both the Node and Edge table with the data in the CSV files that store Node and Edge data
   * respectively
   *
   * @throws IllegalAccessException
   * @throws IOException
   * @throws NoSuchFieldException
   * @throws SQLException
   * @throws InstantiationException
   * @throws ClassNotFoundException
   */
  public static void fillTablesFromCSV()
      throws IllegalAccessException, IOException, NoSuchFieldException, SQLException,
          InstantiationException, ClassNotFoundException {
    ArrayList<Node> nodes = CSV.getNodesCSV();
    ArrayList<Edge> edges = CSV.getEdgesCSV();
    insertArrayListNode(nodes);
    insertArrayListEdge(edges);
  }

  /**
   * Creates the prepared statement that will be executed by the update method for an update on the
   * Node table
   *
   * @param node
   * @return
   * @throws SQLException
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
   * @param edge
   * @return
   * @throws SQLException
   */
  public static PreparedStatement createPreparedStatementUpdate(Edge edge) throws SQLException {
    Connection connection = getConn();
    PreparedStatement statement =
        connection.prepareStatement(
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
    return statement;
  }

  /**
   * Updates the specified Node with its matching nodeID in the Node table
   *
   * @param node
   * @throws ClassNotFoundException
   * @throws SQLException
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws NoSuchFieldException
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
   * @param edge
   * @throws SQLException
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
   * @param tableName
   * @throws SQLException
   */
  public static void selectQuery(String tableName) throws SQLException {

    String sql = "SELECT * FROM ADMIN." + tableName;
    Statement stmt = null;
    stmt = conn.createStatement();
    stmt.executeQuery(sql);
    stmt.close();
  }

  /**
   * Creates the prepared statement that will be executed by the delete method for an delete on the
   * Edge table
   *
   * @param node
   * @return
   * @throws SQLException
   */
  public static PreparedStatement createPreparedStatementDelete(Node node) throws SQLException {
    Connection connection = getConn();
    return connection.prepareStatement(
        "DELETE FROM ADMIN.NODE WHERE NODEID = '" + node.nodeID + "'");
  }

  /**
   * Creates the prepared statement that will be executed by the delete method for a delete on the
   * Edge table
   *
   * @param edge
   * @return
   * @throws SQLException
   */
  public static PreparedStatement createPreparedStatementDelete(Edge edge) throws SQLException {
    Connection connection = getConn();
    return connection.prepareStatement(
        "DELETE FROM ADMIN.EDGE WHERE EDGEID = '" + edge.edgeID + "'");
  }

  /**
   * Deletes the inputted Node's matching entry in the Node table
   *
   * @param node
   * @throws SQLException
   */
  public static void delete(Node node) throws SQLException {
    PreparedStatement statement = createPreparedStatementDelete(node);
    statement.execute();
    statement.closeOnCompletion();
  }

  /**
   * Deletes the inputted Edge's matching entry in the Edge table
   *
   * @param edge
   * @throws SQLException
   */
  public static void delete(Edge edge) throws SQLException {
    PreparedStatement statement = createPreparedStatementDelete(edge);
    statement.execute();
    statement.closeOnCompletion();
  }
}
