package edu.wpi.cs3733.c21.teamY.pages.serviceRequests;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
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
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class AudioVisualSubpageController extends GenericServiceFormPage {

  @FXML private Button avClearBtn;
  @FXML private Button avSubmitBtn;
  @FXML private JFXComboBox avTypeComboBox;
  @FXML private JFXComboBox locationComboBox;
  @FXML private JFXComboBox avEmployeeComboBox;
  @FXML private JFXDatePicker avDate;
  @FXML private JFXTextArea avDesc;
  @FXML private Button backBtn;
  AutoCompleteComboBoxListener<String> typeAuto;
  AutoCompleteComboBoxListener<String> locationAuto;
  AutoCompleteComboBoxListener<String> employeeAuto;

  Settings settings;
  private ArrayList<Node> nodes = new ArrayList<Node>();
  FuzzySearchComboBoxListener locationFuzzy;

  @FXML private StackPane stackPane;

  public AudioVisualSubpageController() {}

  @FXML
  public void initialize() {

    settings = Settings.getSettings();
    // add combobox items
    try {
      nodes = DataOperations.getListOfNodes();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }

    avTypeComboBox.getItems().add("Intercom");
    avTypeComboBox.getItems().add("TV");
    avTypeComboBox.getItems().add("Phone");
    avTypeComboBox.getItems().add("Other");

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
    avSubmitBtn.setCursor(Cursor.HAND);
    backBtn.setOnAction(e -> buttonClicked(e));
    backBtn.setCursor(Cursor.HAND);
    avClearBtn.setOnAction(e -> clearButton());
    avClearBtn.setCursor(Cursor.HAND);
    employeeAuto = new AutoCompleteComboBoxListener<>(avEmployeeComboBox);
    locationAuto = new AutoCompleteComboBoxListener<>(locationComboBox);
    typeAuto = new AutoCompleteComboBoxListener<>(avTypeComboBox);

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
    avTypeComboBox.getSelectionModel().clearSelection();
    avTypeComboBox.setValue(null);
    locationComboBox.setValue(null);
    avDate.setValue(null);
    avDesc.setText("");
    avEmployeeComboBox.getSelectionModel().clearSelection();
  }

  private void submitBtnClicked() {
    // put code for submitting a service request here

    clearIncomplete(locationComboBox);
    clearIncomplete(avTypeComboBox);
    clearIncomplete(avDate);
    clearIncomplete(avDesc);
    clearIncomplete(avEmployeeComboBox);

    if (locationComboBox.getValue() == null
        || !locationComboBox.getItems().contains(locationComboBox.getValue())
        || avTypeComboBox.getValue() == null
        || avDesc.getText().equals("")
        || avDate.getValue() == null
        || (Settings.getSettings().getCurrentPermissions() == 3
            && ((avEmployeeComboBox.getValue() == null)
                || !avEmployeeComboBox.getItems().contains(avEmployeeComboBox.getValue())))) {
      if (locationComboBox.getValue() == null
          || !locationComboBox.getItems().contains(locationComboBox.getValue())) {
        incomplete(locationComboBox);
      }
      if (avTypeComboBox.getValue() == null) {
        incomplete(avTypeComboBox);
      }
      if (avDesc.getText().equals("")) {
        incomplete(avDesc);
      }
      if (avDate.getValue() == null) {
        incomplete(avDate);
        incomplete(avDate);
      }
      if (avEmployeeComboBox.getValue() == null
          || !avEmployeeComboBox.getItems().contains(avEmployeeComboBox.getValue())) {
        incomplete(avEmployeeComboBox);
      }
      nonCompleteForm(stackPane);
    } else {
      Service service = new Service(DataOperations.generateUniqueID("AV"), "Audio Visual");

      service.setCategory((String) avTypeComboBox.getValue());
      service.setLocation(locationComboBox.getValue().toString());
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
      parent.loadCenterSubPage("ServiceRequestNavigator.fxml");
      clearButton();
    }
  }
}
