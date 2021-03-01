package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SanitizationSubPageController extends GenericServiceFormPage {

  @FXML private JFXComboBox location;
  @FXML private JFXComboBox urgency;
  @FXML private JFXComboBox biohazardLevel;
  // @FXML private JFXTextField chemHazardLevel;

  @FXML private JFXTextArea description;

  /*
  @FXML private JFXCheckBox bioHazardBox;
  @FXML private JFXCheckBox chemHazardBox;

   */

  @FXML private JFXButton submitBtn;
  @FXML private JFXButton clearBtn;
  @FXML private JFXButton backBtn;

  private ArrayList<String> urgencies;
  private ArrayList<String> biohazardLevels;
  private ArrayList<String> locations;

  private Settings settings;

  public SanitizationSubPageController() {}

  @FXML
  private void initialize() {
    settings = Settings.getSettings();

    urgencies = new ArrayList<>();
    urgencies.add("Low");
    urgencies.add("Medium");
    urgencies.add("High");

    biohazardLevels = new ArrayList<>();
    biohazardLevels.add("BSL-1");
    biohazardLevels.add("BSL-2");
    biohazardLevels.add("BSL-3");
    biohazardLevels.add("BSL-4");

    locations = new ArrayList<>();
    locations.add("Emergency Room");
    locations.add("ICU");
    locations.add("Operating Room");

    backBtn.setOnAction(e -> buttonClicked(e));
    submitBtn.setOnAction(e -> submitBtnClicked());

    // for (String c : locationList) location.getItems().add(c);
    for (String c : urgencies) urgency.getItems().add(c);
    for (String c : biohazardLevels) biohazardLevel.getItems().add(c);
    // for (String c : locations) location.getItems().add(c);
  }

  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == backBtn) parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
  }

  @FXML
  private void submitBtnClicked() {
    // Stage stage = null;
    // put code for submitting a service request here
    Service service = new Service(this.IDCount, "Sanitization");
    this.IDCount++;
    // System.out.println(this.IDCount);
    service.setLocation((String) location.getValue());
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
  }
}
