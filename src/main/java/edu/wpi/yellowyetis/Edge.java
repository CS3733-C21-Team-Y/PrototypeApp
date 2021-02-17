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
}
