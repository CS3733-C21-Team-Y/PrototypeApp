package edu.wpi.cs3733.c21.teamY.entity;

import java.util.ArrayList;

public class Node {
  private ArrayList<Node> neighbors;

  // DO NOT PUT ANY FIELDS BEFORE THESE OR CHANGE THEIR ORDER PLEASE!!
  public String nodeID;
  public String nodeType;
  public double xcoord;
  public double ycoord;
  public String
      floor; // I made floor and room strings for now but they will likely need special attention
  // later
  public String building;
  public String room;
  public String longName;
  public String shortName;
  public char teamAssigned;

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
    this.room = "";

    this.neighbors = new ArrayList<Node>();
    // this.nodeID = ("" + teamAssigned + nodeType + room + floor).toUpperCase(Locale.ROOT);
  }

  public Node(double xcoord, double ycoord, String floor, String nodeID) {
    this.nodeType = "UG";
    this.xcoord = xcoord;
    this.ycoord = ycoord;
    this.floor = floor;
    this.building = "Faulkner";
    this.longName = "UserLongName " + nodeID;
    this.shortName = "usrShrtNme";
    this.teamAssigned = 'y';
    this.nodeID = nodeID;

    this.neighbors = new ArrayList<Node>();
    // this.nodeID = ("" + teamAssigned + nodeType + room + floor).toUpperCase(Locale.ROOT);
  }

  public Node(TableNodes tb) {
    this.nodeType = tb.nodeType.getValue();
    this.xcoord = Double.parseDouble(tb.xcoord.getValue());
    this.ycoord = Double.parseDouble(tb.ycoord.getValue());
    this.floor = tb.floor.getValue();
    this.building = tb.building.getValue();
    this.longName = tb.longName.getValue();
    this.shortName = tb.shortName.getValue();
    this.teamAssigned = 'y';
    this.nodeID = tb.getNodeID().getValue();
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
    return nodeID
        + ","
        + (int) xcoord
        + ","
        + (int) ycoord
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
