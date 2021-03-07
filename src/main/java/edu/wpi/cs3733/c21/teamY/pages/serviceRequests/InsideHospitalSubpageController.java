package edu.wpi.cs3733.c21.teamY.pages.serviceRequests;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
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

public class InsideHospitalSubpageController extends GenericServiceFormPage {

  @FXML private JFXButton clearBtn;
  @FXML private JFXButton backBtn;
  @FXML private JFXButton submitBtn;
  @FXML private JFXTextField patientName;
  @FXML private JFXTextArea description;
  @FXML private JFXTextField date;
  @FXML private JFXTextField currentLocation;
  @FXML private JFXTextField desiredLocation;
  @FXML private JFXComboBox employeeComboBox;

  @FXML private StackPane stackPane;

  public InsideHospitalSubpageController() {}

  private Settings settings;

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

  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == backBtn) parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
  }

  private void clearButton() {
    description.setText("");
    currentLocation.setText("");
    patientName.setText("");
    date.setText("");
    desiredLocation.setText("");
    employeeComboBox.getSelectionModel().clearSelection();
  }

  @FXML
  private void submitBtnClicked() {
    // put code for submitting a service request here

    if (description.getText().equals("")
        || currentLocation.getText().equals("")
        || patientName.getText().equals("")
        || date.getText().equals("")) {
      nonCompleteForm(stackPane);
    } else {

      Service service = new Service(this.IDCount, "Inside Transport");
      this.IDCount++;
      // service.setCategory((String) patientName.getText());
      // service.setLocation((String) patientName.getValue());
      service.setDescription(description.getText());
      service.setRequester(settings.getCurrentUsername());
      service.setLocation(currentLocation.getText());
      service.setCategory(patientName.getText());
      service.setDate(date.getText());
      service.setAdditionalInfo(desiredLocation.getText());
      service.setRequester(settings.getCurrentUsername());
      if(settings.getCurrentPermissions() == 3) {
        service.setEmployee((String) employeeComboBox.getValue());
      } else {
        service.setEmployee("admin");
      }

      try {
        DataOperations.saveService(service);
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }

      submittedPopUp(stackPane);
      clearButton();
    }
  }
}
