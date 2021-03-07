package edu.wpi.cs3733.c21.teamY.pages.serviceRequests;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import edu.wpi.cs3733.c21.teamY.entity.Employee;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import edu.wpi.cs3733.c21.teamY.pages.GenericServiceFormPage;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class FloralDeliverySubpageController extends GenericServiceFormPage {

  @FXML private JFXButton clearBtn;
  @FXML private JFXButton backBtn;
  @FXML private JFXButton submitBtn;
  @FXML private JFXTextField roomNumberInput;
  @FXML private JFXTextField categoryInput;
  @FXML private JFXTextField fromInput;
  @FXML private JFXTextField toInput;
  @FXML private JFXTextArea descriptionInput;
  @FXML private JFXTextField dateInput;
  @FXML private JFXComboBox employeeComboBox;
  Settings settings;

  @FXML private StackPane stackPane;

  public FloralDeliverySubpageController() {}

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

  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == backBtn) parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
  }

  private void clearButton() {
    roomNumberInput.setText("");
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

    if (roomNumberInput.getText().equals("")
        || categoryInput.getText().equals("")
        || descriptionInput.getText().equals("")
        || dateInput.getText().equals("")) {
      nonCompleteForm(stackPane);
    } else {
      Service service = new Service(this.IDCount, "Floral Delivery");
      this.IDCount++;
      service.setLocation(roomNumberInput.getText());
      service.setCategory(categoryInput.getText());
      service.setDescription(descriptionInput.getText());
      service.setRequester(settings.getCurrentUsername());
      service.setAdditionalInfo(fromInput.getText() + " " + toInput.getText());
      service.setDate(dateInput.getText());
      service.setEmployee((String) employeeComboBox.getValue());

      clearButton();
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
