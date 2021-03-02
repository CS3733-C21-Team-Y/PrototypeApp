package edu.wpi.cs3733.c21.teamY.algorithms;

import edu.wpi.cs3733.c21.teamY.entity.Graph;
import edu.wpi.cs3733.c21.teamY.entity.Node;
import java.util.ArrayList;

public class AStarI implements IAlgorithms {
  @Override
  public ArrayList<Node> run(
      Graph g, String startID, ArrayList<String> goalIDs, String accessType) {
    return AlgorithmCalls.aStar(g, startID, goalIDs, accessType);
  }
}
