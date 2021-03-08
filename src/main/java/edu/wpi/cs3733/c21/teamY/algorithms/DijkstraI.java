package edu.wpi.cs3733.c21.teamY.algorithms;

import edu.wpi.cs3733.c21.teamY.entity.Graph;
import edu.wpi.cs3733.c21.teamY.entity.Node;
import java.util.ArrayList;

public class DijkstraI implements IAlgorithms {
  @Override
  public ArrayList<Node> run(
      Graph g, String startID, ArrayList<String> goalIDs, String accessType) {
    DijkstrasTemplate d = new DijkstrasTemplate();
    System.out.println("Running Dijkstra");
    return d.runPathfind(g, startID, goalIDs, accessType);
  }
}
