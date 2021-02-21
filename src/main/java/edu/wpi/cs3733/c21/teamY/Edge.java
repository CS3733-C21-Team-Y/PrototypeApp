package edu.wpi.cs3733.c21.teamY;

public class Edge {
  public Edge(String edgeID, String startNodeID, String endNodeID) {
    this.edgeID = edgeID;
    this.startNodeID = startNodeID;
    this.endNodeID = endNodeID;
  }

  String edgeID;
  String startNodeID;
  String endNodeID;

  public String getEdgeID() {
    return edgeID;
  }

  public String getStartNodeID() {
    return startNodeID;
  }

  public String getEndNodeID() {
    return endNodeID;
  }

  @Override
  public String toString() {
    return edgeID + "," + startNodeID + ',' + endNodeID;
  }

  /*public String toString(String flag) {

    return "'" + edgeID + "'" + "," + "'" + startNodeID + "'" + ',' + "'" + endNodeID + "'";
  }*/
}
