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

public class LanguageSubpageController extends GenericServiceFormPage {

  @FXML private JFXButton testBtn;
  @FXML private JFXButton backBtn;
  @FXML private JFXButton testBtn2;
  @FXML private JFXComboBox langOptions;
  @FXML private JFXComboBox urgency;
  @FXML private JFXTextField locationField;
  @FXML private JFXTextArea description;
  @FXML private JFXComboBox employeeComboBox;

  @FXML private StackPane stackPane;

  private Settings settings;

  public LanguageSubpageController() {}

  @FXML
  private void initialize() {

    settings = Settings.getSettings();

    backBtn.setOnAction(e -> buttonClicked(e));
    testBtn2.setOnAction(e -> submitBtnClicked());
    testBtn.setOnAction(e -> clearButton());

    langOptions.getItems().add("Spanish");
    langOptions.getItems().add("French");
    langOptions.getItems().add("Chinese");
    langOptions.getItems().add("Portuguese");
    langOptions.getItems().add("Vietnamese");
    urgency.getItems().add("Emergency");
    urgency.getItems().add("High");
    urgency.getItems().add("Medium");
    urgency.getItems().add("Low");

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
    langOptions.setValue(null);
    locationField.setText("");
    description.setText("");
    urgency.setValue(null);
    employeeComboBox.getSelectionModel().clearSelection();
  }

  @FXML
  private void submitBtnClicked() {
    // put code for submitting a service request here

    if (langOptions.getValue() == null || description.getText().equals("")) {
      nonCompleteForm(stackPane);
    } else {
      Service service = new Service(this.IDCount, "Language");
      this.IDCount++;
      service.setCategory((String) langOptions.getValue());
      service.setLocation(locationField.getText());
      service.setDescription(description.getText());
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
