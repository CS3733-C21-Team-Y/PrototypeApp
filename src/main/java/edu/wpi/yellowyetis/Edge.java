package edu.wpi.yellowyetis;

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
    return "\n" + edgeID + "," + startNodeID + ',' + endNodeID;
  }
}