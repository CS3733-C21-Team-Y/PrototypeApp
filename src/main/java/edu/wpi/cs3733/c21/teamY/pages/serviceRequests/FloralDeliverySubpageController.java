package edu.wpi.cs3733.c21.teamY.pages.serviceRequests;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.c21.teamY.dataops.AutoCompleteComboBoxListener;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.FuzzySearchComboBoxListener;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import edu.wpi.cs3733.c21.teamY.entity.*;
import edu.wpi.cs3733.c21.teamY.pages.GenericServiceFormPage;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.layout.StackPane;

public class FloralDeliverySubpageController extends GenericServiceFormPage {

  @FXML private JFXButton clearBtn;
  @FXML private JFXButton backBtn;
  @FXML private JFXButton submitBtn;
  @FXML private JFXComboBox locationComboBox;
  @FXML private JFXTextField categoryInput;
  @FXML private JFXTextField fromInput;
  @FXML private JFXTextField toInput;
  @FXML private JFXTextArea descriptionInput;
  @FXML private JFXTextField dateInput;
  @FXML private JFXComboBox employeeComboBox;
  Settings settings;
  AutoCompleteComboBoxListener<String> employeeAuto;
  AutoCompleteComboBoxListener<String> locationAuto;
  private ArrayList<Node> nodes = new ArrayList<Node>();
  FuzzySearchComboBoxListener locationFuzzy;

  @FXML private StackPane stackPane;

  public FloralDeliverySubpageController() {}

  // this runs once the FXML loads in to attach functions to components
  @FXML
  private void initialize() {
    settings = Settings.getSettings();
    backBtn.setOnAction(e -> buttonClicked(e));
    backBtn.setCursor(Cursor.HAND);
    submitBtn.setOnAction(e -> submitBtnClicked());
    submitBtn.setCursor(Cursor.HAND);
    clearBtn.setOnAction(e -> clearButton());
    clearBtn.setCursor(Cursor.HAND);

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
    locationComboBox.setValue(null);
    categoryInput.setText("");
    descriptionInput.setText("");
    fromInput.setText("");
    toInput.setText("");
    dateInput.setText("");
    employeeComboBox.getSelectionModel().clearSelection();
  }

  @FXML
  private void submitBtnClicked() {
    // put code for submitting a service request here

    clearIncomplete(locationComboBox);
    clearIncomplete(categoryInput);
    clearIncomplete(descriptionInput);
    clearIncomplete(dateInput);
    clearIncomplete(fromInput);
    clearIncomplete(toInput);
    clearIncomplete(employeeComboBox);

    if (locationComboBox.getValue() == null
        || !locationComboBox.getItems().contains(locationComboBox.getValue())
        || categoryInput.getText().equals("")
        || descriptionInput.getText().equals("")
        || dateInput.getText().equals("")
        || fromInput.getText().equals("")
        || toInput.getText().equals("")
        || (Settings.getSettings().getCurrentPermissions() == 3
            && ((employeeComboBox.getValue() == null)
                || !employeeComboBox.getItems().contains(employeeComboBox.getValue())))) {
      if (categoryInput.getText().equals("")) {
        incomplete(categoryInput);
      }
      if (descriptionInput.getText().equals("")) {
        incomplete(descriptionInput);
      }
      if (dateInput.getText().equals("")) {
        incomplete(dateInput);
      }
      if (locationComboBox.getValue() == null
          || !locationComboBox.getItems().contains(locationComboBox.getValue())) {
        incomplete(locationComboBox);
      }

      if (toInput.getText().equals("")) {
        incomplete(toInput);
      }
      if (fromInput.getText().equals("")) {
        incomplete(fromInput);
      }
      if (employeeComboBox.getValue() == null
          || !employeeComboBox.getItems().contains(employeeComboBox.getValue())) {
        incomplete(employeeComboBox);
      }
      nonCompleteForm(stackPane);
    } else {
      Service service = new Service(DataOperations.generateUniqueID("FD"), "Floral Delivery");
      service.setLocation(locationComboBox.getValue().toString());
      service.setCategory(categoryInput.getText());
      service.setDescription(descriptionInput.getText());
      service.setRequester(settings.getCurrentUsername());
      service.setAdditionalInfo("From: " + fromInput.getText() + " To: " + toInput.getText());
      service.setDate(dateInput.getText());
      if (settings.getCurrentPermissions() == 3) {
        service.setEmployee((String) employeeComboBox.getValue());
      } else {
        service.setEmployee("admin");
      }

      clearButton();
      parent.loadCenterSubPage("ServiceRequestNavigator.fxml");
      submittedPopUp(stackPane);
      try {
        DataOperations.saveService(service);
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }
}
