package edu.wpi.cs3733.c21.teamY.algorithms;

import edu.wpi.cs3733.c21.teamY.entity.Graph;
import edu.wpi.cs3733.c21.teamY.entity.Node;
import java.util.ArrayList;

public class AlgoContext {

  // Step 4: Create a context class with an attribute of type Strategy interface
  private IAlgorithms pathAlgorithm;

  public AlgoContext() {}

  public AlgoContext(IAlgorithms pathAlgorithm) {
    this.pathAlgorithm = pathAlgorithm;
  }

  // Step 5: Create the context class methods to set the strategy and call the interface methods
  public void setContext(IAlgorithms pathAlgorithm) {
    this.pathAlgorithm = pathAlgorithm;
  }

  public IAlgorithms getContext() {
    return pathAlgorithm;
  }

  public ArrayList<Node> run(
      Graph g, String startID, ArrayList<String> goalIDs, String accessType) {
    return pathAlgorithm.run(g, startID, goalIDs, accessType);
  }
}
