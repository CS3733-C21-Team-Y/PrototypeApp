package edu.wpi.cs3733.c21.teamY.entity;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TableEmployee extends RecursiveTreeObject<TableEmployee> {
  public StringProperty firstName;
  public StringProperty lastName;
  public StringProperty employeeID;
  public StringProperty password;
  public StringProperty email;
  public StringProperty accessLevel;
  public StringProperty primaryWorkspace;
  public StringProperty salt;
  public StringProperty cleared;

  public TableEmployee(Employee employee) {
    this.firstName = new SimpleStringProperty(employee.getFirstName());
    this.lastName = new SimpleStringProperty(employee.getLastName());
    this.employeeID = new SimpleStringProperty(employee.getEmployeeID());
    this.password = new SimpleStringProperty(employee.getPassword());
    this.email = new SimpleStringProperty(employee.getEmail());
    this.accessLevel = new SimpleStringProperty(employee.getAccessLevel() + "");
    this.primaryWorkspace = new SimpleStringProperty(employee.getPrimaryWorkspace());
    this.salt = new SimpleStringProperty(employee.getSalt());
    this.cleared = new SimpleStringProperty(employee.getClearance() + "");
  }

  public StringProperty getFirstName() {
    return this.firstName;
  }

  public StringProperty getLastName() {
    return this.lastName;
  }

  public StringProperty getEmployeeID() {
    return this.employeeID;
  }

  public StringProperty getPassword() {
    return this.password;
  }

  public StringProperty getEmail() {
    return this.email;
  }

  public StringProperty getAccessLevel() {
    return this.accessLevel;
  }

  public StringProperty getPrimaryWorkspace() {
    return this.primaryWorkspace;
  }

  public StringProperty getSalt() {
    return this.salt;
  }

  public StringProperty getCleared() {
    return this.cleared;
  }
}
