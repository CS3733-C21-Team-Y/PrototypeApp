package edu.wpi.cs3733.c21.teamY.entity;

import java.util.ArrayList;
import java.util.HashMap;

public class Graph {
  public HashMap<String, Integer> allNodes; // NodeID int index
  public HashMap<String, Node> longNodes;
  public Node[] nodeList;
  public double[][] adjMatrix;
  public double[][] heuristicMatrix;

  private int vertices;

  public Graph(ArrayList<Node> nodes, ArrayList<Edge> edges) {
    this.vertices = nodes.size();
    allNodes = new HashMap<>(vertices * 5);
    longNodes = new HashMap<>(vertices * 5);

    nodeList = new Node[vertices];
    adjMatrix = new double[vertices][vertices];
    heuristicMatrix = new double[vertices][vertices];

    try {
      // Populate hashmaps with nodes
      int i = 0;
      for (Node node : nodes) {
        allNodes.put(node.nodeID, i);
        longNodes.put(node.longName, node);
        nodeList[i] = node;
        i++;
      }

      // Fill in edges
      i = 0;
      for (Edge edge : edges) {
        int startID = indexFromID(edge.startNodeID);
        int endID = indexFromID(edge.endNodeID);

        Node start = nodeList[startID];
        Node end = nodeList[endID];

        // I'll leave this in here for now
        start.addEdge(end);
        end.addEdge(start);

        double dist =
            Math.sqrt(
                Math.pow((end.xcoord - start.xcoord), 2)
                    + Math.pow((end.ycoord - start.ycoord), 2));
        adjMatrix[startID][endID] = dist;
        adjMatrix[endID][startID] = dist;
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Node nodeFromID(String id) {
    return nodeList[indexFromID(id)];
  }

  public int indexFromID(String id) {
    return allNodes.get(id);
  }

  public Node nodeFromLongName(String id) {
    return longNodes.get(id);
  }
}
