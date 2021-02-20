package edu.wpi.yellowyetis;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.ArrayList;

import edu.wpi.teamY.AStarAlgorithm;
import edu.wpi.teamY.ActiveGraph;
import edu.wpi.teamY.Node;
import org.junit.jupiter.api.Test;

public class AStarAlgorithmTesting {
  @Test
  public void testAStarAlgorithm_nodeDistance() {
    Node start = new Node("walk", 0.0, 0.0, "1", "Faulkner", "TestNode1", "Test", 'y', "node1");
    Node end = new Node("walk", 3.0, 4.0, "1", "Faulkner", "TestNode2", "Test", 'y', "node2");
    // First test that the basic functionality of nodeDistance is working
    assertEquals(5.0, AStarAlgorithm.nodeDistance(start, end));
    start.xcoord = 1.15;
    start.ycoord = 1.05;
    end.xcoord = 0.35;
    end.ycoord = 0.45;
    // Second test that the formula works in the negative direction as well as on decimals
    assertEquals(1.0, AStarAlgorithm.nodeDistance(start, end));

    // Third test the the formula works on a zero distance calculation
    assertEquals(0, AStarAlgorithm.nodeDistance(start, start));
  }

  @Test
  public void testAStarAlgorithm_aStar() throws SQLException {
    ActiveGraph.initialize();

    ArrayList<Node> expectPath = new ArrayList<>();
    // Set the expected path based on the mathematical distance
    expectPath.add(0, ActiveGraph.getActiveGraph().nodeFromID("1"));
    expectPath.add(1, ActiveGraph.getActiveGraph().nodeFromID("5"));
    expectPath.add(2, ActiveGraph.getActiveGraph().nodeFromID("7"));
    expectPath.add(3, ActiveGraph.getActiveGraph().nodeFromID("6"));
    expectPath.add(4, ActiveGraph.getActiveGraph().nodeFromID("10"));

    // First test that the basic functionality of aStar is working
    assertEquals(expectPath, AStarAlgorithm.aStar(ActiveGraph.getActiveGraph(), "1", "10"));

    expectPath.clear();
    // Set the expected path based on the mathematical distance
    expectPath.add(0, ActiveGraph.getActiveGraph().nodeFromID("8"));
    expectPath.add(1, ActiveGraph.getActiveGraph().nodeFromID("1"));
    expectPath.add(2, ActiveGraph.getActiveGraph().nodeFromID("2"));
    expectPath.add(3, ActiveGraph.getActiveGraph().nodeFromID("4"));

    // Second test that the basic functionality of aStar is working on a less obvious path
    assertEquals(expectPath, AStarAlgorithm.aStar(ActiveGraph.getActiveGraph(), "8", "4"));

    expectPath.clear();
    expectPath.add(0, ActiveGraph.getActiveGraph().nodeFromID("5"));
    // Third test on inputting the same start and end node
    assertEquals(expectPath, AStarAlgorithm.aStar(ActiveGraph.getActiveGraph(), "5", "5"));
  }
}
