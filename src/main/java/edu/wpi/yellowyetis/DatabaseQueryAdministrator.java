package edu.wpi.yellowyetis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseQueryAdministrator {

  // static Connection conn;
  public static Graph createGraph() {
    return new Graph(CSV.nodes, CSV.edges);
  }

  /**
   * Takes an ArrayList<Node> and iterates through, inserting each into its respective [tableName]
   * table
   *
   * @param nodes
   * @param tableName
   */
  public static void insertArrayListNode(ArrayList<Node> nodes, String tableName)
      throws SQLException, ClassNotFoundException, NoSuchFieldException, InstantiationException,
          IllegalAccessException {

    int numDuplicateIDs = 0;

    int size = nodes.size();
    for (Node node : nodes) {
      JDBCUtils.insert(9, node, "Node");
    }

    if (numDuplicateIDs > 0) {
      System.out.println(
          "There were "
              + numDuplicateIDs
              + " duplicate node ID's within the entries that were instead updated!");
    } else System.out.println("Success!");
  }

  /**
   * Takes an ArrayList<Edge> and iterates through, inserting each into its respective [tableName]
   * table
   *
   * @param edges
   * @param tableName
   */
  public static void insertArrayListEdge(ArrayList<Edge> edges, String tableName)
      throws SQLException {
    int numDuplicateIDs = 0;

    int size = edges.size();
    for (int i = 0; i < size; i++) {
      if (insertEdge(edges.get(i)) == 1) {
        numDuplicateIDs++;
      }
    }
    if (numDuplicateIDs > 0) {
      System.out.println(
          "There were "
              + numDuplicateIDs
              + " duplicate edge ID's within the entries that were instead updated!");
    } else {
      System.out.println("Success!");
    }
  }

  /**
   * Takes a Node and inserts into the Table within embedded database with given TableName
   *
   * @param node
   */
  public static int insertNode(Node node) throws SQLException {

    // setup for database connection beginning
    Connection conn = JDBCUtils.getConn();
    try {
      // Creation of SQL insert
      PreparedStatement psInsert =
          conn.prepareStatement(
              "insert into ADMIN.NODE values((?),(?),(?),(?),(?),(?),(?),(?),(?),(?))");

      psInsert.setString(1, node.nodeID);
      psInsert.setString(2, node.nodeType);
      psInsert.setDouble(3, node.xcoord);
      psInsert.setDouble(4, node.ycoord);
      psInsert.setString(5, node.floor);
      psInsert.setString(6, node.building);
      psInsert.setString(7, node.room);
      psInsert.setString(8, node.longName);
      psInsert.setString(9, node.shortName);
      psInsert.setString(10, Character.toString(node.teamAssigned));
      psInsert.executeUpdate();
      JDBCUtils.close(psInsert, null, null, conn);

      // createPreparedStatementNode(node, psInsert);

      return 0;

    } catch (SQLException e) {

      // System.out.println(e.getErrorCode());
      if (e.getErrorCode() == 30000) {
        updateNode(node, "ADMIN.NODE");
        return 1;
      } else {
        System.out.println("~Node~ Get Data Failed! Check output console");
        return 0;
      }
    }
  }

  /**
   * Takes an Edge and inserted it into the Table within embedded database with given tableName
   *
   * @param edge
   */
  public static int insertEdge(Edge edge) throws SQLException {
    // setup for database connection beginning
    Connection conn = JDBCUtils.getConn();

    try {
      // Creation of SQL insert
      PreparedStatement psInsert =
          conn.prepareStatement("insert into ADMIN.EDGE values((?),(?),(?))");
      psInsert.setString(1, edge.edgeID);
      psInsert.setString(2, edge.startNodeID);
      psInsert.setString(3, edge.endNodeID);
      psInsert.executeUpdate();
      // int affectedRow = psInsert.executeUpdate();

      JDBCUtils.close(psInsert, null, null, conn);
      return 0;

    } catch (SQLException e) {
      if (e.getErrorCode() == 30000) {
        updateEdge(edge, "ADMIN.EDGE");
        return 1;
      } else {
        System.out.println("~Edge~ Get Data Failed! Check output console");
        return 0;
      }
    }
  }

  public static void createPreparedStatementEdge(
      PreparedStatement psInsert,
      int i,
      String edgeID,
      int i2,
      String startNodeID,
      int i3,
      String endNodeID)
      throws SQLException {
    psInsert.setString(i, edgeID);
    psInsert.setString(i2, startNodeID);
    psInsert.setString(i3, endNodeID);
  }

  /**
   * Takes in a node and a table name and updates the given table with the new node info for the
   * node with a matching ID
   *
   * @param node
   * @param tableName
   */
  public static void updateNode(Node node, String tableName) throws SQLException {
    // setup for database connection beginning
    Connection conn = JDBCUtils.getConn();
    try {
      // Creation of SQL insert
      PreparedStatement psInsert =
          conn.prepareStatement(
              "update ADMIN."
                  + tableName
                  + " SET ADMIN."
                  + tableName
                  + ".id = (?),"
                  + " ADMIN."
                  + tableName
                  + ".xcoord = (?), "
                  + "ADMIN."
                  + tableName
                  + ".ycoord = (?) "
                  + "ADMIN."
                  + tableName
                  + ".floor = (?) "
                  + "ADMIN."
                  + tableName
                  + ".building = (?) "
                  + "ADMIN."
                  + tableName
                  + ".nodeType = (?) "
                  + "ADMIN."
                  + tableName
                  + ".longName = (?) "
                  + "ADMIN."
                  + tableName
                  + ".shortName = (?) "
                  + "where ADMIN."
                  + tableName
                  + ".id = "
                  + node.nodeID);

      createPreparedStatementNode(node, psInsert);

    } catch (SQLException e) {

      // System.out.println(e.getErrorCode());
      if (e.getErrorCode() == 30000) {

      } else {
        System.out.println("~Node~ Get Data Failed! Check output console");
      }
    }
  }

  public static void updateEdge(Edge edge, String tableName) throws SQLException {
    Connection conn = JDBCUtils.getConn();
    try {
      // Creation of SQL insert
      PreparedStatement psInsert =
          conn.prepareStatement(
              "update ADMIN."
                  + tableName
                  + " SET ADMIN."
                  + tableName
                  + ".id = (?),"
                  + " ADMIN."
                  + tableName
                  + ".startNode = (?), ADMIN."
                  + tableName
                  + ".endNode = (?) "
                  + "where ADMIN."
                  + tableName
                  + ".id = "
                  + edge.edgeID);

      createPreparedStatementEdge(psInsert, 1, edge.edgeID, 2, edge.startNodeID, 3, edge.endNodeID);

      int affectedRow = psInsert.executeUpdate();

      psInsert.close();

    } catch (SQLException e) {
      if (e.getErrorCode() == 30000) {

      } else {
        System.out.println("~Node~ Get Data Failed! Check output console");
      }
    }
  }

  /** @param nodeID nodeID of the node which you want to remove */
  public static void removeNode(String nodeID) throws SQLException { // string tableName
    Connection conn = JDBCUtils.getConn();

    try { // currently the database foes not function properly the cannot find Node
      PreparedStatement psDelete =
          conn.prepareStatement("DELETE FROM ADMIN.NODE WHERE ADMIN.NODE.NodeID=(?)");
      psDelete.setString(1, nodeID);
      int affectRow = psDelete.executeUpdate();
      if (affectRow == 0) {
        System.out.println("Node with ID " + nodeID + " does not exist");
      } else {
        System.out.println("delete successful");
      }
      JDBCUtils.close(psDelete, null, null, conn);
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  /** @param edgeID: edgeID of the edge which you want to remove */
  public static void removeEdge(String edgeID) throws SQLException {
    Connection conn = JDBCUtils.getConn();

    try {
      PreparedStatement psDelete =
          conn.prepareStatement("DELETE FROM ADMIN.EDGE WHERE ADMIN.EDGE.edgeID=(?)");
      psDelete.setString(1, edgeID);
      int affectRow = psDelete.executeUpdate();
      if (affectRow == 0) {
        System.out.println("Edge with ID " + edgeID + " does not exist");
      } else {
        System.out.println("delete successful");
      }
      JDBCUtils.close(psDelete, null, null, conn);
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  private static void createPreparedStatementNode(Node node, PreparedStatement psInsert)
      throws SQLException {
    psInsert.setString(1, node.nodeID);
    psInsert.setString(2, String.valueOf(node.xcoord));
    psInsert.setString(3, String.valueOf(node.ycoord));
    psInsert.setString(4, node.floor);
    psInsert.setString(5, node.building);
    psInsert.setString(6, node.nodeType);
    psInsert.setString(7, node.longName);
    psInsert.setString(8, node.shortName);

    int affectedRow = psInsert.executeUpdate();

    psInsert.close();
  }
}
