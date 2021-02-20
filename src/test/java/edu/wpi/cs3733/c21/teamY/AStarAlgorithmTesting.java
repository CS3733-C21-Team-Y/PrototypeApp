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
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("1"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("2"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("3"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("9"));

    // First test that the basic functionality of aStar is working
    assertEquals(expectPath, AStarAlgorithm.aStar(ActiveGraph.getActiveGraph(), "1", "9"));

    expectPath.clear();
    // Set the expected path based on the mathematical distance
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("8"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("1"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("2"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("4"));

    // Second test that the basic functionality of aStar is working on a less obvious path
    assertEquals(expectPath, AStarAlgorithm.aStar(ActiveGraph.getActiveGraph(), "8", "4"));

    expectPath.clear();
    expectPath.add(0, ActiveGraph.getActiveGraph().nodeFromID("5"));
    // Third test on inputting the same start and end node
    assertEquals(expectPath, AStarAlgorithm.aStar(ActiveGraph.getActiveGraph(), "5", "5"));
  }

  @Test
  public void testAStarAlgorithm_multipleAStar() {
    ActiveGraph.initialize();

    ArrayList<Node> expectPath = new ArrayList<>();
    // Set the expected path based on the mathematical distance
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("1"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("2"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("3"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("2"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("4"));

    ArrayList<String> destinations = new ArrayList<>();
    destinations.add("3");
    destinations.add("4");

    // First test that the basic functionality of aStar is working
    assertEquals(expectPath, AStarAlgorithm.aStar(ActiveGraph.getActiveGraph(), "1", destinations));
    System.out.println("Test 1 Complete \n");

    expectPath.clear();
    // Set the expected path based on the mathematical distance
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("8"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("1"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("2"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("4"));

    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("2"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("3"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("5"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("7"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("6"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("10"));

    destinations.clear();
    destinations.add("4");
    destinations.add("10");

    // Second test that the basic functionality of aStar is working on a less obvious path
    assertEquals(expectPath, AStarAlgorithm.aStar(ActiveGraph.getActiveGraph(), "8", destinations));
    System.out.println("Test 2 Complete \n");

    // NEW TEST WITH 5 DESTINATIONS
    expectPath.clear();
    // Set the expected path based on the mathematical distance
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("8"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("1"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("2"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("4"));

    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("2"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("3"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("5"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("7"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("6"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("10"));

    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("6"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("7"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("5"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("3"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("2"));

    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("1"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("8"));

    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("7"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("9"));

    destinations.clear();
    destinations.add("4");
    destinations.add("10");
    destinations.add("2");
    destinations.add("1");
    destinations.add("8");
    destinations.add("9");

    // Third test that the basic functionality of aStar is working on a path with 6 destinations
    assertEquals(expectPath, AStarAlgorithm.aStar(ActiveGraph.getActiveGraph(), "8", destinations));
    System.out.println("Test 3 Complete \n");

    expectPath.clear();
    // Set the expected path based on the mathematical distance
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("1"));
    expectPath.add(ActiveGraph.getActiveGraph().nodeFromID("2"));
    destinations.clear();
    destinations.add("2");
    destinations.add("2");

    // Fourth test that aStar will navigate to itself without going to additional nodes
    assertEquals(expectPath, AStarAlgorithm.aStar(ActiveGraph.getActiveGraph(), "1", destinations));
    System.out.println("Test 4 Complete \n");
  }
}
