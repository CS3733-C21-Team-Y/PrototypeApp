package edu.wpi.cs3733.c21.teamY;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

// import com.opencsv.CSVWriter;
// import org.apache.commons.csv.CSVFormat;
// import org.apache.commons.csv.CSVPrinter;

public class CSV {
  public static BufferedReader brNode;
  public static BufferedReader brEdge;
  public static ArrayList<Node> nodes = new ArrayList<Node>();
  public static ArrayList<Edge> edges = new ArrayList<Edge>();
  public static final String splitBy = ",";
  public static String nodeCSVpath =
      "src/main/resources/edu/wpi/cs3733/c21/teamY/MapYNodesAllFloors.csv";
  public static String edgeCSVpath =
      "src/main/resources/edu/wpi/cs3733/c21/teamY/MapYEdgesAllFloors.csv";
  public static String edgeTestCSVpath = "src/main/resources/edu/wpi/cs3733/c21/teamY/TestEdge.csv";
  public static String nodeTestCSVpath = "src/main/resources/edu/wpi/cs3733/c21/teamY/TestNode.csv";

  /** load the BufferedReader for node */
  static {
    try {
      System.out.println("Working Directory = " + System.getProperty("user.dir"));
      // parsing a CSV file into BufferedReader class constructor

      try {
        brNode = new BufferedReader(new FileReader(nodeCSVpath));
        System.out.println("BufferedReader initialized successful!");
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** load the BufferedReader for edge */
  static {
    try {
      System.out.println("Working Directory = " + System.getProperty("user.dir"));
      // parsing a CSV file into BufferedReader class constructor

      try {
        brEdge = new BufferedReader(new FileReader(edgeCSVpath));
        System.out.println("BufferedReader initialized successful!");
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * @return: list of edges from CSV
   * @throws IOException
   */
  public static ArrayList<Edge> getEdgesCSV()
      throws IOException, ClassNotFoundException, NoSuchFieldException, InstantiationException,
          IllegalAccessException {
    String line = brEdge.readLine(); // get rid of first line
    while ((line = brEdge.readLine()) != null) {
      String[] stringEdge = line.split(splitBy); // use comma as separator

      String edgeID = stringEdge[0];
      String startNodeID = stringEdge[1];
      String endNodeID = stringEdge[2];

      Edge edge = new Edge(edgeID, startNodeID, endNodeID);

      edges.add(edge);
      // fix this error handling!!!
      try {
        JDBCUtils.insert(3, edge, "Edge");
      } catch (SQLException ignore) {

      }
    }
    return edges;
  }

  /**
   * @return: list of nodes from CSV
   * @throws IOException
   */
  public static ArrayList<Node> getNodesCSV()
      throws IOException, ClassNotFoundException, SQLException, NoSuchFieldException,
          InstantiationException, IllegalAccessException {
    String line = brNode.readLine(); // get rid of first line
    while ((line = brNode.readLine()) != null) {
      String[] stringEdge = line.split(splitBy); // use comma as separator

      String nodeID = stringEdge[0];
      String xcoord = stringEdge[1];
      String ycoord = stringEdge[2];
      String floor = stringEdge[3];
      String building = stringEdge[4];
      String nodeType = stringEdge[5];
      String longName = stringEdge[6];
      String shortName = stringEdge[7];
      String teamAssigned = stringEdge[8];

      // System.out.println(xcoord);

      Node node =
          new Node(
              nodeType,
              (double) Integer.parseInt(xcoord),
              (double) Integer.parseInt(ycoord),
              floor,
              building,
              longName,
              shortName,
              teamAssigned.charAt(0),
              nodeID);
      nodes.add(node);
      try {
        JDBCUtils.insert(10, node, "Node");
      } catch (SQLException ignore) {

      }
    }
    return nodes;
  }

  /**
   * @param node: a node you want to write to CSV
   * @return true if write successful, false otherwise
   */
  public static boolean saveNodeCSV(Node node) {

    try {
      BufferedWriter bufferedWriter =
          new BufferedWriter(new FileWriter(nodeCSVpath, true)); // true = append, false = overwrite
      bufferedWriter.write(node.toString() + "\n");
      bufferedWriter.close();
      System.out.println("node Write successfully");
      return true;

    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }
  // write an Edge to CSV file

  /**
   * @param edge: a edge you want to write(save) to CSV
   * @return true if write successful, false otherwise
   */
  public static boolean saveEdgeCSV(Edge edge) {

    try {
      BufferedWriter bufferedWriter =
          new BufferedWriter(new FileWriter(edgeCSVpath, true)); // true = append, false = overwrite
      bufferedWriter.write(edge.toString() + "\n");
      bufferedWriter.close();
      System.out.println("edge Write successfully");
      return true;

    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  public static void main(String[] args) throws SQLException {
    CSV.generateEdgeCSV();
  }

  /**
   * @param mode can be either "EDGE" or "NODE"
   * @return true if generated successfully, false otherwise
   */
  public static boolean DBtoCSV(String mode) throws SQLException {
    Connection conn = JDBCUtils.getConn();
    String str = ""; // SQL query
    String CSVpath = ""; // CSV file path
    int numAttributes = 0; // number of attributes in that object
    if (mode.equals("EDGE")) {
      str = "SELECT * FROM ADMIN.EDGE";
      numAttributes = 3;
      CSVpath = edgeTestCSVpath;
    } else if (mode.equals("NODE")) {
      str = "SELECT * FROM ADMIN.NODE";
      numAttributes = 10;
      CSVpath = nodeTestCSVpath;
    }
    BufferedWriter bufferedWriter; // true = append, false = overwrite
    try {
      bufferedWriter = new BufferedWriter(new FileWriter(CSVpath, false));
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("initialize bufferWriter failed");
      return false;
    }
    try {
      Statement statement = conn.createStatement();
      ResultSet resultSet = statement.executeQuery(str);
      StringBuilder stringBuilder = new StringBuilder("");
      System.out.println("exporting" + mode + "from database to CSV files");
      while (resultSet.next()) {
        for (int i = 1; i < numAttributes; i++) {
          stringBuilder.append(resultSet.getString(i)).append(",");
        }
        bufferedWriter.write(stringBuilder.toString());
        bufferedWriter.newLine();
      }
      resultSet.close();
      JDBCUtils.close(null, null, statement, conn);
      bufferedWriter.flush();
      bufferedWriter.close();
      return true;
    } catch (SQLException | IOException throwables) {
      throwables.printStackTrace();
    }
    return true;
  }
  // out-dated version of generating CSV file
  public static boolean generateEdgeCSV() throws SQLException {
    Connection conn = JDBCUtils.getConn();
    boolean generatedSuccessful = false;
    String str = "SELECT * FROM ADMIN.EDGE";
    BufferedWriter bufferedWriter; // true = append, false = overwrite
    try {
      bufferedWriter = new BufferedWriter(new FileWriter(edgeTestCSVpath, false));
    } catch (IOException e) {

      e.printStackTrace();
      System.out.println("initialize bufferWriter failed");
      return generatedSuccessful;
    }

    try {
      Statement statement = conn.createStatement();
      ResultSet resultSet = statement.executeQuery(str);

      System.out.println("exporting Edges from database to CSV files");
      while (resultSet.next()) {
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder
            .append(resultSet.getString(1))
            .append(",")
            .append(resultSet.getString(2))
            .append(",")
            .append(resultSet.getString(3));
        bufferedWriter.write(stringBuilder.toString());
        bufferedWriter.newLine();
      }
      resultSet.close();
      JDBCUtils.close(null, null, statement, conn);

      bufferedWriter.flush();
      bufferedWriter.close();
      generatedSuccessful = true;
    } catch (SQLException | IOException throwables) {
      throwables.printStackTrace();
    }
    return generatedSuccessful;
  }

  /*
  public static void SQLexportCSV() {
    PreparedStatement ps = null;
    try {
      ps = JDBCUtils.getConn().prepareStatement("CALLSYSCS_UTIL.SYSCS_EXPORT_TABLE (?,?,?,?,?,?)");
      ps.setString(1, null);
      ps.setString(2, "EDGES");
      ps.setString(3, "TestEdge.csv");
      ps.setString(4, null);
      ps.setString(5, null);
      ps.setString(6, null);
      ps.execute();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }


  }

   */

  public static ArrayList<Edge> getListOfEdge() throws SQLException {
    // Connection conn=JDBCUtils.getConn();
    Connection conn = JDBCUtils.getConn();
    String str = "SELECT * FROM ADMIN.EDGE";
    ArrayList<Edge> edges = new ArrayList<>();
    String edgeID = "";
    String startNodeID = "";
    String endNodeID = "";
    try {
      Statement statement = conn.createStatement();
      ResultSet resultSet = statement.executeQuery(str);
      System.out.println("exporting Nodes from database to list of nodes");
      while (resultSet.next()) {
        edgeID = resultSet.getString(1);
        startNodeID = resultSet.getString(2);
        endNodeID = resultSet.getString(3);
        Edge edge = new Edge(edgeID, startNodeID, endNodeID);
        edges.add(edge);
        JDBCUtils.insert(3, edge, "Edge");
      }
      resultSet.close();
      JDBCUtils.close(null, null, statement, conn);
      return edges;
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  /** @return a list of nodes */
  public static ArrayList<Node> getListOfNodes() throws SQLException {
    // Connection conn=JDBCUtils.getConn();
    Connection conn = JDBCUtils.getConn();
    String str = "SELECT * FROM ADMIN.NODE";
    ArrayList<Node> nodes = new ArrayList<>();
    String nodeType = "";
    double xcoord = 0;
    double ycoord = 0;
    String floor = "";
    String building = "";
    String longName = "";
    String shortName = "";
    char teamAssigned = 'X';
    String nodeID = "";
    try {
      Statement statement = conn.createStatement();
      ResultSet resultSet = statement.executeQuery(str);
      System.out.println("exporting Nodes from database to list of nodes");
      while (resultSet.next()) {
        nodeID = resultSet.getString(1);
        nodeType = resultSet.getString(2);
        xcoord = resultSet.getDouble(3);
        ycoord = resultSet.getDouble(4);
        floor = resultSet.getString(5);
        building = resultSet.getString(6);
        longName = resultSet.getString(7);
        shortName = resultSet.getString(8);
        teamAssigned = resultSet.getString(9).charAt(0);

        Node node =
            new Node(
                nodeType,
                xcoord,
                ycoord,
                floor,
                building,
                longName,
                shortName,
                teamAssigned,
                nodeID);
        nodes.add(node);
      }
      resultSet.close();
      JDBCUtils.close(null, null, statement, conn);
      return nodes;
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    if (nodes.size() == 0) {
      System.out.println("zero node in the list, there could be no rows in the table");
    }
    return nodes;
  }
  // generate the CSV file from database
  public static void DBtoCSV() {}
}
