package edu.wpi.yellowyetis;

import java.util.ArrayList;

public class Node {
  private ArrayList<Node> neighbors;

  public String nodeType;
  public double xcoord;
  public double ycoord;
  public String
      floor; // I made floor and room strings for now but they will likely need special attention
  // later
  public String room;
  public String building;
  public String longName;
  public String shortName;
  public char teamAssigned;
  public String nodeID;

  public Node(
      String nodeType,
      double xcoord,
      double ycoord,
      String floor,
      String building,
      String longName,
      String shortName,
      char teamAssigned,
      String nodeID) {
    this.nodeType = nodeType;
    this.xcoord = xcoord;
    this.ycoord = ycoord;
    this.floor = floor;
    this.building = building;
    this.longName = longName;
    this.shortName = shortName;
    this.teamAssigned = teamAssigned;
    this.nodeID = nodeID;

    this.neighbors = new ArrayList<Node>();
    // this.nodeID = ("" + teamAssigned + nodeType + room + floor).toUpperCase(Locale.ROOT);
  }

  public boolean addEdge(Node node) {
    return neighbors.add(node);
  }

  public boolean removeEdge(Node node) {
    return neighbors.remove(node);
  }

  public ArrayList<Node> getNeighbors() {
    return neighbors;
  }
}
