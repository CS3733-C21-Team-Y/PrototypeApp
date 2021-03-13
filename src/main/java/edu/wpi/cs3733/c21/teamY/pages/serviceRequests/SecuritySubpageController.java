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

public class SecuritySubpageController extends GenericServiceFormPage {

  @FXML private JFXButton clearBtn;
  @FXML private JFXButton backBtn;
  @FXML private JFXButton submitBtn;
  @FXML private JFXComboBox category;
  @FXML private JFXComboBox locationBox;
  @FXML private JFXComboBox urgency;
  @FXML private JFXTimePicker time;
  @FXML private JFXDatePicker datePickerObject;
  @FXML private JFXTextArea description;
  @FXML private JFXComboBox employeeComboBox;

  AutoCompleteComboBoxListener<String> locationAuto;
  AutoCompleteComboBoxListener<String> urgencyAuto;
  AutoCompleteComboBoxListener<String> employeeAuto;
  AutoCompleteComboBoxListener<String> categoryAuto;

  @FXML private StackPane stackPane;

  Settings settings;

  public SecuritySubpageController() {}

  @FXML
  private void initialize() {
    settings = Settings.getSettings();
    backBtn.setOnAction(e -> buttonClicked(e));
    submitBtn.setOnAction(e -> submitBtnClicked());
    clearBtn.setOnAction(e -> clearButton());

    category.getItems().add("Guard");
    locationBox.getItems().add("Lobby");
    locationBox.getItems().add("Radiology");
    locationBox.getItems().add("Phlebotomy");
    urgency.getItems().add("Low");
    urgency.getItems().add("Medium");
    urgency.getItems().add("High");

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

    locationAuto = new AutoCompleteComboBoxListener<>(locationBox);
    urgencyAuto = new AutoCompleteComboBoxListener<>(urgency);
    employeeAuto = new AutoCompleteComboBoxListener<>(employeeComboBox);
    categoryAuto = new AutoCompleteComboBoxListener<>(category);
  }

  private void clearButton() {
    locationBox.setValue(null);
    datePickerObject.setValue(null);
    time.setValue(null);
    description.setText("");
    category.setValue(null);
    urgency.setValue(null);
    employeeComboBox.getSelectionModel().clearSelection();
  }

  @FXML
  private void submitBtnClicked() {

    clearIncomplete(employeeComboBox);
    clearIncomplete(description);
    clearIncomplete(locationBox);
    clearIncomplete(category);
    clearIncomplete(urgency);
    clearIncomplete(time);
    clearIncomplete(datePickerObject);

    if (locationBox.toString().equals("")
        || description.getText().equals("")
        || category.toString().equals("")
        || urgency.toString().equals("")
        || time.toString().equals("")
        || datePickerObject.getValue() == null
        || (Settings.getSettings().getCurrentPermissions() == 3
            && employeeComboBox.getValue() == null)) {
      if (description.getText().equals("")) {
        incomplete(description);
      }
      if (locationBox.getValue() == null) {
        incomplete(locationBox);
      }
      if (category.getValue() == null) {
        incomplete(category);
      }
      if (urgency.getValue() == null) {
        incomplete(urgency);
      }
      if (time.getValue() == null) {
        incomplete(time);
      }
      if (datePickerObject.getValue() == null) {
        incomplete(datePickerObject);
      }
      if (employeeComboBox.getValue() == null) {
        incomplete(employeeComboBox);
      }
      nonCompleteForm(stackPane);
    } else {
      Service service = new Service( "Security");
      service.setCategory((String) category.getValue());
      service.setLocation((String) locationBox.getValue());
      service.setUrgency((String) urgency.getValue());
      service.setAdditionalInfo("Time: " + time.getValue().toString());
      service.setDate(datePickerObject.getValue().toString());
      service.setDescription(description.getText());
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

  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == backBtn) parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
  }
}
