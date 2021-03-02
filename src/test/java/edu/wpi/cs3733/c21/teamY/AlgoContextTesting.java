package edu.wpi.cs3733.c21.teamY;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.cs3733.c21.teamY.algorithms.AlgoContext;
import edu.wpi.cs3733.c21.teamY.algorithms.BFSI;
import edu.wpi.cs3733.c21.teamY.algorithms.DFSI;
import edu.wpi.cs3733.c21.teamY.entity.Node;
import edu.wpi.cs3733.c21.teamY.entity.TestGraph;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class AlgoContextTesting {

  @Test
  public void testAlgoContext_BFS0()
      throws SQLException, ClassNotFoundException, NoSuchFieldException, InstantiationException,
          IllegalAccessException, IOException {
    TestGraph.initialize();

    AlgoContext AlgorithmSelection = new AlgoContext();
    AlgorithmSelection.setContext(new BFSI());
    ArrayList<String> goalIDs = new ArrayList<>();
    goalIDs.add("3");

    ArrayList<Node> expectPath = new ArrayList<>();
    // Set the expected path based on the mathematical distance
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("1"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("2"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("3"));

    assertEquals(expectPath, AlgorithmSelection.run(TestGraph.getActiveGraph(), "1", goalIDs, ""));
  }

  @Test
  public void testAlgoContext_BFS1()
      throws SQLException, ClassNotFoundException, NoSuchFieldException, InstantiationException,
          IllegalAccessException, IOException {
    TestGraph.initialize();

    AlgoContext AlgorithmSelection = new AlgoContext();
    AlgorithmSelection.setContext(new BFSI());
    ArrayList<String> goalIDs = new ArrayList<>();
    goalIDs.add("8");

    ArrayList<Node> expectPath = new ArrayList<>();
    // Set the expected path based on the mathematical distance
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("1"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("8"));

    assertEquals(expectPath, AlgorithmSelection.run(TestGraph.getActiveGraph(), "1", goalIDs, ""));
  }

  @Test
  public void testAlgoContext_BFS2()
      throws SQLException, ClassNotFoundException, NoSuchFieldException, InstantiationException,
          IllegalAccessException, IOException {
    TestGraph.initialize();

    AlgoContext AlgorithmSelection = new AlgoContext();
    AlgorithmSelection.setContext(new BFSI());
    ArrayList<String> goalIDs = new ArrayList<>();
    goalIDs.add("8");

    ArrayList<Node> expectPath = new ArrayList<>();
    // Set the expected path based on the mathematical distance
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("2"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("1"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("8"));

    assertEquals(expectPath, AlgorithmSelection.run(TestGraph.getActiveGraph(), "2", goalIDs, ""));
  }

  @Test
  public void testAlgoContext_DFS0()
      throws SQLException, ClassNotFoundException, NoSuchFieldException, InstantiationException,
          IllegalAccessException, IOException {
    TestGraph.initialize();

    AlgoContext AlgorithmSelection = new AlgoContext();
    AlgorithmSelection.setContext(new DFSI());
    ArrayList<String> goalIDs = new ArrayList<>();
    goalIDs.add("8");

    ArrayList<Node> expectPath = new ArrayList<>();
    // Set the expected path based on the mathematical distance
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("2"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("1"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("5"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("3"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("9"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("7"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("8"));

    assertEquals(expectPath, AlgorithmSelection.run(TestGraph.getActiveGraph(), "2", goalIDs, ""));
  }
}
