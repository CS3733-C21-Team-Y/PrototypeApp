package edu.wpi.cs3733.c21.teamY.pages;

import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import edu.wpi.cs3733.c21.teamY.entity.Employee;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UserProfilePageController extends SubPage {
  @FXML private Label idLabel;
  @FXML private Label firstnameLabel;
  @FXML private Label lastnameLabel;
  @FXML private Label accessLabel;
  @FXML private Label workspaceLabel;
  @FXML private Label emailLabel;

  String userID = "";

  @FXML
  private void initialize() {
    userID = Settings.getSettings().getCurrentUsername();
    Employee employee = DataOperations.locateEmployee(this.userID);
    idLabel.setText(employee.getEmployeeID());
    firstnameLabel.setText(employee.getFirstName());
    lastnameLabel.setText(employee.getLastName());
    String accessLavel = "";
    String guestWorkspace = "";
    int access = employee.getAccessLevel();
    String employeeWorkspace = employee.getPrimaryWorkspace();
    if (access == 0) {
      accessLavel = "guest";
      guestWorkspace = "N/A";
    } else if (access == 1) {
      accessLavel = "patient";
      guestWorkspace = "N/A";
    } else if (access == 2) {
      accessLavel = "staff";
      guestWorkspace = employeeWorkspace;
    } else {
      accessLavel = "administrator";
      guestWorkspace = employeeWorkspace;
    }
    accessLabel.setText(accessLavel);
    workspaceLabel.setText(guestWorkspace);
    emailLabel.setText(employee.getEmail());
  }
}
