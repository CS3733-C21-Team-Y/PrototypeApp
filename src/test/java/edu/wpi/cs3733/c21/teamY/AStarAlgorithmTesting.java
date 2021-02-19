package edu.wpi.cs3733.c21.teamY;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
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
  public void testAStarAlgorithm_aStar() {
    ActiveGraph.initialize();

    ArrayList<Node> expectPath = new ArrayList<>();
    // Set the expected path based on the mathematical distance
    expectPath.add(0, ActiveGraph.getActiveGraph().nodeFromID("PDEPT00401"));
    expectPath.add(1, ActiveGraph.getActiveGraph().nodeFromID("PDEPT00401"));
    expectPath.add(2, ActiveGraph.getActiveGraph().nodeFromID("PDEPT00401"));
    expectPath.add(3, ActiveGraph.getActiveGraph().nodeFromID("PDEPT00401"));
    expectPath.add(4, ActiveGraph.getActiveGraph().nodeFromID("PDEPT00401"));

    // First test that the basic functionality of aStar is working
    assertEquals(
        expectPath, AStarAlgorithm.aStar(ActiveGraph.getActiveGraph(), "PDEPT00401", "PSTAI00201"));

    expectPath.clear();
    // Set the expected path based on the mathematical distance
    expectPath.add(0, ActiveGraph.getActiveGraph().nodeFromID("PDEPT00401"));
    expectPath.add(1, ActiveGraph.getActiveGraph().nodeFromID("PDEPT00401"));
    expectPath.add(2, ActiveGraph.getActiveGraph().nodeFromID("PDEPT00401"));
    expectPath.add(3, ActiveGraph.getActiveGraph().nodeFromID("PDEPT00401"));

    // Second test that the basic functionality of aStar is working on a less obvious path
    assertEquals(
        expectPath, AStarAlgorithm.aStar(ActiveGraph.getActiveGraph(), "PDEPT00401", "PSTAI00201"));

    expectPath.clear();
    expectPath.add(0, ActiveGraph.getActiveGraph().nodeFromID("PDEPT00401"));
    // Third test on inputting the same start and end node
    assertEquals(
        expectPath, AStarAlgorithm.aStar(ActiveGraph.getActiveGraph(), "PSTAI00201", "PSTAI00201"));
  }
}
