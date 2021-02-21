package edu.wpi.cs3733.c21.teamY;

public class Service {
  private String type;

  private String description;
  private String location;
  private String category;
  private String urgency;
  private String date;

  public Service(String type) {
    this.type = type;
    description = "";
    location = "";
    category = "";
    urgency = "";
    date = "";
  }

  public Service(
      String type,
      String description,
      String location,
      String category,
      String urgency,
      String date) {
    this.type = type;
    this.description = description;
    this.location = location;
    this.category = category;
    this.urgency = urgency;
    this.date = date;
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
