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

public class GiftDeliverySubpageController extends GenericServiceFormPage {

  @FXML private JFXButton clearBtn;
  @FXML private JFXButton backBtn;
  @FXML private JFXButton submitBtn;
  @FXML private JFXComboBox locationComboBox;
  @FXML private JFXComboBox giftType;
  @FXML private JFXTextArea description;
  @FXML private JFXDatePicker datePicker;
  @FXML private JFXComboBox employeeComboBox;
  AutoCompleteComboBoxListener<String> typeAuto;
  AutoCompleteComboBoxListener<String> employeeAuto;

  Settings settings;
  private ArrayList<Node> nodes = new ArrayList<Node>();
  FuzzySearchComboBoxListener locationFuzzy;

  @FXML private StackPane stackPane;

  public GiftDeliverySubpageController() {}

  // this runs once the FXML loads in to attach functions to components
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

    giftType.getItems().add("Teddy Bear");
    giftType.getItems().add("Snack Basket");
    giftType.getItems().add("Blanket");

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
    typeAuto = new AutoCompleteComboBoxListener<>(giftType);

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
    giftType.setValue(null);
    locationComboBox.setValue(null);
    description.setText("");
    datePicker.setValue(null);
    employeeComboBox.getSelectionModel().clearSelection();
  }

  @FXML
  private void submitBtnClicked() {
    // put code for submitting a service request here

    clearIncomplete(locationComboBox);
    clearIncomplete(description);
    clearIncomplete(datePicker);
    clearIncomplete(employeeComboBox);

    if (giftType.getValue() == null
        || locationComboBox.getValue() == null
        || !locationComboBox.getItems().contains(locationComboBox.getValue())
        || description.getText().equals("")
        || datePicker.getValue() == null
        || (Settings.getSettings().getCurrentPermissions() == 3
            && ((employeeComboBox.getValue() == null)
                || !employeeComboBox.getItems().contains(employeeComboBox.getValue())))) {
      if (locationComboBox.getValue() == null
          || !locationComboBox.getItems().contains(locationComboBox.getValue())) {
        incomplete(locationComboBox);
      }
      if (datePicker.getValue() == null) {
        incomplete(datePicker);
      }
      if (description.getText().equals("")) {
        incomplete(description);
      }
      if (employeeComboBox.getValue() == null
          || !employeeComboBox.getItems().contains(employeeComboBox.getValue())) {
        incomplete(employeeComboBox);
      }
      if (giftType.getValue() == null) {
        incomplete(giftType);
      }
      nonCompleteForm(stackPane);
    } else {
      Service service = new Service(DataOperations.generateUniqueID("GD"), "Gift Delivery");
      service.setCategory((String) giftType.getValue());
      service.setLocation(locationComboBox.getValue().toString());
      service.setDescription(description.getText());
      service.setDate(datePicker.getValue().toString());
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
