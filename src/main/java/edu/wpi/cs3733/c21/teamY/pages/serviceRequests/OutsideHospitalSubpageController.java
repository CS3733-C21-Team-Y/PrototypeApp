package edu.wpi.cs3733.c21.teamY.pages.serviceRequests;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.c21.teamY.dataops.AutoCompleteComboBoxListener;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.FuzzySearchComboBoxListener;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import edu.wpi.cs3733.c21.teamY.entity.Employee;
import edu.wpi.cs3733.c21.teamY.entity.Node;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import edu.wpi.cs3733.c21.teamY.pages.GenericServiceFormPage;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class OutsideHospitalSubpageController extends GenericServiceFormPage {

  @FXML private JFXButton clearBtn;
  @FXML private JFXButton backBtn;
  @FXML private JFXButton submitBtn;
  @FXML private JFXTextField patientName;
  @FXML private JFXTextArea description;
  @FXML private JFXDatePicker date;
  @FXML private JFXComboBox locationComboBox;
  @FXML private JFXTextField desiredLocation;
  @FXML private JFXComboBox employeeComboBox;
  AutoCompleteComboBoxListener<String> employeeAuto;
  AutoCompleteComboBoxListener<String> locationAuto;

  @FXML private StackPane stackPane;

  public OutsideHospitalSubpageController() {}

  Settings settings;
  private ArrayList<Node> nodes = new ArrayList<Node>();
  FuzzySearchComboBoxListener locationFuzzy;

  @FXML
  private void initialize() {

    settings = Settings.getSettings();

    backBtn.setOnAction(e -> buttonClicked(e));
    submitBtn.setOnAction(e -> submitBtnClicked());
    clearBtn.setOnAction(e -> clearButton());

    try {
      nodes = DataOperations.getListOfNodes();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }

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
    locationAuto = new AutoCompleteComboBoxListener<>(locationComboBox);

    Platform.runLater(
        () -> {
          resetComboBoxes();
        });
  }

  private void resetComboBoxes() {

    locationComboBox.getItems().remove(0, locationComboBox.getItems().size());
    for (Node node : nodes) {
      String name = node.longName;
      String type = node.nodeType;
      // Filtering out the unwanted midway points
      if (!type.equals("WALK")
          && !type.equals("ELEV")
          && !type.equals("HALL")
          && !type.equals("STAI")) {
        locationComboBox.getItems().add(name);
      }
    }
    locationFuzzy = new FuzzySearchComboBoxListener(locationComboBox);
  }

  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == backBtn) parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
  }

  private void clearButton() {
    description.setText("");
    locationComboBox.setValue(null);
    patientName.setText("");
    date.setValue(null);
    desiredLocation.setText("");
    employeeComboBox.getSelectionModel().clearSelection();
  }

  @FXML
  private void submitBtnClicked() {
    // put code for submitting a service request here

    clearIncomplete(desiredLocation);
    clearIncomplete(description);
    clearIncomplete(locationComboBox);
    clearIncomplete(patientName);
    clearIncomplete(date);
    clearIncomplete(employeeComboBox);

    if (description.getText().equals("")
        || locationComboBox.getValue() == null
        || !locationComboBox.getItems().contains(locationComboBox.getValue())
        || desiredLocation.getText().equals("")
        || patientName.getText().equals("")
        || date.getValue() == null
        || ((Settings.getSettings().getCurrentPermissions() == 3
            && ((employeeComboBox.getValue() == null)
                || !employeeComboBox.getItems().contains(employeeComboBox.getValue()))))) {
      if (locationComboBox.getValue() == null
          || !locationComboBox.getItems().contains(locationComboBox.getValue())) {
        incomplete(locationComboBox);
      }
      if (patientName.getText().equals("")) {
        incomplete(patientName);
      }
      if (date.getValue() == null) {
        incomplete(date);
      }
      if (desiredLocation.getText().equals("")) {
        incomplete(desiredLocation);
      }
      if (employeeComboBox.getValue() == null
          || !employeeComboBox.getItems().contains(employeeComboBox.getValue())) {
        incomplete(employeeComboBox);
      }
      if (description.getText().equals("")) {
        incomplete(description);
      }
      nonCompleteForm(stackPane);
    } else {

      Service service = new Service("OH", "Outside Transport");
      // service.setCategory((String) patientName.getText());
      // service.setLocation((String) patientName.getValue());
      service.setDescription(description.getText());
      service.setRequester(settings.getCurrentUsername());
      service.setLocation(
          "From: " + locationComboBox.getValue().toString() + "To: " + desiredLocation.getText());
      service.setCategory(patientName.getText());
      service.setDate(date.getValue().toString());
      service.setAdditionalInfo(desiredLocation.getText());
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
