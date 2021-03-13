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

public class GiftDeliverySubpageController extends GenericServiceFormPage {

  @FXML private JFXButton clearBtn;
  @FXML private JFXButton backBtn;
  @FXML private JFXButton submitBtn;
  @FXML private JFXComboBox locationField;
  @FXML private JFXComboBox giftType;
  @FXML private JFXTextArea description;
  @FXML private JFXDatePicker datePicker;
  @FXML private JFXComboBox employeeComboBox;
  AutoCompleteComboBoxListener<String> locationAuto;
  AutoCompleteComboBoxListener<String> typeAuto;
  AutoCompleteComboBoxListener<String> employeeAuto;

  private Settings settings;

  @FXML private StackPane stackPane;

  public GiftDeliverySubpageController() {}

  // this runs once the FXML loads in to attach functions to components
  @FXML
  private void initialize() {

    settings = Settings.getSettings();

    backBtn.setOnAction(e -> buttonClicked(e));
    submitBtn.setOnAction(e -> submitBtnClicked());
    clearBtn.setOnAction(e -> clearButton());

    locationField.getItems().add("Room 142");
    locationField.getItems().add("Room 736");
    locationField.getItems().add("Room 246");
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
    locationAuto = new AutoCompleteComboBoxListener<>(locationField);
  }

  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == backBtn) parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
  }

  private void clearButton() {
    giftType.setValue(null);
    locationField.setValue(null);
    description.setText("");
    datePicker.setValue(null);
    employeeComboBox.getSelectionModel().clearSelection();
  }

  @FXML
  private void submitBtnClicked() {
    // put code for submitting a service request here

    clearIncomplete(locationField);
    clearIncomplete(description);
    clearIncomplete(datePicker);
    clearIncomplete(employeeComboBox);

    if (giftType.getValue() == null
        || locationField.getValue() == null
        || description.getText().equals("")
        || datePicker.getValue() == null
        || (Settings.getSettings().getCurrentPermissions() == 3
            && employeeComboBox.getValue() == null)) {
      if (locationField.getValue() == null) {
        incomplete(locationField);
      }
      if (datePicker.getValue() == null) {
        incomplete(datePicker);
      }
      if (description.getText().equals("")) {
        incomplete(description);
      }
      if (employeeComboBox.getValue() == null) {
        incomplete(employeeComboBox);
      }
      if (giftType.getValue() == null) {
        incomplete(giftType);
      }
      nonCompleteForm(stackPane);
    } else {
      Service service = new Service( "Gift Delivery");
      service.setCategory((String) giftType.getValue());
      service.setLocation((String) locationField.getValue());
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
