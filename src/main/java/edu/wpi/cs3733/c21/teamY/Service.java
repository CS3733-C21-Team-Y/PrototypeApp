package edu.wpi.cs3733.c21.teamY;

public class Service {
  private int serviceID;
  private String type;

  private String description;
  private String location;
  private String category;
  private String urgency;
  private String date;
  private int status;//-1 not started yet, 0 not completed, 1 completed

  public Service(int serviceID,String type) {
    this.serviceID=serviceID;
    this.type = type;
    this.description = "";
    this.location = "";
    this.category = "";
    this.urgency = "";
    this.date = "";
    this.status=-1;
  }

  public Service(
          int serviceID,
      String type,
      String description,
      String location,
      String category,
      String urgency,
      String date) {
    this.serviceID=serviceID;
    this.type = type;
    this.description = description;
    this.location = location;
    this.category = category;
    this.urgency = urgency;
    this.date = date;
    this.status=-1;
  }
  public Service(
          int serviceID,
          String type,
          String description,
          String location,
          String category,
          String urgency,
          String date, int status) {
    this.serviceID=serviceID;
    this.type = type;
    this.description = description;
    this.location = location;
    this.category = category;
    this.urgency = urgency;
    this.date = date;
    this.status=status;
  }

  public int getServiceID() {
    return serviceID;
  }

  public void setServiceID(int serviceID) {
    this.serviceID = serviceID;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getUrgency() {
    return urgency;
  }

  public void setUrgency(String urgency) {
    this.urgency = urgency;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }
}
