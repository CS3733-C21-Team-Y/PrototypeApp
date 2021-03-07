package edu.wpi.cs3733.c21.teamY.entity;

import java.util.Date;

public class ParkingLot {
  String nodeID;
  String userName;
  Date enterParkingLot;
  Date leaveParkingLot;

  public ParkingLot() {}

  public ParkingLot(String nodeID, String userName) {
    this.nodeID = nodeID;
    this.userName = userName;
    this.enterParkingLot = null;
    this.leaveParkingLot = null;
  }

  public String getNodeID() {
    return nodeID;
  }

  public String getUserName() {
    return userName;
  }

  public Date getEnterParkingLot() {
    return enterParkingLot;
  }

  public Date getLeaveParkingLot() {
    return leaveParkingLot;
  }

  public void setNodeID(String nodeID) {
    this.nodeID = nodeID;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public void setEnterParkingLot(Date enterParkingLot) {
    this.enterParkingLot = enterParkingLot;
  }

  public void setLeaveParkingLot(Date leaveParkingLot) {
    this.leaveParkingLot = leaveParkingLot;
  }
}
