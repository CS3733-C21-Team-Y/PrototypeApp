package edu.wpi.cs3733.c21.teamY.algorithms;

import edu.wpi.cs3733.c21.teamY.entity.Graph;
import edu.wpi.cs3733.c21.teamY.entity.Node;
import java.util.ArrayList;

public class DFSI implements IAlgorithms {
  private ArrayList<Boolean> visited = new ArrayList<>();

  @Override
  public ArrayList<Node> run(
      Graph g, String startID, ArrayList<String> goalIDs, String accessType) {
    System.out.println("Running DFS");
    for (int i = 0; i < g.nodeList.length; i++) {
      visited.add(false);
    }
    ArrayList<Node> path = new ArrayList<>();
    return DFSHelper(g, startID, goalIDs, accessType, path);
  }

  private ArrayList<Node> DFSHelper(
      Graph g, String startID, ArrayList<String> goalIDs, String accessType, ArrayList<Node> path) {
    // If we've already visited the node, end early
    if (visited.get(g.indexFromID(startID))) {
      return null;
    }
    visited.set(g.indexFromID(startID), true);
    Node visit = g.nodeFromID(startID);
    // We found our destination
    if (startID.equals(goalIDs.get(0))) {
      path.add(visit);
      return path;
    } else {
      ArrayList<Node> neighbors = new ArrayList<>();
      neighbors = g.nodeFromID(startID).getNeighbors();
      if (neighbors.size() != 0) {
        for (int i = 0; i < neighbors.size(); i++) {
          ArrayList<Node> deep = DFSHelper(g, neighbors.get(i).nodeID, goalIDs, accessType, path);
          // If the chain of neighbors includes our path
          if (deep != null) {
            deep.add(0, visit);
            return deep;
          }
        }
      }
      return null;
    }
  }
}
