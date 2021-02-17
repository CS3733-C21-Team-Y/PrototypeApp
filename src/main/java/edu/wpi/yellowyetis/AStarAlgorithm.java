package edu.wpi.yellowyetis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Used to perform the A-Star (A*) Algorithm to find the shortest path from a start to a target
 * node.
 */
public class AStarAlgorithm {

  public static double nodeDistance(Node start, Node end) {
    return Math.sqrt(
        Math.pow((end.xcoord - start.xcoord), 2) + Math.pow((end.ycoord - start.ycoord), 2));
  }

  /**
   * Finds the shortest distance between two nodes using the A-star algorithm
   *
   * @param g an adjacency-matrix-representation of the graph where (x,y) is the weight of the edge
   *     or 0 if there is no edge.
   * @param startID the node to start from.
   * @param goalID the node we're searching for.
   * @return modified to return the path.
   */
  public static ArrayList<Node> aStar(Graph g, String startID, String goalID) {

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
    priorities[start] = nodeDistance(g.nodeList[start], g.nodeList[goal]);

    // This contains whether a node was already visited
    boolean[] visited = new boolean[graph.length];

    // While there are nodes left to visit...
    while (true) {

      // ... find the node with the currently lowest priority...
      double lowestPriority = Integer.MAX_VALUE;
      int lowestPriorityIndex = -1;
      for (int i = 0; i < priorities.length; i++) {
        // ... by going through all nodes that haven't been visited yet
        if (priorities[i] < lowestPriority && !visited[i]) {
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
        return path; // distances[lowestPriorityIndex];
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
            priorities[i] = distances[i] + nodeDistance(g.nodeList[i], g.nodeList[goal]);

            cameFrom.put(i, lowestPriorityIndex);
            System.out.println(
                "Updating distance of node "
                    + g.nodeList[i].nodeID
                    + " to "
                    + distances[i]
                    + " and priority to "
                    + priorities[i]);
          }
        }
      }

      // Lastly, note that we are finished with this node.
      visited[lowestPriorityIndex] = true;
      // System.out.println("Visited nodes: " + Arrays.toString(visited));
      // System.out.println("Currently lowest distances: " + Arrays.toString(distances));

    }
  }
}