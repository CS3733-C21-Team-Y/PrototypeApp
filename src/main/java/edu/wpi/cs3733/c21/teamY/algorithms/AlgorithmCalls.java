package edu.wpi.cs3733.c21.teamY.algorithms;

import edu.wpi.cs3733.c21.teamY.entity.Graph;
import edu.wpi.cs3733.c21.teamY.entity.Node;
import java.util.ArrayList;
import java.util.HashMap;

public class AlgorithmCalls {

  public AlgorithmCalls() {}

  public static double nodeDistance(Node start, Node end) {
    return DijkstrasAlgorithm.nodeDistance(start, end);
  }

  public static ArrayList<Node> aStar(Graph g, String startID, String goalID, String accessType) {
    return AStarAlgorithm.aStar(g, startID, goalID, accessType);
  }

  public static ArrayList<Node> aStar(
      Graph g, String startID, ArrayList<String> goalIDs, String accessType) {
    return AStarAlgorithm.aStar(g, startID, goalIDs, accessType);
  }

  public static ArrayList<String> nearestNeighbor(
      Graph g, String startID, ArrayList<String> goalIDs) {
    return AStarAlgorithm.nearestNeighbor(g, startID, goalIDs);
  }

  public static String minDistance(
      HashMap<String, Double> dist, HashMap<String, Boolean> inShortest) {
    return DijkstrasAlgorithm.minDistance(dist, inShortest);
  }

  public static String dijkstra(Graph g, String startID, ArrayList<String> goalIDs) {
    return DijkstrasAlgorithm.dijkstra(g, startID, goalIDs);
  }

  public static ArrayList<String> dijkstraDetour(
      Graph g, ArrayList<Node> path, ArrayList<String> endLocations, String detourType) {
    return DijkstrasAlgorithm.dijkstraDetour(g, path, endLocations, detourType);
  }

  public static double directionOfPoint(Node node1, Node node2, Node P) {
    return AStarAlgorithm.directionOfPoint(node1, node2, P);
  }

  public static ArrayList<String> textDirections(ArrayList<Node> path) {
    return AStarAlgorithm.textDirections(path);
  }
}
