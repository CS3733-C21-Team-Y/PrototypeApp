package edu.wpi.cs3733.c21.teamY.algorithms;

import edu.wpi.cs3733.c21.teamY.entity.Graph;
import edu.wpi.cs3733.c21.teamY.entity.Node;
import java.util.ArrayList;

public class BFSI implements IAlgorithms {
  private ArrayList<Boolean> visited = new ArrayList<>();

  @Override
  public ArrayList<Node> run(
      Graph g, String startID, ArrayList<String> goalIDs, String accessType) {
    for (int i = 0; i < g.nodeList.length; i++) {
      visited.add(false);
    }
    ArrayList<Node> path = new ArrayList<>();
    return BFSHelper(g, startID, goalIDs, accessType, path);
  }

  private ArrayList<Node> BFSHelper(
      Graph g, String startID, ArrayList<String> goalIDs, String accessType, ArrayList<Node> path) {
    if (visited.get(g.indexFromID(startID))) {
      return null;
    } else {
      visited.set(g.indexFromID(startID), true);
    }
    ArrayList<Node> neighbors = new ArrayList<>();
    neighbors = g.nodeFromID(startID).getNeighbors();
    if (neighbors.size() != 0) {
      // System.out.println("Inside" + startID);
      // Check if any neighbors are the goal
      for (int i = 0; i < neighbors.size(); i++) {
        if (neighbors.get(i).nodeID.equals(goalIDs.get(0))) {
          path.add(g.nodeFromID(startID));
          path.add(neighbors.get(i));
          return path;
        }
      }
      // Run recursively on any non-visited
      for (int i = 0; i < neighbors.size(); i++) {
        // System.out.println("Second loop:" + visited.get(g.indexFromID(neighbors.get(i).nodeID)));
        if (!visited.get(g.indexFromID(neighbors.get(i).nodeID))) {
          // System.out.println("Inside loop:" + neighbors.get(i).nodeID);
          ArrayList<Node> breadth =
              BFSHelper(g, neighbors.get(i).nodeID, goalIDs, accessType, path);
          if (breadth != null) {
            breadth.add(0, g.nodeFromID(startID));
            return breadth;
          }
        }
      }
    }
    return null;
  }
}
