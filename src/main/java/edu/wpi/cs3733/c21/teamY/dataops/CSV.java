package edu.wpi.cs3733.c21.teamY.dataops;

import edu.wpi.cs3733.c21.teamY.entity.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class CSV {
  public static final String splitBy = ",";
  public static BufferedReader brNode;
  public static BufferedReader brEdge;
  public static ArrayList<Node> nodes = new ArrayList<>();
  public static ArrayList<Edge> edges = new ArrayList<>();
  public static String nodePath =
      "src/main/resources/edu/wpi/cs3733/c21/teamY/CSV/MapYNodesAllFloors.csv";
  public static String edgePath =
      "src/main/resources/edu/wpi/cs3733/c21/teamY/CSV/MapYEdgesAllFloors.csv";
  public static String edgeTestPath =
      "src/main/resources/edu/wpi/cs3733/c21/teamY/CSV/TestEdge.csv";
  public static String nodeTestPath =
      "src/main/resources/edu/wpi/cs3733/c21/teamY/CSV/TestNode.csv";
  public static String serviceTestPath =
      "src/main/resources/edu/wpi/cs3733/c21/teamY/CSV/Services.csv";
  public static String servicePath = "src/main/resources/edu/wpi/cs3733/c21/teamY/CSV/Services.csv";
  public static String employeePath =
      "src/main/resources/edu/wpi/cs3733/c21/teamY/CSV/Employee.CSV";
  public static BufferedReader brService;
  public static BufferedWriter bwService;

  // load the BufferedReader for node
  static {
    try {
      System.out.println("Working Directory = " + System.getProperty("user.dir"));
      // parsing a CSV file into BufferedReader class constructor

      try {
        brNode = new BufferedReader(new FileReader(nodePath));
        System.out.println("BufferedReader initialized successful!");
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // load the BufferedReader for edge
  static {
    try {
      System.out.println("Working Directory = " + System.getProperty("user.dir"));
      // parsing a CSV file into BufferedReader class constructor

      try {
        brEdge = new BufferedReader(new FileReader(edgePath));
        System.out.println("BufferedReader initialized successful!");
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // Load buffered reader for Service
  static {
    try {
      System.out.println("Working Directory = " + System.getProperty("user.dir"));
      // parsing a CSV file into BufferedReader class constructor

      try {
        CSV.brService = new BufferedReader(new FileReader(CSV.servicePath));
        System.out.println("Service BufferedReader initialized successful!");
      } catch (FileNotFoundException e) {
        System.out.println("Service BufferedReader initialized failed!");

        e.printStackTrace();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // load the bufferWriter for service
  static {
    try {
      CSV.bwService = new BufferedWriter(new FileWriter(CSV.servicePath, true));
      System.out.println("Service BufferedWriter initialized successful!");
    } catch (FileNotFoundException e) {
      System.out.println("Service BufferedWriter initialized failed!");

      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * @throws IOException handle I/O error
   * @return: list of edges from CSV
   */
  public static ArrayList<Edge> getEdgesCSV() throws IOException, IllegalAccessException {
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
   * @throws IOException handle I/O error
   * @return: list of nodes from CSV
   */
  public static ArrayList<Node> getNodesCSV() throws IOException, IllegalAccessException {
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

      Node node =
          new Node(
              nodeType,
              Integer.parseInt(xcoord),
              Integer.parseInt(ycoord),
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
  @Deprecated
  public static boolean saveNodeCSV(Node node) {

    try {
      BufferedWriter bufferedWriter =
          new BufferedWriter(new FileWriter(nodePath, true)); // true = append, false = overwrite
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
  @Deprecated
  public static boolean saveEdgeCSV(Edge edge) {

    try {
      BufferedWriter bufferedWriter =
          new BufferedWriter(new FileWriter(edgePath, true)); // true = append, false = overwrite
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
   * @throws SQLException if there are duplicate keys trying to be inserted or SQL syntax error
   */
  public static void DBtoCSV(String mode) throws SQLException {
    Connection conn = JDBCUtils.getConn();
    String str = ""; // SQL query
    String CSVpath = ""; // CSV file path
    int numAttributes = 0; // number of attributes in that object
    switch (mode) {
      case "EDGE":
        str = "SELECT * FROM ADMIN.EDGE";
        numAttributes = 3;
        CSVpath = edgeTestPath;
        break;
      case "NODE":
        str = "SELECT * FROM ADMIN.NODE";
        numAttributes = 9;
        CSVpath = nodeTestPath;
        break;
      case "SERVICE":
        str = "SELECT * FROM ADMIN.SERVICE";
        numAttributes = 8;
        CSVpath = serviceTestPath;
        break;
    }
    BufferedWriter bufferedWriter; // true = append, false = overwrite
    try {
      bufferedWriter = new BufferedWriter(new FileWriter(CSVpath, false));
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("initialize bufferWriter failed");
      return;
    }
    try {
      Statement statement = conn.createStatement();
      ResultSet resultSet = statement.executeQuery(str);
      StringBuilder stringBuilder = new StringBuilder();
      System.out.println("exporting " + mode + " from database to CSV files");
      switch (mode) {
        case "EDGE":
          bufferedWriter.write("edgeID,startNode,endNode"); // writes header line with fields to CSV

          bufferedWriter.newLine();
          break;
        case "NODE":
          bufferedWriter.write(
              "nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName,teamAssigned"); // writes header line with fields to CSV

          bufferedWriter.newLine();
          break;
        case "SERVICE":
          bufferedWriter.write("serviceID,type,description,location,category,urgency,date,status");
          bufferedWriter.newLine();
          break;
      }
      while (resultSet.next()) {
        for (int i = 1; i <= numAttributes; i++) {
          stringBuilder.append(resultSet.getString(i)).append(",");
        }
        stringBuilder.deleteCharAt(
            stringBuilder.length() - 1); // Gets rid of final unnecessary comma
        bufferedWriter.write(stringBuilder.toString());
        bufferedWriter.newLine();
        stringBuilder.setLength(0); // Clears stringBuilder for the new line
      }
      resultSet.close();
      JDBCUtils.close(null, null, statement, conn);
      bufferedWriter.flush();
      bufferedWriter.close();
    } catch (SQLException | IOException throwables) {
      throwables.printStackTrace();
    }
  }

  // out-dated version of generating CSV file
  @Deprecated
  public static boolean generateEdgeCSV() throws SQLException {
    Connection conn = JDBCUtils.getConn();
    boolean generatedSuccessful = false;
    String str = "SELECT * FROM ADMIN.EDGE";
    BufferedWriter bufferedWriter; // true = append, false = overwrite
    try {
      bufferedWriter = new BufferedWriter(new FileWriter(edgeTestPath, false));
    } catch (IOException e) {

      e.printStackTrace();
      System.out.println("initialize bufferWriter failed");
      return false;
    }

    try {
      Statement statement = conn.createStatement();
      ResultSet resultSet = statement.executeQuery(str);

      System.out.println("exporting Edges from database to CSV files");
      while (resultSet.next()) {
        String stringBuilder =
            resultSet.getString(1) + "," + resultSet.getString(2) + "," + resultSet.getString(3);
        bufferedWriter.write(stringBuilder);
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

  public static ArrayList<Edge> getListOfEdge() throws SQLException {
    return getListOfEdge(ActiveGraph.FilterMapElements.None);
  }

  public static ArrayList<Edge> getListOfEdge(ActiveGraph.FilterMapElements filters)
      throws SQLException {
    Connection conn = JDBCUtils.getConn();
    String str = "SELECT * FROM ADMIN.EDGE";
    ArrayList<Edge> edges = new ArrayList<>();
    String edgeID;
    String startNodeID;
    String endNodeID;
    try {
      Statement statement = conn.createStatement();
      ResultSet resultSet = statement.executeQuery(str);
      System.out.println("exporting Edge from database to list of nodes");
      while (resultSet.next()) {
        edgeID = resultSet.getString(1);
        startNodeID = resultSet.getString(2);
        endNodeID = resultSet.getString(3);

        if (filters == ActiveGraph.FilterMapElements.NoStairs
            || filters == ActiveGraph.FilterMapElements.Employee_NoStairs) {
          if (startNodeID.contains("STAI") || endNodeID.contains("STAI")) {
            continue;
          }
        }
        Edge edge = new Edge(edgeID, startNodeID, endNodeID);
        edges.add(edge);
        JDBCUtils.insert(3, edge, "Edge");
      }
      resultSet.close();
      JDBCUtils.close(null, null, statement, conn);
      return edges;
    } catch (SQLException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Deprecated
  public static ArrayList<Edge> getListOfEdgeNoStairs() throws SQLException {
    Connection conn = JDBCUtils.getConn();
    String str = "SELECT * FROM ADMIN.EDGE";
    ArrayList<Edge> edges = new ArrayList<>();
    String edgeID;
    String startNodeID;
    String endNodeID;
    try {
      Statement statement = conn.createStatement();
      ResultSet resultSet = statement.executeQuery(str);
      System.out.println("exporting Edge from database to list of nodes (No Stairs)");
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
    } catch (SQLException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static ArrayList<Node> getListOfNodes() throws SQLException {
    return getListOfNodes(ActiveGraph.FilterMapElements.None);
  }

  /** @return a list of nodes filtered by ActiveGraph.FilterMapElements */
  public static ArrayList<Node> getListOfNodes(ActiveGraph.FilterMapElements filters)
      throws SQLException {
    // Connection conn=JDBCUtils.getConn();
    Connection conn = JDBCUtils.getConn();
    String str = "SELECT * FROM ADMIN.NODE";
    ArrayList<Node> nodes = new ArrayList<>();
    String nodeType;
    double xcoord;
    double ycoord;
    String floor;
    String building;
    String longName;
    String shortName;
    char teamAssigned;
    String nodeID;
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

        if (filters == ActiveGraph.FilterMapElements.NoStairs
            || filters == ActiveGraph.FilterMapElements.Employee_NoStairs) {
          if (nodeID.contains("STAI")) {
            continue;
          }
        }

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
    } catch (SQLException e) {
      e.printStackTrace();
    }
    if (nodes.size() == 0) {
      System.out.println("zero node in the list, there could be no rows in the table");
    }
    return nodes;
  }

  /**
   * close the writer
   *
   * @param bw the bufferedWriter
   */
  public static void closeWriter(BufferedWriter bw) {
    try {
      bw.flush();
      bw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * close the reader
   *
   * @param br the bufferedReader
   */
  public static void closeReader(BufferedReader br) {
    try {
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * save the service to CSV
   *
   * @param service a service a be saved
   * @return true if write successfully, false otherwise
   */
  @Deprecated
  public static boolean saveServiceToCSV(Service service) {
    boolean hasBeenWritten = false;

    try {
      bwService.write(service.toString());
      System.out.println(service.toString());
      bwService.newLine();
      hasBeenWritten = true;
      bwService.flush();
      // bwService.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println("service Write successfully");
    return hasBeenWritten;
  }

  /**
   * load the CSV file to the DB
   *
   * @throws IOException handles I/O exceptions
   * @throws SQLException if there are duplicate keys trying to be inserted or SQL syntax error
   */
  @Deprecated
  public static void loadCSVtoDB() throws IOException, SQLException {
    String line;
    Connection connection;
    connection = JDBCUtils.getConn();
    System.out.println("start loading service CSV to database");
    String insert = "insert into ADMIN.SERVICE values(?,?,?,?,?,?,?,?,?)";
    PreparedStatement preparedStatement = connection.prepareStatement(insert);
    brService.readLine(); // Reads first line to clear the attributes line
    while ((line = brService.readLine()) != null) {
      String[] strService = line.split(splitBy);
      int serviceID = Integer.parseInt(strService[0]);
      String type = strService[1];
      String description = strService[2];
      String location = strService[3];
      String category = strService[4];
      String urgency = strService[5];
      String date = strService[6];
      int status = Integer.parseInt(strService[7]);
      Service service =
          new Service(serviceID, type, description, location, category, urgency, date, status);
      JDBCUtils.createPreparedStatementInsert(service, preparedStatement);
    }
    System.out.println("Loading successful");
    JDBCUtils.close(preparedStatement, null, null, connection);
    closeReader(brService);
  }

  public static void loadCSVtoDBEmployee() throws IOException, SQLException {
    String line;
    Connection connection;
    connection = JDBCUtils.getConn();
    System.out.println("start loading employee CSV to database");
    String insert = "insert into ADMIN.EMPLOYEE values(?,?,?,?,?,?,?)";
    PreparedStatement preparedStatement = connection.prepareStatement(insert);
    BufferedReader brEmployee = new BufferedReader(new FileReader(employeePath));
    while ((line = brEmployee.readLine()) != null) {
      String[] strEmployee = line.split(splitBy);
      String firstName = strEmployee[0];
      preparedStatement.setString(1, firstName);
      String lastName = strEmployee[1];
      preparedStatement.setString(2, lastName);
      String employeeID = strEmployee[2];
      preparedStatement.setString(3, employeeID);
      String password = strEmployee[3];
      preparedStatement.setString(4, password);
      String email = strEmployee[4];
      preparedStatement.setString(5, email);
      int accessLevel = Integer.parseInt(strEmployee[5]);
      preparedStatement.setInt(6, accessLevel);
      String primaryWorkspace = strEmployee[6];
      preparedStatement.setString(7, primaryWorkspace);
      preparedStatement.executeUpdate();
    }
    System.out.println("Loading successful");
    JDBCUtils.close(preparedStatement, null, null, connection);
    closeReader(brEmployee);
  }
}
