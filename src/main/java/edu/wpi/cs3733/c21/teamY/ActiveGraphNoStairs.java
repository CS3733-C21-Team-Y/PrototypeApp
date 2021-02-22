package edu.wpi.cs3733.c21.teamY;

import java.sql.SQLException;
import java.util.ArrayList;

public class ActiveGraphNoStairs {

  public static Graph getActiveGraphNoStairs;

  private static Graph activeGraphNoStairs;

  /**
   * Getter for graph used by map stuff for now
   *
   * @return
   */
  public static Graph getActiveGraph() {
    return activeGraphNoStairs;
  }

  /**
   * Emergency setter for graph used by map stuff for now
   *
   * @return
   */
  public static void setActiveGraph(Graph activeGraphNoStairs) {
    ActiveGraphNoStairs.activeGraphNoStairs = activeGraphNoStairs;
  }

  public static void initialize() throws SQLException {
    ArrayList<Node> nodes = new ArrayList<Node>();
    ArrayList<Edge> edges = new ArrayList<Edge>();

    nodes = CSV.getListOfNodesNoStairs();
    edges = CSV.getListOfEdgeNoStairs();

    Graph g = new Graph(nodes, edges);
    activeGraphNoStairs = g;
  }
}
