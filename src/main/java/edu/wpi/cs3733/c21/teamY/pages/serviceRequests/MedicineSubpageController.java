package edu.wpi.cs3733.c21.teamY.pages.serviceRequests;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.c21.teamY.dataops.AutoCompleteComboBoxListener;
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

public class MedicineSubpageController extends GenericServiceFormPage {
  // connects the scenebuilder button to a code button
  // add buttons to other scenes here
  @FXML private JFXButton clearBtn;
  @FXML private JFXButton backBtn;
  @FXML private JFXButton submitBtn;
  @FXML private JFXTextField medicine;
  @FXML private JFXTextField patient;
  @FXML private JFXDatePicker date;
  @FXML private JFXTextField doctor;
  @FXML private StackPane stackPane;
  @FXML private JFXComboBox employeeComboBox;
  AutoCompleteComboBoxListener<String> employeeAuto;

  private Settings settings;

  // unused constructor
  public MedicineSubpageController() {}

  // this runs once the FXML loads in to attach functions to components
  @FXML
  private void initialize() {

    settings = Settings.getSettings();

    // attaches a handler to the button with a lambda expression
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

    employeeAuto = new AutoCompleteComboBoxListener<>(employeeComboBox);
  }

  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == backBtn) parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
  }

  private void clearButton() {
    patient.setText("");
    date.setValue(null);
    doctor.setText("");
    medicine.setText("");
  }

  @FXML
  private void submitBtnClicked() {
    // put code for submitting a service request here

    clearIncomplete(patient);
    clearIncomplete(date);
    clearIncomplete(doctor);
    clearIncomplete(medicine);
    clearIncomplete(employeeComboBox);

    if (patient.getText().equals("")
        || date.getValue().equals(null)
        || doctor.getText().equals("")
        || medicine.getText().equals("")) {
      if (patient.getText().equals("")
          || (Settings.getSettings().getCurrentPermissions() == 3
              && employeeComboBox.getValue() == null)) {
        incomplete(patient);
      }
      if (date.getValue().equals(null)) {
        incomplete(date);
      }
      if (doctor.getText().equals("")) {
        incomplete(doctor);
      }
      if (medicine.getText().equals("")) {
        incomplete(medicine);
      }
      if (employeeComboBox.getValue() == null) {
        incomplete(employeeComboBox);
      }
      nonCompleteForm(stackPane);
    } else {

      Service service = new Service(DataOperations.generateUniqueID("MED"), "Medicine");
      service.setCategory(medicine.getText());
      service.setDescription(patient.getText());
      service.setDate(date.getValue().toString());
      service.setAdditionalInfo(doctor.getText());
      service.setRequester(settings.getCurrentUsername());
      if (settings.getCurrentPermissions() == 3) {
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
      parent.loadCenterSubPage("ServiceRequestNavigator.fxml");
      clearButton();
    }
  }
}
