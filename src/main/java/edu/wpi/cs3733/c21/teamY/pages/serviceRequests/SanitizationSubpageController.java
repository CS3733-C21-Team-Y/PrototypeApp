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
import javafx.scene.Cursor;
import javafx.scene.layout.StackPane;

public class SanitizationSubpageController extends GenericServiceFormPage {

  @FXML private JFXComboBox locationComboBox;
  @FXML private JFXComboBox urgency;
  @FXML private JFXComboBox biohazardLevel;
  @FXML private JFXComboBox employeeComboBox;
  // @FXML private JFXTextField chemHazardLevel;

  AutoCompleteComboBoxListener<String> locationAuto;
  AutoCompleteComboBoxListener<String> urgencyAuto;
  AutoCompleteComboBoxListener<String> employeeAuto;
  AutoCompleteComboBoxListener<String> biohazardAuto;

  Settings settings;
  private ArrayList<Node> nodes = new ArrayList<Node>();
  FuzzySearchComboBoxListener locationFuzzy;

  @FXML private JFXTextArea description;

  @FXML private StackPane stackPane;

  @FXML private JFXButton submitBtn;
  @FXML private JFXButton clearBtn;
  @FXML private JFXButton backBtn;

  private ArrayList<String> urgencies;
  private ArrayList<String> biohazardLevels;

  public SanitizationSubpageController() {}

  @FXML
  private void initialize() {
    settings = Settings.getSettings();

    try {
      nodes = DataOperations.getListOfNodes();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }

    urgencies = new ArrayList<>();
    urgencies.add("Low");
    urgencies.add("Medium");
    urgencies.add("High");

    biohazardLevels = new ArrayList<>();
    biohazardLevels.add("BSL-1");
    biohazardLevels.add("BSL-2");
    biohazardLevels.add("BSL-3");
    biohazardLevels.add("BSL-4");

    backBtn.setOnAction(e -> buttonClicked(e));
    submitBtn.setOnAction(e -> submitBtnClicked());
    clearBtn.setOnAction(e -> clearButton());
    backBtn.setCursor(Cursor.HAND);
    submitBtn.setCursor(Cursor.HAND);
    clearBtn.setCursor(Cursor.HAND);

    for (String c : urgencies) urgency.getItems().add(c);
    for (String c : biohazardLevels) biohazardLevel.getItems().add(c);

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

    urgencyAuto = new AutoCompleteComboBoxListener<>(urgency);
    employeeAuto = new AutoCompleteComboBoxListener<>(employeeComboBox);
    biohazardAuto = new AutoCompleteComboBoxListener<>(biohazardLevel);
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
    locationComboBox.setValue(null);
    description.setText("");
    biohazardLevel.setValue(null);
    urgency.setValue(null);
    employeeComboBox.getSelectionModel().clearSelection();
  }

  @FXML
  private void submitBtnClicked() {

    clearIncomplete(locationComboBox);
    clearIncomplete(urgency);
    clearIncomplete(biohazardLevel);
    clearIncomplete(description);
    clearIncomplete(employeeComboBox);

    if (locationComboBox.getValue() == null
        || !locationComboBox.getItems().contains(locationComboBox.getValue())
        || urgency.toString().equals("")
        || biohazardLevel.toString().equals("")
        || description.getText().equals("")) {
      if (description.getText().equals("")
          || (Settings.getSettings().getCurrentPermissions() == 3
              && ((employeeComboBox.getValue() == null)
                  || !employeeComboBox.getItems().contains(employeeComboBox.getValue())))) {
        incomplete(description);
      }
      if (urgency.getValue() == null) {
        incomplete(urgency);
      }
      if (locationComboBox.getValue() == null
          || !locationComboBox.getItems().contains(locationComboBox.getValue())) {
        incomplete(locationComboBox);
      }
      if (biohazardLevel.getValue() == null) {
        incomplete(biohazardLevel);
      }
      if (employeeComboBox.getValue() == null
          || !employeeComboBox.getItems().contains(employeeComboBox.getValue())) {
        incomplete(employeeComboBox);
      }
      nonCompleteForm(stackPane);
    } else {

      // put code for submitting a service request here
      Service service = new Service(DataOperations.generateUniqueID("SAN"), "Sanitization");
      // System.out.println(this.IDCount);
      service.setLocation(locationComboBox.getValue().toString());
      service.setCategory((String) biohazardLevel.getValue());
      service.setDescription(description.getText());
      service.setAdditionalInfo("Urgency: " + (String) urgency.getValue());
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
