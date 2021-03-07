package edu.wpi.cs3733.c21.teamY.pages.serviceRequests;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import edu.wpi.cs3733.c21.teamY.entity.Employee;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import edu.wpi.cs3733.c21.teamY.pages.GenericServiceFormPage;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class AudioVisualSubpageController extends GenericServiceFormPage {

  @FXML private Button avClearBtn;
  @FXML private Button avSubmitBtn;
  @FXML private JFXComboBox avTypeComboBox;
  @FXML private JFXComboBox avLocationComboBox;
  @FXML private JFXComboBox avEmployeeComboBox;
  @FXML private JFXDatePicker avDate;
  @FXML private JFXTextArea avDesc;
  @FXML private Button backBtn;

  private Settings settings;

  @FXML private StackPane stackPane;

  public AudioVisualSubpageController() {}

  @FXML
  public void initialize() {

    settings = Settings.getSettings();
    // add combobox items
    avTypeComboBox.getItems().add("Patient TV");
    avTypeComboBox.getItems().add("Lecture Hall Setup");
    avLocationComboBox.getItems().add("RM 124");
    avLocationComboBox.getItems().add("Lecture Hall 1");

    if (settings.getCurrentPermissions() == 3) {
      avEmployeeComboBox.setVisible(true);
      try {
        ArrayList<Employee> employeeList = DataOperations.getStaffList();
        for (Employee employee : employeeList) {
          avEmployeeComboBox.getItems().add(employee.getEmployeeID());
        }
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }
    } else {
      avEmployeeComboBox.setVisible(false);
    }

    // initialize buttons
    // avClearBtn.setOnAction(e -> serviceButtonClicked(e, "AudioVisualSubPage.fxml"));
    avSubmitBtn.setOnAction(e -> submitBtnClicked());
    backBtn.setOnAction(e -> buttonClicked(e));
    avClearBtn.setOnAction(e -> clearButton());
  }

  private void buttonClicked(ActionEvent e) {

    if (e.getSource() == backBtn) parent.loadRightSubPage("ServiceRequestManagerSubPage.fxml");
  }

  private void clearButton() {
    avTypeComboBox.getSelectionModel().clearSelection();
    avTypeComboBox.setValue(null);
    avLocationComboBox.getSelectionModel().clearSelection();
    avLocationComboBox.setValue(null);
    avDate.setValue(null);
    avDesc.setText("");
    avEmployeeComboBox.getSelectionModel().clearSelection();
  }

  @FXML
  private void submitBtnClicked() {
    // put code for submitting a service request here

    System.out.println("hi" + avDesc.getText());
    if (avLocationComboBox == null || avTypeComboBox == null || avDesc.getText().equals("")) {
      nonCompleteForm(stackPane);
    } else {
      Service service = new Service(this.IDCount, "Audio Visual");
      this.IDCount++;
      service.setCategory((String) avTypeComboBox.getValue());
      service.setLocation((String) avLocationComboBox.getValue());
      service.setDate(avDate.getValue().toString());
      service.setDescription(avDesc.getText());
      service.setRequester(settings.getCurrentUsername());
      if (settings.getCurrentPermissions() == 3) {
        service.setEmployee((String) avEmployeeComboBox.getValue());
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
