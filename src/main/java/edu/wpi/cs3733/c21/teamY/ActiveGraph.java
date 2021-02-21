package edu.wpi.cs3733.c21.teamY;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ActiveGraph {

  /**
   * Getter for graph used by map stuff for now
   *
   * @return
   */
  public static Graph getActiveGraph() {
    return activeGraph;
  }

  /**
   * Emergency setter for graph used by map stuff for now
   *
   * @return
   */
  public static void setActiveGraph(Graph activeGraph) {
    ActiveGraph.activeGraph = activeGraph;
  }

  private static Graph activeGraph;

  /** Initializes graph from csv. TO BE REPLACED LATER */
  public static void initialize()
      throws SQLException, ClassNotFoundException, IOException, NoSuchFieldException,
          InstantiationException, IllegalAccessException {
    JDBCUtils.fillTablesFromCSV();
    ArrayList<Node> nodes = CSV.getListOfNodes();
    ArrayList<Edge> edges = CSV.getListOfEdge();

    Graph g = new Graph(nodes, edges);
    activeGraph = g;

    //        try {
    //          System.out.println("Working Directory = " + System.getProperty("user.dir"));
    //          // parsing a CSV file into BufferedReader class constructor
    //
    //          BufferedReader br = new BufferedReader(new FileReader("TestMapNodes.csv"));
    //
    //          String line = "";
    //          String splitBy = ",";
    //
    //          line = br.readLine(); // get rid of first line
    //          while ((line = br.readLine()) != null) {
    //            String[] stringNode = line.split(splitBy);
    //
    //            String nodeType = stringNode[5];
    //            double xcoord = Double.parseDouble(stringNode[1]);
    //            double ycoord = Double.parseDouble(stringNode[2]);
    //            String floor = stringNode[3];
    //            String building = stringNode[4];
    //            String longName = stringNode[6];
    //            String shortName = stringNode[7];
    //            char teamAssigned = stringNode[8].charAt(0);
    //            String nodeID = stringNode[0];
    //
    //            Node node =
    //                new Node(
    //                    nodeType,
    //                    xcoord,
    //                    ycoord,
    //                    floor,
    //                    building,
    //                    longName,
    //                    shortName,
    //                    teamAssigned,
    //                    nodeID);
    //            if (!(nodeID == null)) nodes.add(node);
    //          }
    //
    //          br = new BufferedReader(new FileReader("TestMapEdges.csv"));
    //
    //          line = br.readLine(); // get rid of first line
    //          while ((line = br.readLine()) != null) {
    //            String[] stringEdge = line.split(splitBy); // use comma as separator
    //
    //            String edgeID = stringEdge[0];
    //            String startNodeID = stringEdge[1];
    //            String endNodeID = stringEdge[2];
    //
    //            Edge edge = new Edge(edgeID, startNodeID, endNodeID);
    //            edges.add(edge);
    //          }
    //
    //        } catch (Exception e) {
    //          e.printStackTrace();
    //        }

  }
}
