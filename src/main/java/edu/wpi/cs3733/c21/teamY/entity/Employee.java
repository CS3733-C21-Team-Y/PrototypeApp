package edu.wpi.cs3733.c21.teamY.entity;

public class Employee {

  private String firstName;
  private String lastName;
  private String employeeID;
  private int accessLevel;
  private String primaryWorkspace;

  public Employee(
      String firstName,
      String lastName,
      String employeeID,
      int accessLevel,
      String primaryWorkspace) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.employeeID = employeeID;
    this.accessLevel = accessLevel;
    this.primaryWorkspace = primaryWorkspace;
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
}
