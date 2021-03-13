package edu.wpi.cs3733.c21.teamY.entity;

public class Service {

  private int serviceID;
  private String type;

  private String description;
  private String location;
  private String category;
  private String urgency;
  private String date;
  private String additionalInfo;
  private String requester;
  private int status;

  private String employee;

  public Service(String type){
    this.type=type;
  }

  // no requester
  public Service(
      int serviceID,
      String type,
      String description,
      String location,
      String category,
      String urgency,
      String date,
      String additionalInfo,
      int status) {
    this.serviceID = serviceID;
    this.type = type;
    this.description = description;
    this.location = location;
    this.category = category;
    this.urgency = urgency;
    this.date = date;
    this.additionalInfo = additionalInfo;
    this.status = status;
    this.employee = "admin";
  }

  // requester
  public Service(
      int serviceID,
      String type,
      String description,
      String location,
      String category,
      String urgency,
      String date,
      String additionalInfo,
      String requester,
      int status) {
    this.serviceID = serviceID;
    this.type = type;
    this.description = description;
    this.location = location;
    this.category = category;
    this.urgency = urgency;
    this.date = date;
    this.additionalInfo = additionalInfo;
    this.requester = requester;
    this.status = status;
    this.employee = "admin";
  }

  public Service(int serviceID, String type) {
    this.serviceID = serviceID;
    this.type = type;
    description = "";
    location = "";
    category = "";
    urgency = "";
    date = "";
    status = -1;
    this.employee = "admin";
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
    this.employee = "admin";
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
    this.employee = "admin";
  }

  public Service(
      int serviceID,
      String type,
      String description,
      String location,
      String category,
      String urgency,
      String date,
      String additionalInfo,
      String requester,
      int status,
      String employee) {
    this.serviceID = serviceID;
    this.type = type;
    this.description = description;
    this.location = location;
    this.category = category;
    this.urgency = urgency;
    this.date = date;
    this.additionalInfo = additionalInfo;
    this.requester = requester;
    this.status = status;
    this.employee = employee;
  }

  public String getEmployee() {
    return employee;
  }

  public void setEmployee(String employee) {
    this.employee = employee;
  }

  public String getRequester() {
    return requester;
  }

  public String getAdditionalInfo() {
    return additionalInfo;
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

  public void setAdditionalInfo(String additionalInfo) {
    this.additionalInfo = additionalInfo;
  }

  public void setRequester(String requester) {
    this.requester = requester;
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
