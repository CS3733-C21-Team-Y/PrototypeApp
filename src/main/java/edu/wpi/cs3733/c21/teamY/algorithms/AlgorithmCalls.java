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

  public static ArrayList<Node> aStar(Graph g, String startID, String goalID) {
    return AStarAlgorithm.aStar(g, startID, goalID);
  }

  public static ArrayList<Node> aStar(Graph g, String startID, ArrayList<String> goalIDs) {
    return AStarAlgorithm.aStar(g, startID, goalIDs);
  }

  public static ArrayList<String> nearestNeighbor(
      Graph g, String startID, ArrayList<String> goalIDs) {
    return AStarAlgorithm.nearestNeighbor(g, startID, goalIDs);
  }

  public static String minDistance(
      HashMap<String, Double> dist, HashMap<String, Boolean> inShortest) {
    return DijkstrasAlgorithm.minDistance(dist, inShortest);
  }

  public static HashMap<String, Double> dijkstra(
      Graph g, String startID, ArrayList<String> goalIDs) {
    return DijkstrasAlgorithm.dijkstra(g, startID, goalIDs);
  }

  public static String dijkstraDetour(
      Graph g, String startID, ArrayList<String> goalIDs, String detourType) {
    return DijkstrasAlgorithm.dijkstraDetour(g, startID, goalIDs, detourType);
  }
}