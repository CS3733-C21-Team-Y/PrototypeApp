package edu.wpi.yellowyetis;

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
      "src/main/resources/edu/wpi/yellowyetis/MapYNodesAllFloors.csv";
  public static String edgeCSVpath =
      "src/main/resources/edu/wpi/yellowyetis/MapYEdgesAllFloors.csv";
  public static String edgeTestCSVpath = "src/main/resources/edu/wpi/yellowyetis/TestEdge.csv";
  public static String nodeTestCSVpath = "src/main/resources/edu/wpi/yellowyetis/TestNode.csv";

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
  public static ArrayList<Edge> getEdges() throws IOException {
    String line = brEdge.readLine(); // get rid of first line
    while ((line = brEdge.readLine()) != null) {
      String[] stringEdge = line.split(splitBy); // use comma as separator

      String edgeID = stringEdge[0];
      String startNodeID = stringEdge[1];
      String endNodeID = stringEdge[2];

      Edge edge = new Edge(edgeID, startNodeID, endNodeID);

      edges.add(edge);
    }
    return edges;
  }

  /**
   * @return: list of nodes from CSV
   * @throws IOException
   */
  public static ArrayList<Node> getNodes() throws IOException {
    String line = brEdge.readLine(); // get rid of first line
    while ((line = brNode.readLine()) != null) {
      String[] stringEdge = line.split(splitBy); // use comma as separator

      String edgeID = stringEdge[0];
      String startNodeID = stringEdge[1];
      String endNodeID = stringEdge[2];

      Edge edge = new Edge(edgeID, startNodeID, endNodeID);
      edges.add(edge);
    }
    return nodes;
  }

  /**
   * @param node: a node you want to write to CSV
   * @return true if write successful, false otherwise
   */
  public static boolean saveNode(Node node) {

    try {
      BufferedWriter bufferedWriter =
          new BufferedWriter(new FileWriter(nodeCSVpath, true)); // true = append, false = overwrite
      bufferedWriter.write(node.toString());
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
  public static boolean saveEdge(Edge edge) {

    try {
      BufferedWriter bufferedWriter =
          new BufferedWriter(new FileWriter(edgeCSVpath, true)); // true = append, false = overwrite
      bufferedWriter.write(edge.toString());
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

  // get list of Edges from database
  public static void getListOfObjects() {
    // Connection conn=JDBCUtils.getConn();

  }
  // generate the CSV file from database
  public static void generateCSV() {}
}
