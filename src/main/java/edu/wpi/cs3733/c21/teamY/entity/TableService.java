package edu.wpi.cs3733.c21.teamY.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TableService {
  private StringProperty serviceID;
  private StringProperty type;
  private StringProperty description;
  private StringProperty location;
  private StringProperty category;
  private StringProperty urgency;
  private StringProperty date;
  private StringProperty additionalInfo;
  private StringProperty requester;
  private StringProperty status;
  private StringProperty employee;

  public TableService(Service service) {
    this.serviceID = new SimpleStringProperty(String.valueOf(service.getServiceID()));
    this.type = new SimpleStringProperty(service.getType());
    this.description = new SimpleStringProperty(service.getDescription());
    this.location = new SimpleStringProperty(service.getLocation());
    this.category = new SimpleStringProperty(service.getCategory());
    this.urgency = new SimpleStringProperty(service.getUrgency());
    this.date = new SimpleStringProperty(service.getDate());
    this.additionalInfo = new SimpleStringProperty(service.getAdditionalInfo());
    this.requester = new SimpleStringProperty(service.getRequester());
    this.status = new SimpleStringProperty(String.valueOf(service.getStatus()));
    this.employee = new SimpleStringProperty(service.getEmployee());
  }

  public String getServiceID() {
    return serviceID.get();
  }

  public StringProperty serviceIDProperty() {
    return serviceID;
  }

  public String getType() {
    return type.get();
  }

  public StringProperty typeProperty() {
    return type;
  }

  public String getDescription() {
    return description.get();
  }

  public StringProperty descriptionProperty() {
    return description;
  }

  public String getLocation() {
    return location.get();
  }

  public StringProperty locationProperty() {
    return location;
  }

  public String getCategory() {
    return category.get();
  }

  public StringProperty categoryProperty() {
    return category;
  }

  public String getUrgency() {
    return urgency.get();
  }

  public StringProperty urgencyProperty() {
    return urgency;
  }

  public String getDate() {
    return date.get();
  }

  public StringProperty dateProperty() {
    return date;
  }

  public String getAdditionalInfo() {
    return additionalInfo.get();
  }

  public StringProperty additionalInfoProperty() {
    return additionalInfo;
  }

  public String getRequester() {
    return requester.get();
  }

  public StringProperty requesterProperty() {
    return requester;
  }

  public String getStatus() {
    return status.get();
  }

  public StringProperty statusProperty() {
    return status;
  }

  public String getEmployee() {
    return employee.get();
  }

  public StringProperty employeeProperty() {
    return employee;
  }
}
