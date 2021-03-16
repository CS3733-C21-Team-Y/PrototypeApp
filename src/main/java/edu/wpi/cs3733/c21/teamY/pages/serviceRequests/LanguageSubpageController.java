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
import javafx.scene.Cursor;
import javafx.scene.layout.StackPane;

public class LanguageSubpageController extends GenericServiceFormPage {

  @FXML private JFXButton testBtn;
  @FXML private JFXButton backBtn;
  @FXML private JFXButton testBtn2;
  @FXML private JFXComboBox langOptions;
  @FXML private JFXComboBox urgency;
  @FXML private JFXComboBox locationComboBox;
  @FXML private JFXTextArea description;
  @FXML private JFXComboBox employeeComboBox;

  AutoCompleteComboBoxListener<String> employeeAuto;
  AutoCompleteComboBoxListener<String> urgencyAuto;
  AutoCompleteComboBoxListener<String> langAuto;
  AutoCompleteComboBoxListener<String> locationAuto;

  @FXML private StackPane stackPane;

  Settings settings;
  private ArrayList<Node> nodes = new ArrayList<Node>();
  FuzzySearchComboBoxListener locationFuzzy;

  public LanguageSubpageController() {}

  @FXML
  private void initialize() {

    settings = Settings.getSettings();

    backBtn.setOnAction(e -> buttonClicked(e));
    testBtn2.setOnAction(e -> submitBtnClicked());
    testBtn.setOnAction(e -> clearButton());
    backBtn.setCursor(Cursor.HAND);
    testBtn2.setCursor(Cursor.HAND);
    testBtn.setCursor(Cursor.HAND);

    try {
      nodes = DataOperations.getListOfNodes();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }

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

    langAuto = new AutoCompleteComboBoxListener<>(langOptions);
    employeeAuto = new AutoCompleteComboBoxListener<>(employeeComboBox);
    urgencyAuto = new AutoCompleteComboBoxListener<>(urgency);
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
    langOptions.setValue(null);
    locationComboBox.setValue(null);
    description.setText("");
    urgency.setValue(null);
    employeeComboBox.getSelectionModel().clearSelection();
  }

  @FXML
  private void submitBtnClicked() {
    // put code for submitting a service request here

    clearIncomplete(langOptions);
    clearIncomplete(locationComboBox);
    clearIncomplete(description);
    clearIncomplete(urgency);
    clearIncomplete(employeeComboBox);

    if (langOptions.getValue() == null
        || description.getText().equals("")
        || urgency.getValue() == null
        || locationComboBox.getValue() == null
        || !locationComboBox.getItems().contains(locationComboBox.getValue())
        || (Settings.getSettings().getCurrentPermissions() == 3
            && ((employeeComboBox.getValue() == null)
                || !employeeComboBox.getItems().contains(employeeComboBox.getValue())))) {
      if (langOptions.getValue() == null) {
        incomplete(langOptions);
      }
      if (description.getText().equals("")) {
        incomplete(description);
      }
      if (urgency.getValue() == null) {
        incomplete(urgency);
      }
      if (locationComboBox.getValue() == null
          || !locationComboBox.getItems().contains(locationComboBox.getValue())) {
        incomplete(locationComboBox);
      }
      if (employeeComboBox.getValue() == null
          || !employeeComboBox.getItems().contains(employeeComboBox.getValue())) {
        incomplete(employeeComboBox);
      }
      nonCompleteForm(stackPane);
    } else {
      Service service = new Service(DataOperations.generateUniqueID("LANG"), "Language");
      service.setCategory((String) langOptions.getValue());
      service.setLocation(locationComboBox.getValue().toString());
      service.setDescription(description.getText());
      service.setRequester(settings.getCurrentUsername());
      service.setAdditionalInfo("Urgency: " + (String) urgency.getValue());
      if (settings.getCurrentPermissions() == 3) {
        service.setEmployee((String) employeeComboBox.getValue());
      } else {
        service.setEmployee("admin");
      }
      submittedPopUp(stackPane);
      parent.loadCenterSubPage("ServiceRequestNavigator.fxml");
      clearButton();

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
