package edu.wpi.cs3733.c21.teamY.algorithms;

import edu.wpi.cs3733.c21.teamY.entity.Graph;
import edu.wpi.cs3733.c21.teamY.entity.Node;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Used to perform the A-Star (A*) Algorithm to find the shortest path from a start to a target
 * node.
 */
public class AStarAlgorithm {

  /**
   * Finds the shortest distance between two nodes using the A-star algorithm
   *
   * @param g an adjacency-matrix-representation of the graph where (x,y) is the weight of the edge
   *     or 0 if there is no edge.
   * @param startID the node to start from.
   * @param goalID the node we're searching for.
   * @return modified to return the path.
   */
  public static ArrayList<Node> aStar(Graph g, String startID, String goalID, String accessType) {

    // instantiating adjacency matrix
    double[][] graph = g.adjMatrix;
    // This contains the distances from the start node to all other nodes
    double[] distances = new double[graph.length];

    //
    HashMap<Integer, Integer> cameFrom = new HashMap();
    // Initializing with a distance of "Infinity"
    Arrays.fill(distances, Integer.MAX_VALUE);
    // The distance from the start node to itself is of course 0

    int start = g.indexFromID(startID);
    int goal = g.indexFromID(goalID);

    distances[start] = 0;

    // This contains the priorities with which to visit the nodes, calculated using the heuristic.
    double[] priorities = new double[graph.length];
    // Initializing with a priority of "Infinity"
    Arrays.fill(priorities, Integer.MAX_VALUE);
    // start node has a priority equal to straight line distance to goal. It will be the first to be
    // expanded.
    priorities[start] = DijkstrasAlgorithm.nodeDistance(g.nodeList[start], g.nodeList[goal]);

    // This contains whether a node was already visited
    boolean[] visited = new boolean[graph.length];

    // While there are nodes left to visit...
    while (true) {
      // ... find the node with the currently lowest priority...
      double lowestPriority = Integer.MAX_VALUE;
      int lowestPriorityIndex = -1;
      for (int i = 0; i < priorities.length; i++) {
        // ... by going through all nodes that haven't been visited yet
        if (priorities[i] < lowestPriority
            && !visited[i]
            && !g.nodeList[i].nodeType.equals(accessType)) {
          lowestPriority = priorities[i];
          lowestPriorityIndex = i;
        }
      }

      if (lowestPriorityIndex == -1) {
        // There was no node not yet visited --> Node not found
        return null;
      } else if (lowestPriorityIndex == goal) {
        // Goal node found
        System.out.println("Goal node found!");

        int pathIndex = goal;
        ArrayList<Node> path = new ArrayList<>();
        while (pathIndex != start) {
          path.add(0, g.nodeList[pathIndex]);
          pathIndex = cameFrom.get(pathIndex);
        }
        path.add(0, g.nodeList[start]);
        System.out.println(path);

        return path;
      }

      System.out.println(
          "Visiting node "
              + g.nodeList[lowestPriorityIndex].nodeID
              + " with currently lowest priority of "
              + lowestPriority);

      // ...then, for all neighboring nodes that haven't been visited yet....
      for (int i = 0; i < graph[lowestPriorityIndex].length; i++) {
        if (graph[lowestPriorityIndex][i] != 0
            && graph[lowestPriorityIndex][i] != Integer.MAX_VALUE
            && !visited[i]) {
          // ...if the path over this edge is shorter...
          if (distances[lowestPriorityIndex] + graph[lowestPriorityIndex][i] < distances[i]) {
            // ...save this path as new shortest path
            distances[i] = distances[lowestPriorityIndex] + graph[lowestPriorityIndex][i];
            // ...and set the priority with which we should continue with this node
            priorities[i] =
                distances[i] + DijkstrasAlgorithm.nodeDistance(g.nodeList[i], g.nodeList[goal]);

            cameFrom.put(i, lowestPriorityIndex);
          }
        }
      }

      // Lastly, note that we are finished with this node.
      visited[lowestPriorityIndex] = true;
    }
  }

