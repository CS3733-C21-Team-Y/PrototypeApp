package edu.wpi.cs3733.c21.teamY.pages.serviceRequests;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import edu.wpi.cs3733.c21.teamY.entity.Employee;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import edu.wpi.cs3733.c21.teamY.pages.GenericServiceFormPage;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class OutsideHospitalSubpageController extends GenericServiceFormPage {

  @FXML private JFXButton clearBtn;
  @FXML private JFXButton backBtn;
  @FXML private JFXButton submitBtn;
  @FXML private JFXTextArea descriptionTextArea;
  @FXML private JFXTextField locationTextField;
  @FXML private JFXDatePicker serviceDate;
  @FXML private JFXComboBox employeeComboBox;

  @FXML private StackPane stackPane;

  private Settings settings;

  public OutsideHospitalSubpageController() {}

  // this runs once the FXML loads in to attach functions to components
  @FXML
  private void initialize() {
    settings = Settings.getSettings();
    backBtn.setOnAction(e -> buttonClicked(e));
    submitBtn.setOnAction(e -> submitBtnClicked());
    clearBtn.setOnAction(e -> clearButton());

    if (settings.getCurrentPermissions() == 3) {
      employeeComboBox.setVisible(true);
      try {
        ArrayList<Employee> employeeList = DataOperations.getStaffList();
        for (Employee employee : employeeList) {
          employeeComboBox.getItems().add(employee.getEmployeeID());
        }
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }
    } else {
      employeeComboBox.setVisible(false);
    }
  }

  @FXML
  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == backBtn) parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
  }

  private void clearButton() {
    locationTextField.setText("");
    serviceDate.setValue(null);
    descriptionTextArea.setText("");
    employeeComboBox.getSelectionModel().clearSelection();
  }

  @FXML
  private void submitBtnClicked() {

    clearIncomplete(locationTextField);
    clearIncomplete(descriptionTextArea);
    clearIncomplete(serviceDate);
    clearIncomplete(employeeComboBox);

    if (locationTextField.getText().equals("")
        || descriptionTextArea.getText().equals("")
        || serviceDate.getValue() == null
        || (Settings.getSettings().getCurrentPermissions() == 3
            && employeeComboBox.getValue() == null)) {
      if (locationTextField.getText().equals("")) {
        incomplete(locationTextField);
      }
      if (descriptionTextArea.getText().equals("")) {
        incomplete(descriptionTextArea);
      }
      if (serviceDate.getValue() == null) {
        incomplete(serviceDate);
      }
      if (employeeComboBox.getValue() == null) {
        incomplete(employeeComboBox);
      }
      nonCompleteForm(stackPane);
    } else {
      Service service = new Service(this.IDCount, "Outside Hospital");
      this.IDCount++;
      service.setLocation(locationTextField.getText());
      service.setDate(serviceDate.getValue().toString());
      service.setDescription(descriptionTextArea.getText());
      service.setRequester(settings.getCurrentUsername());
      if (settings.getCurrentPermissions() == 3) {
        service.setEmployee((String) employeeComboBox.getValue());
      } else {
        service.setEmployee("admin");
      }

      try {
        DataOperations.saveService(service);
      } catch (IllegalAccessException | SQLException e) {
        e.printStackTrace();
      }

      submittedPopUp(stackPane);
      parent.loadCenterSubPage("ServiceRequestNavigator.fxml");
      clearButton();
    }
  }
}
