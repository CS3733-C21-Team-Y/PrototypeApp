package edu.wpi.cs3733.c21.teamY.entity;

public class Employee {

  private String firstName;
  private String lastName;
  private String employeeID;
  private String password;
  private String email;
  private int accessLevel;
  private String primaryWorkspace;
  private String salt;
  private boolean cleared;

  // all feilds including salt
  public Employee(
      String firstName,
      String lastName,
      String employeeID,
      String password,
      String email,
      int accessLevel,
      String primaryWorkspace,
      String salt) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.employeeID = employeeID;
    this.password = password;
    this.email = email;
    this.accessLevel = accessLevel;
    this.primaryWorkspace = primaryWorkspace;
    this.salt = salt;
  }

  // for account creation
  public Employee(
      String firstName,
      String lastName,
      String employeeID,
      String password,
      String email,
      String salt) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.employeeID = employeeID;
    this.password = password;
    this.email = email;
    this.salt = salt;
  }

  public Employee(TableEmployee tb) {
    this.firstName = tb.getFirstName().getValue();
    this.lastName = tb.getLastName().getValue();
    this.employeeID = tb.getEmployeeID().getValue();
    this.password = tb.getPassword().getValue();
    this.email = tb.getEmail().getValue();
    this.accessLevel = Integer.parseInt(tb.getAccessLevel().getValue());
    this.primaryWorkspace = tb.getPrimaryWorkspace().getValue();
    this.salt = tb.getSalt().getValue();
    this.cleared = Boolean.parseBoolean(tb.cleared.getValue());
  }

  // Employee constructor for use in the view of cleared employees
  public Employee(String firstName, String lastName, String employeeID, boolean cleared) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.employeeID = employeeID;
    this.cleared = cleared;
  }

  public boolean getClearance() {
    return cleared;
  }

  public String getPassword() {
    return password;
  }

  public String getSalt() {
    return salt;
  }

  public String getEmail() {
    return email;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getEmployeeID() {
    return employeeID;
  }

  public int getAccessLevel() {
    return accessLevel;
  }

  public String getPrimaryWorkspace() {
    return primaryWorkspace;
  }

  @Override
  public String toString() {
    return "Employee{"
        + "firstName='"
        + firstName
        + '\''
        + ", lastName='"
        + lastName
        + '\''
        + ", employeeID='"
        + employeeID
        + '\''
        + ", password='"
        + password
        + '\''
        + ", email='"
        + email
        + '\''
        + ", accessLevel="
        + accessLevel
        + ", primaryWorkspace='"
        + primaryWorkspace
        + '\''
        + '}';
  }
}
