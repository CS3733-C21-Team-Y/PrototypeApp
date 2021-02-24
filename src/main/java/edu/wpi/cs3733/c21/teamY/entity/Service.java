package edu.wpi.cs3733.c21.teamY.entity;

public class Service {

  private int serviceID;
  private String type;

  private String description;
  private String location;
  private String category;
  private String urgency;
  private String date;

  private int status;

  public Service(int serviceID, String type) {
    this.serviceID = serviceID;
    this.type = type;
    description = "";
    location = "";
    category = "";
    urgency = "";
    date = "";
    status = -1;
  }

  public Service(
      int serviceID,
      String type,
      String description,
      String location,
      String category,
      String urgency,
      String date) {
    this.serviceID = serviceID;
    this.type = type;
    this.description = description;
    this.location = location;
    this.category = category;
    this.urgency = urgency;
    this.date = date;
    this.status = -1;
  }

  public Service(
      int serviceID,
      String type,
      String description,
      String location,
      String category,
      String urgency,
      String date,
      int status) {
    this.serviceID = serviceID;
    this.type = type;
    this.description = description;
    this.location = location;
    this.category = category;
    this.urgency = urgency;
    this.date = date;
    this.status = status;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
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
    status = -1;
  }

  @Override
  public String toString() {
    return serviceID
        + ","
        + type
        + ","
        + description
        + ","
        + location
        + ","
        + category
        + ","
        + urgency
        + ","
        + date
        + ","
        + status;
  }
}
