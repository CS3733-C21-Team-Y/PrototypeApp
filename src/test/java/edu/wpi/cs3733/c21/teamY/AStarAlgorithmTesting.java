package edu.wpi.cs3733.c21.teamY;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.cs3733.c21.teamY.algorithms.AStarAlgorithm;
import edu.wpi.cs3733.c21.teamY.entity.Node;
import edu.wpi.cs3733.c21.teamY.entity.TestGraph;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class AStarAlgorithmTesting {
  @Test
  public void testAStarAlgorithm_aStar()
      throws SQLException, ClassNotFoundException, NoSuchFieldException, InstantiationException,
          IllegalAccessException, IOException {
    TestGraph.initialize();

    ArrayList<Node> expectPath = new ArrayList<>();
    // Set the expected path based on the mathematical distance
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("1"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("2"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("3"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("9"));

    // First test that the basic functionality of aStar is working
    assertEquals(expectPath, AStarAlgorithm.aStar(TestGraph.getActiveGraph(), "1", "9"));

    expectPath.clear();
    // Set the expected path based on the mathematical distance
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("8"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("1"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("2"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("4"));

    // Second test that the basic functionality of aStar is working on a less obvious path
    assertEquals(expectPath, AStarAlgorithm.aStar(TestGraph.getActiveGraph(), "8", "4"));

    expectPath.clear();
    expectPath.add(0, TestGraph.getActiveGraph().nodeFromID("5"));
    // Third test on inputting the same start and end node
    assertEquals(expectPath, AStarAlgorithm.aStar(TestGraph.getActiveGraph(), "5", "5"));
  }

  @Test
  public void testAStarAlgorithm_multipleAStar() throws SQLException {
    TestGraph.initialize();

    ArrayList<Node> expectPath = new ArrayList<>();
    // Set the expected path based on the mathematical distance
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("1"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("2"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("3"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("2"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("4"));

    ArrayList<String> destinations = new ArrayList<>();
    destinations.add("3");
    destinations.add("4");

    // First test that the basic functionality of aStar is working
    assertEquals(expectPath, AStarAlgorithm.aStar(TestGraph.getActiveGraph(), "1", destinations));
    System.out.println("Test 1 Complete \n");

    expectPath.clear();
    // Set the expected path based on the mathematical distance
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("8"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("1"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("2"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("4"));

    expectPath.add(TestGraph.getActiveGraph().nodeFromID("2"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("3"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("5"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("7"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("6"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("10"));

    destinations.clear();
    destinations.add("4");
    destinations.add("10");

    // Second test that the basic functionality of aStar is working on a less obvious path
    assertEquals(expectPath, AStarAlgorithm.aStar(TestGraph.getActiveGraph(), "8", destinations));
    System.out.println("Test 2 Complete \n");

    // Third Test With 6 Destinations
    expectPath.clear();
    // Set the expected path based on the mathematical distance
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("8"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("1"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("2"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("4"));

    expectPath.add(TestGraph.getActiveGraph().nodeFromID("2"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("3"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("5"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("7"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("6"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("10"));

    expectPath.add(TestGraph.getActiveGraph().nodeFromID("6"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("7"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("5"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("3"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("2"));

    expectPath.add(TestGraph.getActiveGraph().nodeFromID("1"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("8"));

    expectPath.add(TestGraph.getActiveGraph().nodeFromID("7"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("9"));

    destinations.clear();
    destinations.add("4");
    destinations.add("10");
    destinations.add("2");
    destinations.add("1");
    destinations.add("8");
    destinations.add("9");

    // Third test that the basic functionality of aStar is working on a path with 6 destinations
    assertEquals(expectPath, AStarAlgorithm.aStar(TestGraph.getActiveGraph(), "8", destinations));
    System.out.println("Test 3 Complete \n");

    expectPath.clear();
    // Set the expected path based on the mathematical distance
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("2"));
    destinations.clear();
    destinations.add("2");

    // Fourth test that aStar will navigate to itself without going to additional nodes
    // Also tests that we can input an arraylist with one element and the function will still work
    assertEquals(expectPath, AStarAlgorithm.aStar(TestGraph.getActiveGraph(), "2", destinations));
    System.out.println("Test 4 Complete \n");
  }

  @Test
  public void testAStarAlgorithm_nearestNeighbor() {
    TestGraph.initialize();

    ArrayList<String> destinations = new ArrayList<>();
    destinations.add("2");
    destinations.add("4");

    ArrayList<String> answerKey = new ArrayList<>();
    answerKey.add("2");
    answerKey.add("4");

    // First test that the basic functionality of aStar is working
    assertEquals(
        answerKey, AStarAlgorithm.nearestNeighbor(TestGraph.getActiveGraph(), "1", destinations));
    System.out.println("Test 5 Complete \n");

    destinations.clear();
    answerKey.clear();

    destinations.add("4");
    destinations.add("2");

    answerKey.add("2");
    answerKey.add("4");

    assertEquals(
        answerKey, AStarAlgorithm.nearestNeighbor(TestGraph.getActiveGraph(), "1", destinations));
    System.out.println("Test 6 Complete \n");

    destinations.clear();
    answerKey.clear();

    destinations.add("4");

    answerKey.add("4");

    assertEquals(
        answerKey, AStarAlgorithm.nearestNeighbor(TestGraph.getActiveGraph(), "1", destinations));
    System.out.println("Test 7 Complete \n");
  }
}