  /**
   * Finds the shortest distance between a start node, and multiple end nodes using the A-star
   * algorithm
   *
   * @param g an adjacency-matrix-representation of the graph where (x,y) is the weight of the edge
   *     or 0 if there is no edge.
   * @param startID the node to start from.
   * @param goalIDs the nodes we're searching for in desired order.
   * @return modified to return the path.
   */
  public static ArrayList<Node> aStar(
      Graph g, String startID, ArrayList<String> goalIDs, String accessType) {
    ArrayList<Node> path;
    path = aStar(g, startID, goalIDs.get(0), accessType);
    for (int i = 1; i < goalIDs.size(); i++) {
      ArrayList<Node> tempPath;
      tempPath = aStar(g, goalIDs.get(i - 1), goalIDs.get(i), accessType);
      // Remove the first element to avoid duplicates
      tempPath.remove(0);
      // Append the path for these nodes to the path
      path.addAll(tempPath);
    }

    return path;
  }

  public static double directionOfPoint(Node node1, Node node2, Node P) {
    // subtracting co-ordinates of point A
    // from B and P, to make A as origin

    double ZERO = 0.0;

    double v1_x = node2.xcoord - node1.xcoord;
    double v1_y = node2.ycoord - node1.ycoord;

    double v2_x = P.xcoord - node2.xcoord;
    double v2_y = P.ycoord - node2.ycoord;

    double dot_product = (v1_x * v2_x) + (v1_y * v2_y);

    double v1Mag = Math.sqrt(Math.pow(v1_x, 2) + Math.pow(v1_y, 2));
    double v2Mag = Math.sqrt(Math.pow(v2_x, 2) + Math.pow(v2_y, 2));
    double angle = (180 / Math.PI) * Math.acos(dot_product / (v1Mag * v2Mag));

    // Determining cross Product
    double cross_product = (node2.xcoord * P.ycoord - node2.ycoord * P.xcoord);
    angle = cross_product * angle;

    // return ZERO if dot_product is zero.
    if (angle == 0.0) return Math.abs(angle);

    // return RIGHT if cross product is positive
    if (angle > 0) return angle;

    // return LEFT if cross product is negative
    if (angle < 0) return angle;

    // return ZERO if cross product is zero.
    return ZERO;
  }

  public static ArrayList<String> textDirections(ArrayList<Node> path) {

    ArrayList<String> pathDirections = new ArrayList<>();

    pathDirections.add("Start from " + path.get(0).longName + " to " + path.get(1).longName);

    for (int i = 0; i < path.size() - 2; i++) {
      double crossProd = directionOfPoint(path.get(i), path.get(i + 1), path.get(i + 2));
      System.out.println(crossProd);
      if (crossProd < 0) {
        pathDirections.add(
            "Turn left from " + path.get(i + 1).longName + " to " + path.get(i + 2).longName);
      } else if (crossProd > 0) {
        pathDirections.add(
            "Turn right from " + path.get(i + 1).longName + " to " + path.get(i + 2).longName);
      } else if (crossProd == 0.0) {
        pathDirections.add(
            "Continue Straight from "
                + path.get(i + 1).longName
                + " to "
                + path.get(i + 2).longName);
      }
    }

    // TODO check for duplicate "continue straights" and delete the intermediary ones

    pathDirections.add("You have reached your destination.");
    return pathDirections;
  }

  /**
   * Uses the principle of find the nearest neighbor each point to locate the next node it should
   * move to in order to generate a semi-optimized path. Usually won't be completely optimal, but it
   * should be an improvement.
   *
   * @param g an adjacency-matrix-representation of the graph where (x,y) is the weight of the edge
   *     or 0 if there is no edge.
   * @param startID the node to start from.
   * @param goalIDs the nodes we're searching for in no particular order.
   * @return modified to return the goalIDs in an ordered semi-optimal form.
   */
  public static ArrayList<String> nearestNeighbor(
      Graph g, String startID, ArrayList<String> goalIDs) {

    ArrayList<String> organized = new ArrayList<>();

    // Initialize list of goal indices
    ArrayList<String> goals = new ArrayList<>();
    goals = (ArrayList<String>) goalIDs.clone();

    String min;
    String start = startID;

    for (int i = 0; i < goalIDs.size(); i++) {
      min = DijkstrasAlgorithm.dijkstra(g, start, goals);
      organized.add(min);
      goals.remove(min);
      start = min;
    }
    return organized;
  }
}
