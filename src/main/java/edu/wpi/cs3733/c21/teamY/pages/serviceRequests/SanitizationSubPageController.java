package edu.wpi.cs3733.c21.teamY.pages.serviceRequests;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import edu.wpi.cs3733.c21.teamY.pages.GenericServiceFormPage;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class SanitizationSubPageController extends GenericServiceFormPage {

  @FXML private JFXComboBox locationField;
  @FXML private JFXComboBox urgency;
  @FXML private JFXComboBox biohazardLevel;
  // @FXML private JFXTextField chemHazardLevel;

  @FXML private JFXTextArea description;

  @FXML private StackPane stackPane;

  @FXML private JFXButton submitBtn;
  @FXML private JFXButton clearBtn;
  @FXML private JFXButton backBtn;

  private ArrayList<String> urgencies;
  private ArrayList<String> biohazardLevels;
  private ArrayList<String> locationFields;

  private Settings settings;

  public SanitizationSubPageController() {}

  @FXML
  private void initialize() {
    settings = Settings.getSettings();

    locationFields = new ArrayList<>();
    locationFields.add("Emergency Room");
    locationFields.add("Operating Theatre");
    locationFields.add("Pediatrics Office");
    locationFields.add("Intensive Care Unit");

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

    for (String c : urgencies) urgency.getItems().add(c);
    for (String c : locationFields) locationField.getItems().add(c);
    for (String c : biohazardLevels) biohazardLevel.getItems().add(c);
  }

  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == backBtn) parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
  }

  private void clearButton() {
    locationField.setValue(null);
    description.setText("");
    biohazardLevel.setValue(null);
    urgency.setValue(null);
  }

  @FXML
  private void submitBtnClicked() {

    if (locationField.toString().equals("")
        || urgency.toString().equals("")
        || biohazardLevel.toString().equals("")
        || description.getText().equals("")) {
      nonCompleteForm(stackPane);
    } else {

      // put code for submitting a service request here
      Service service = new Service(this.IDCount, "Sanitization");
      this.IDCount++;
      // System.out.println(this.IDCount);
      service.setLocation((String) locationField.getValue());
      service.setCategory((String) biohazardLevel.getValue());
      service.setDescription(description.getText());
      service.setUrgency((String) urgency.getValue());
      service.setRequester(settings.getCurrentUsername());

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
