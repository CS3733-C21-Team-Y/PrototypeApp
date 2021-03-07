package edu.wpi.cs3733.c21.teamY.algorithms;

import edu.wpi.cs3733.c21.teamY.entity.Graph;
import edu.wpi.cs3733.c21.teamY.entity.Node;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DijkstrasAlgorithm {

  // Finds the minimum distance the algorithm can travel to reach a new node.
  public static String minDistance(
      HashMap<String, Double> dist, HashMap<String, Boolean> inShortest) {
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

  // Finds the distance between two nodes
  public static double nodeDistance(Node start, Node end) {
    // if executing neighbor calcs ignore floors
    if (start.getNeighbors().contains(end)) {
      return Math.sqrt(
          Math.pow((end.xcoord - start.xcoord), 2) + Math.pow((end.ycoord - start.ycoord), 2));
    } else {
      // TODO: Modify to efficiently account for floor distance
      // If our end node isn't on the same floor as we are
      // Ideally returns distance to the stair we want to path to
      return Math.sqrt(
              Math.pow((end.xcoord - start.xcoord), 2) + Math.pow((end.ycoord - start.ycoord), 2))
          + (Math.abs(Integer.parseInt(end.floor) - Integer.parseInt(start.floor)) * 210);
    }
  }

  public static ArrayList<Node> dijkstraPath(Graph g, String startID, ArrayList<String> goalIDs) {
    ArrayList<String> localGoals = new ArrayList<>();
    localGoals = (ArrayList<String>) goalIDs.clone();
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
      if (localGoals.get(0).equals(min)) {
        ArrayList<Node> nodeNeighbors = g.nodeFromID(min).getNeighbors();
        ArrayList<Node> nodePath = new ArrayList<>();
        nodePath.add(g.nodeFromID(min));
        while (!nodePath.get(0).nodeID.equals(startID)) {
          Node closeNode = null;
          double nodeDist = Double.MAX_VALUE;
          for (Node k : nodeNeighbors) {
            if (dist.get(k.nodeID) < nodeDist) {
              closeNode = k;
              nodeDist = dist.get(k.nodeID);
            }
          }
          nodePath.add(0, closeNode);
          nodeNeighbors = g.nodeFromID(closeNode.nodeID).getNeighbors();
        }

        return nodePath;
      }




        // Allows you to get all the neighbors of a node
        // iterate through neighbors and find "path of least resistance
        // Once found add to beginning of path list -> this gives you the node g.nodeFromID(node)
        // repeat on that new (closer) node
        // continue until start node is found
        // TODO: Should return the list

    }
    return null;
  }

  /**
   * Calculates the shortest paths to every point in the map until the first goalID is calculated
   *
   * @param g an adjacency-matrix-representation of the graph where (x,y) is the weight of the edge
   *     or 0 if there is no edge.
   * @param startID the node to start from.
   * @param goalIDs the nodes we're searching for.
   * @return modified to return as a stringID of the closest destination
   */
  public static String dijkstra(Graph g, String startID, ArrayList<String> goalIDs) {
    ArrayList<String> localGoals = new ArrayList<>();
    localGoals = (ArrayList<String>) goalIDs.clone();
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
        return min;
      }
    }
    return null;
  }

  /**
   * Locates the closest detourType (bathroom for example) to the nodes in the path
   *
   * @param g an adjacency-matrix-representation of the graph where (x,y) is the weight of the edge
   *     or 0 if there is no edge.
   * @param path the current path from start to end nodes
   * @param endLocations the goal nodes in our path.
   * @param detourType the type of detour we're looking for.
   * @return modified to return the node id of the closest detourType.
   */
  public static ArrayList<String> dijkstraDetour(
      Graph g, ArrayList<Node> path, ArrayList<String> endLocations, String detourType) {
    // dist will hold the shortest distance from startNode to node
    HashMap<String, Double> dist = new HashMap<>();

    // inShortest will true if node is included in shortest path
    // or if the distance from the startNode to node is finalized
    HashMap<String, Boolean> inShortest = new HashMap<>(g.nodeList.length);

    // Initialize dists as infinite and inShortest as false
    for (Node node : g.nodeList) {
      // for the nodes already in the path we'll set the distance to zero so this runs on all the
      // nodes in the path sequence
      if (path.contains(g.nodeFromID(node.nodeID))) {
        dist.put(node.nodeID, 0.0);
      } else {
        dist.put(node.nodeID, Double.MAX_VALUE);
      }
      inShortest.put(node.nodeID, false);
    }

    // This loop will run once for each node
    for (int i = 0; i < g.nodeList.length - 1; i++) {
      // Pick the minimum distance node from the set of nodes not yet checked.
      // min is always equal to start or pathGoalIDs in first iterations.
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
      // Checks whether we've found a node of detourType
      Node minNode = g.nodeFromID(min);
      int end = 0;
      if (minNode.nodeType.equals(detourType)) {
        for (int j = 0; j < path.size(); j++) {
          if (path.get(j).getNeighbors().contains(minNode) || path.get(j) == minNode) {
            endLocations.add(end, min);
            return endLocations;
          }
          if (endLocations.contains(path.get(j).nodeID)) {
            end++;
          }
        }
        endLocations.add(endLocations.size() - 1, min);
        return endLocations;
      }
    }
    return endLocations;
  }
}
