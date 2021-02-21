package edu.wpi.cs3733.c21.teamY;

public class ActiveGraphNoStairs {

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
}
