package edu.wpi.cs3733.c21.teamY.entity;

import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
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

  public static ArrayList<Node> getNodes() {
    return nodes;
  }

  public static ArrayList<Edge> getEdges() {
    return edges;
  }

  private static ArrayList<Node> nodes = new ArrayList<Node>();
  private static ArrayList<Edge> edges = new ArrayList<Edge>();

  public static void initialize() throws SQLException {
    // initializing normal graph
    nodes = DataOperations.getListOfNodes();
    edges = DataOperations.getListOfEdge();
    activeGraph = new Graph(nodes, edges);
  }
}
