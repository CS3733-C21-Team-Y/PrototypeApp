package edu.wpi.yellowyetis;

import java.util.ArrayList;

public class Node {
  private ArrayList<Node> neighbors;

  String nodeID;
  String nodeType;
  double xcoord;
  double ycoord;
  String floor; // I made floor and room strings for now but they will likely need special attention
  // later
  String building;
  String room;
  String longName;
  String shortName;
  char teamAssigned;

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

  public String getNodeType() {
    return nodeType;
  }

  public double getXcoord() {
    return xcoord;
  }

  public double getYcoord() {
    return ycoord;
  }

  public String getFloor() {
    return floor;
  }

  public String getRoom() {
    return room;
  }

  public String getBuilding() {
    return building;
  }

  public String getLongName() {
    return longName;
  }

  public String getShortName() {
    return shortName;
  }

  public char getTeamAssigned() {
    return teamAssigned;
  }

  public String getNodeID() {
    return nodeID;
  }

  @Override
  public String toString() {
    return "\n"
        + nodeID
        + ","
        + xcoord
        + ","
        + ycoord
        + ","
        + floor
        + ","
        + building
        + ","
        + nodeType
        + ","
        + longName
        + ","
        + shortName
        + ","
        + teamAssigned;
  }
}