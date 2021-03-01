package edu.wpi.cs3733.c21.teamY.entity;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TableNodes extends RecursiveTreeObject<TableNodes> {
  public StringProperty nodeID;
  public StringProperty nodeType;
  public StringProperty xcoord;
  public StringProperty ycoord;
  public StringProperty
      floor; // I made floor and room strings for now but they will likely need special attention
  // later
  public StringProperty building;
  public StringProperty room;
  public StringProperty longName;
  public StringProperty shortName;
  //    public CharacterP teamAssigned;

  public TableNodes(Node node) {
    this.nodeID = new SimpleStringProperty(node.getNodeID());
    this.nodeType = new SimpleStringProperty(node.getNodeType());
    this.xcoord = new SimpleStringProperty(node.getXcoord() + "");
    this.ycoord = new SimpleStringProperty(node.getYcoord() + "");
    this.floor = new SimpleStringProperty(node.getFloor());
    this.building = new SimpleStringProperty(node.getBuilding());
    this.room = new SimpleStringProperty(node.getRoom());
    this.longName = new SimpleStringProperty(node.getLongName());
    this.shortName = new SimpleStringProperty(node.getShortName());
    try {
      //            this.teamAssigned = new UCharacterProperty(node.getTeamAssigned());
    } catch (Exception e) {
    }
  }

  public StringProperty getNodeID() {
    return this.nodeID;
  }

  public StringProperty getNodeType() {
    return this.nodeType;
  }

  public StringProperty getXcoord() {
    return this.xcoord;
  }

  public StringProperty getYcoord() {
    return this.ycoord;
  }

  public StringProperty getFloor() {
    return this.floor;
  }

  public StringProperty getBuilding() {
    return this.building;
  }

  public StringProperty getRoom() {
    return this.room;
  }

  public StringProperty getLongName() {
    return this.longName;
  }

  public StringProperty getShortName() {
    return this.shortName;
  }
}
