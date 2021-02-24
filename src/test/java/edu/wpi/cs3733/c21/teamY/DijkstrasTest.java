package edu.wpi.cs3733.c21.teamY;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.cs3733.c21.teamY.algorithms.DijkstrasAlgorithm;
import edu.wpi.cs3733.c21.teamY.entity.Node;
import edu.wpi.cs3733.c21.teamY.entity.TestGraph;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

public class DijkstrasTest {
  @Test
  public void testMinDistance() {
    HashMap<String, Double> distance = new HashMap<>();
    HashMap<String, Boolean> shortList = new HashMap<>();

    // We will simulate starting at node ten and moving out from that point
    // 10 is our start node. All distances are from 10 to that node
    distance.put("1", Double.MAX_VALUE);
    distance.put("2", Double.MAX_VALUE);
    distance.put("3", Double.MAX_VALUE);
    distance.put("4", Double.MAX_VALUE);
    distance.put("5", Double.MAX_VALUE);
    distance.put("6", 5.25);
    distance.put("7", 7.5);
    distance.put("8", Double.MAX_VALUE);
    distance.put("9", Double.MAX_VALUE);
    distance.put("10", 0.0);

    shortList.put("1", false);
    shortList.put("2", false);
    shortList.put("3", false);
    shortList.put("4", false);
    shortList.put("5", false);
    shortList.put("6", true);
    shortList.put("7", false);
    shortList.put("8", false);
    shortList.put("9", false);
    shortList.put("10", true);

    // First test that the basic functionality of minDistance is working
    assertEquals("7", DijkstrasAlgorithm.minDistance(distance, shortList));

    TestGraph.initialize();

    distance.put("5", 9.5);
    distance.put(
        "9",
        7.5
            + DijkstrasAlgorithm.nodeDistance(
                TestGraph.getActiveGraph().nodeFromID("7"),
                TestGraph.getActiveGraph().nodeFromID("9")));
    distance.put("8", 9.75);
    shortList.put("7", true);

    // Second test on the next iteration of the sequence
    assertEquals("5", DijkstrasAlgorithm.minDistance(distance, shortList));
  }

  @Test
  public void testNodeDistance() {
    Node start = new Node("walk", 0.0, 0.0, "1", "Faulkner", "TestNode1", "Test", 'y', "node1");
    Node end = new Node("walk", 3.0, 4.0, "1", "Faulkner", "TestNode2", "Test", 'y', "node2");
    // First test that the basic functionality of nodeDistance is working
    assertEquals(5.0, DijkstrasAlgorithm.nodeDistance(start, end));
    start.xcoord = 1.15;
    start.ycoord = 1.05;
    end.xcoord = 0.35;
    end.ycoord = 0.45;
    // Second test that the formula works in the negative direction as well as on decimals
    assertEquals(1.0, DijkstrasAlgorithm.nodeDistance(start, end));

    // Third test the the formula works on a zero distance calculation
    assertEquals(0, DijkstrasAlgorithm.nodeDistance(start, start));
  }

  @Test
  public void testDijkstraHash() {
    TestGraph.initialize();

    ArrayList<String> destinations = new ArrayList<>();
    destinations.add("3");
    destinations.add("4");

    HashMap<String, Double> answerKey = new HashMap<>();
    answerKey = DijkstrasAlgorithm.dijkstra(TestGraph.getActiveGraph(), "1", destinations);

    // First tests that the basic functionality of dijkstras is working
    // We're checking that the hashmap was generated correctly for a few specific examples
    assertEquals(4.5, answerKey.get("3"));
    assertEquals(
        3.25
            + DijkstrasAlgorithm.nodeDistance(
                TestGraph.getActiveGraph().nodeFromID("2"),
                TestGraph.getActiveGraph().nodeFromID("4")),
        answerKey.get("4"));
  }

  @Test
  public void testDijkstraDetour() {
    TestGraph.initialize();

    ArrayList<String> destinations = new ArrayList<>();
    destinations.add("3");
    destinations.add("4");

    // First test that the basic functionality of detour is working
    assertEquals(
        "5",
        DijkstrasAlgorithm.dijkstraDetour(TestGraph.getActiveGraph(), "1", destinations, "FOOD"));

    destinations.remove("3");
    destinations.add("6");

    // Second test that detour works if a node included is already the detour type
    assertEquals(
        "6",
        DijkstrasAlgorithm.dijkstraDetour(TestGraph.getActiveGraph(), "1", destinations, "FOOD"));
  }
}
