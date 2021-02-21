package edu.wpi.cs3733.c21.teamY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DijkstrasAlgorithm {

  static String minDistance(HashMap<String, Double> dist, HashMap<String, Boolean> inShortest) {
    // Initialize min value
    double min = Double.MAX_VALUE;
    String min_node = null;

    for (Map.Entry<String, Double> mapElement : dist.entrySet()) {
      if (!inShortest.get(mapElement.getKey()) && dist.get(mapElement.getKey()) <= min) {
        min = dist.get(mapElement.getKey());
        min_node = mapElement.getKey();
      }
    }
    return min_node;
  }

  static double nodeDistance(Node start, Node end) {
    return Math.sqrt(
        Math.pow((end.xcoord - start.xcoord), 2) + Math.pow((end.ycoord - start.ycoord), 2));
  }

  // Function that implements Dijkstra's on a single starting point
  // It will calculate the shortest paths to every point in the map
  // Returns these paths as a hashmap of the node and the shortest path cost to that node
  static HashMap<String, Double> dijkstra(Graph g, String startID, ArrayList<String> goalIDs) {
    ArrayList<String> localGoals = goalIDs;
    // dist will hold the shortest distance from startNode to node
    HashMap<String, Double> dist = new HashMap<>();

    // inShortest will true if node is included in shortest path
    // or if the distance from the startNode to node is finalized
    HashMap<String, Boolean> inShortest = new HashMap<>(g.nodeList.length);

    // Initialize dists as infinite and inShortest as false
    for (Node node : g.nodeList) {
      dist.put(node.nodeID, Double.MAX_VALUE);
      inShortest.put(node.nodeID, false);
    }

    // Distance from start to itself is 0
    dist.put(startID, 0.0);

    // This loop will run once for each node
    for (int i = 0; i < g.nodeList.length - 1; i++) {
      // Pick the minimum distance node from the set of nodes not yet checked.
      // min is always equal to start in first iteration.
      String min = minDistance(dist, inShortest);

      // Mark the picked node as processed
      inShortest.put(min, true);

      // Update dist value of the adjacent nodes of the picked node.
      for (Map.Entry<String, Double> mapElement : dist.entrySet()) {
        // Update dist of the current mapElement only if is not inShortest,
        // there is an edge from min to mapElement,
        // and total weight of path from startNode to mapElement through min
        // is less than the dist of the current mapElement
        double edgeDist = nodeDistance(g.nodeFromID(min), g.nodeFromID(mapElement.getKey()));
        ArrayList<Node> neighbors = g.nodeFromID(mapElement.getKey()).getNeighbors();
        if (!inShortest.get(mapElement.getKey())
            && neighbors.contains(g.nodeFromID(min))
            && dist.get(min) != Integer.MAX_VALUE
            && dist.get(min) + edgeDist < dist.get(mapElement.getKey())) {
          dist.put(mapElement.getKey(), dist.get(min) + edgeDist);
        }
      }
      // Checks whether we've found all our goal nodes and exits early if so
      if (localGoals.contains(min)) {
        localGoals.remove(min);
        if (localGoals.size() == 0) {
          return dist;
        }
      }
    }
    return dist;
  }
}
