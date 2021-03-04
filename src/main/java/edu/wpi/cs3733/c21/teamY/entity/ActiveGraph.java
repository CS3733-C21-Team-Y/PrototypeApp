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

  private static Graph activeGraphNoStairs;

  public static ArrayList<Node> getNodes() {
    return nodes;
  }

  public static ArrayList<Edge> getEdges() {
    return edges;
  }

  private static ArrayList<Node> nodes = new ArrayList<Node>();
  private static ArrayList<Edge> edges = new ArrayList<Edge>();
  private static ArrayList<Node> nodesNS = new ArrayList<>();
  private static ArrayList<Edge> edgesNS = new ArrayList<>();

  public static void initialize() throws SQLException {
    // initializing normal graph
    nodes = DataOperations.getListOfNodes();
    edges = DataOperations.getListOfEdge();
    activeGraph = new Graph(nodes, edges);
    // initializing no stairs graph
    nodesNS = DataOperations.getListOfNodeNoStairs();
    edgesNS = DataOperations.getListOfEdgeNoStairs();
    activeGraphNoStairs = new Graph(nodesNS, edgesNS);

    // initialize(FilterMapElements.None);
  }

  /** Initializes graph from csv. TO BE REPLACED LATER */
  public static void initialize(FilterMapElements filters) throws SQLException {

    nodes = DataOperations.getListOfNodes();
    edges = DataOperations.getListOfEdge();
    Graph g = new Graph(nodes, edges);
    activeGraph = g;
  }

  public enum FilterMapElements {
    None,
    NoStairs,
    Employee,
    Employee_NoStairs;
  }

  public static Graph getActiveGraphNoStairs() {
    return activeGraphNoStairs;
  }

  public static ArrayList<Node> getNodesNS() {
    return nodesNS;
  }

  public static ArrayList<Edge> getEdgesNS() {
    return edgesNS;
  }
}
