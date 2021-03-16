package edu.wpi.cs3733.c21.teamY.pages.serviceRequests;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
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

public class ITSubpageController extends GenericServiceFormPage {

  @FXML private JFXButton clearBtn;
  @FXML private JFXButton backBtn;
  @FXML private JFXButton submitBtn;
  @FXML private JFXComboBox categoryComboBox;
  @FXML private JFXComboBox locationComboBox;
  @FXML private JFXComboBox affectsComboBox;
  @FXML private JFXTextArea description;
  @FXML private JFXComboBox employeeComboBox;

  AutoCompleteComboBoxListener<String> locationAuto;
  AutoCompleteComboBoxListener<String> categoryAuto;
  AutoCompleteComboBoxListener<String> affectsAuto;
  AutoCompleteComboBoxListener<String> employeeAuto;

  @FXML private StackPane stackPane;

  Settings settings;
  private ArrayList<Node> nodes = new ArrayList<Node>();
  FuzzySearchComboBoxListener locationFuzzy;

  public ITSubpageController() {}

  @FXML
  private void initialize() {

    settings = Settings.getSettings();

    backBtn.setOnAction(e -> buttonClicked(e));
    submitBtn.setOnAction(e -> submitBtnClicked());
    clearBtn.setOnAction(e -> clearButton());

    categoryComboBox.getItems().add("Hardware");
    categoryComboBox.getItems().add("Software");
    categoryComboBox.getItems().add("Login");
    affectsComboBox.getItems().add("Floor Efficiency");
    affectsComboBox.getItems().add("Daily Tasks");

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
    affectsAuto = new AutoCompleteComboBoxListener<>(affectsComboBox);
    locationAuto = new AutoCompleteComboBoxListener<>(locationComboBox);
    categoryAuto = new AutoCompleteComboBoxListener<>(employeeComboBox);

    Platform.runLater(
        () -> {
          resetComboBoxes();
        });
  }

  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == backBtn) parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
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

  private void clearButton() {
    categoryComboBox.setValue(null);
    locationComboBox.setValue(null);
    affectsComboBox.setValue(null);
    description.setText("");
    employeeComboBox.getSelectionModel().clearSelection();
  }

  @FXML
  private void submitBtnClicked() {
    // put code for submitting a service request here

    clearIncomplete(categoryComboBox);
    clearIncomplete(locationComboBox);
    clearIncomplete(affectsComboBox);
    clearIncomplete(description);
    clearIncomplete(employeeComboBox);

    if (categoryComboBox.getValue() == null
        || locationComboBox.getValue() == null
        || !locationComboBox.getItems().contains(locationComboBox.getValue())
        || affectsComboBox.getValue() == null
        || description.getText().equals("")
        || (Settings.getSettings().getCurrentPermissions() == 3
            && ((employeeComboBox.getValue() == null)
                || !employeeComboBox.getItems().contains(employeeComboBox.getValue())))) {
      if (categoryComboBox.getValue() == null) {
        incomplete(categoryComboBox);
      }
      if (locationComboBox.getValue() == null
          || !locationComboBox.getItems().contains(locationComboBox.getValue())) {
        incomplete(locationComboBox);
      }
      if (affectsComboBox.getValue() == null) {
        incomplete(affectsComboBox);
      }
      if (description.getText().equals("")) {
        incomplete(description);
      }
      if (employeeComboBox.getValue() == null
          || !employeeComboBox.getItems().contains(employeeComboBox.getValue())) {
        incomplete(employeeComboBox);
      }
      nonCompleteForm(stackPane);
    } else {

      Service service = new Service(DataOperations.generateUniqueID("IT"), "IT Request");
      service.setCategory((String) categoryComboBox.getValue());
      service.setLocation((String) locationComboBox.getValue());
      service.setAdditionalInfo("Affects: " + (String) affectsComboBox.getValue());
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
}
