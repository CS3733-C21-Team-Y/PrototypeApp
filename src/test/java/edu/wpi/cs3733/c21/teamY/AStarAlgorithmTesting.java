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
    assertEquals(expectPath, AStarAlgorithm.aStar(TestGraph.getActiveGraph(), "1", "9", ""));

    expectPath.clear();
    // Set the expected path based on the mathematical distance
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("8"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("1"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("2"));
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("4"));

    // Second test that the basic functionality of aStar is working on a less obvious path
    assertEquals(expectPath, AStarAlgorithm.aStar(TestGraph.getActiveGraph(), "8", "4", ""));

    expectPath.clear();
    expectPath.add(0, TestGraph.getActiveGraph().nodeFromID("5"));
    // Third test on inputting the same start and end node
    assertEquals(expectPath, AStarAlgorithm.aStar(TestGraph.getActiveGraph(), "5", "5", ""));
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
    assertEquals(
        expectPath, AStarAlgorithm.aStar(TestGraph.getActiveGraph(), "1", destinations, ""));
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
    assertEquals(
        expectPath, AStarAlgorithm.aStar(TestGraph.getActiveGraph(), "8", destinations, ""));
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
    assertEquals(
        expectPath, AStarAlgorithm.aStar(TestGraph.getActiveGraph(), "8", destinations, ""));
    System.out.println("Test 3 Complete \n");

    expectPath.clear();
    // Set the expected path based on the mathematical distance
    expectPath.add(TestGraph.getActiveGraph().nodeFromID("2"));
    destinations.clear();
    destinations.add("2");

    // Fourth test that aStar will navigate to itself without going to additional nodes
    // Also tests that we can input an arraylist with one element and the function will still work
    assertEquals(
        expectPath, AStarAlgorithm.aStar(TestGraph.getActiveGraph(), "2", destinations, ""));
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

  @Test
  public void testCrossProductDirection() {
    TestGraph.initialize();

    ArrayList<Node> nodes = AStarAlgorithm.aStar(TestGraph.getActiveGraph(), "1", "7", "");
    // First test that the basic functionality of aStar is working
    assertTrue(1 < AStarAlgorithm.directionOfPoint(nodes.get(0), nodes.get(1), nodes.get(2)));

    nodes.clear();
    nodes = AStarAlgorithm.aStar(TestGraph.getActiveGraph(), "1", "4", "");
    assertTrue(-1 > AStarAlgorithm.directionOfPoint(nodes.get(0), nodes.get(1), nodes.get(2)));

    nodes.clear();
    nodes = AStarAlgorithm.aStar(TestGraph.getActiveGraph(), "1", "3", "");
    assertEquals(0, AStarAlgorithm.directionOfPoint(nodes.get(0), nodes.get(1), nodes.get(2)));
  }

  @Test
  public void testCrossProductDirection1() {
    TestGraph.initialize();

    ArrayList<Node> nodes = AStarAlgorithm.aStar(TestGraph.getActiveGraph(), "7", "10", "");
    assertEquals(0.0, AStarAlgorithm.directionOfPoint(nodes.get(0), nodes.get(1), nodes.get(2)));
  }

  @Test
  public void testTextDirections() {
    TestGraph.initialize();

    ArrayList<String> answerList = new ArrayList<>();
    answerList.add("Start from Node1 to Node5");
    answerList.add("Bear right from Node5 to Node7");
    answerList.add("Turn left from Node7 to Node6");
    answerList.add("Continue Straight from Node6 to Node10");
    answerList.add("You have reached your destination.");

    ArrayList<Node> nodes = AStarAlgorithm.aStar(TestGraph.getActiveGraph(), "1", "10", "");
    assertEquals(answerList, AStarAlgorithm.textDirections(nodes));

    ArrayList<String> dest = new ArrayList<>();
    dest.add("10");
    dest.add("7");

    answerList.remove("You have reached your destination.");
    answerList.add("Walk towards Node10 and turn around facing Node6");
    answerList.add("Continue Straight from Node6 to Node7");
    answerList.add("You have reached your destination.");

    nodes = AStarAlgorithm.aStar(TestGraph.getActiveGraph(), "1", dest, "");
    assertEquals(answerList, AStarAlgorithm.textDirections(nodes));
  }
}
