package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

public class MaintenanceSubPageController extends GenericServiceFormPage {
  // connects the scenebuilder button to a code button
  // add buttons to other scenes here
  @FXML private JFXButton clearBtn;
  @FXML private JFXButton backBtn;
  @FXML private JFXButton submitBtn;
  @FXML private JFXComboBox category;
  @FXML private JFXTextArea description;
  @FXML private JFXComboBox urgency;
  @FXML private JFXTextField date2;
  @FXML private JFXComboBox locationField;
  @FXML private StackPane stackPane;

  private Settings settings;

  private ArrayList<String> categories;
  private ArrayList<String> urgencies;

  // unused constructor
  public MaintenanceSubPageController() {}

  // this runs once the FXML loads in to attach functions to components
  @FXML
  private void initialize() {
    settings = Settings.getSettings();

    categories = new ArrayList<String>();
    categories.add("Room Damage");
    categories.add("Electrical");
    categories.add("Water/Plumbing");
    categories.add("Janitorial");
    categories.add("Property Damage");
    urgencies = new ArrayList<String>();
    urgencies.add("Low");
    urgencies.add("Medium");
    urgencies.add("High");
    ArrayList<String> locations = new ArrayList<String>();
    locations.add("Lobby");
    locations.add("Roof");
    locations.add("Cafeteria");
    // attaches a handler to the button with a lambda expression
    /// clearBtn.setOnAction(e -> serviceButtonClicked(e, "MaintenancePage.fxml"));

    backBtn.setOnAction(e -> buttonClicked(e));
    submitBtn.setOnAction(e -> submitBtnClicked());
    clearBtn.setOnAction(e -> clearButton());

    for (String c : categories) category.getItems().add(c);
    for (String c : urgencies) urgency.getItems().add(c);
    for (String c : locations) locationField.getItems().add(c);
  }

  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == backBtn) parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
  }

  private void clearButton() {
    category.setValue(null);
    urgency.setValue(null);
    description.setText("");
    date2.setText("");
    locationField.setValue(null);
  }

  @FXML
  private void submitBtnClicked() {

    if (category.getValue().equals(null)
        || description.getText().equals("")
        || urgency.getValue().equals(null)
        || date2.getText().equals("")
        || locationField.getValue().equals(null)) {
      nonCompleteForm(stackPane);
    } else {

      // Stage stage = null;
      // put code for submitting a service request here
      Service service = new Service(this.IDCount, "Maintenance");
      this.IDCount++;
      // System.out.println(this.IDCount);
      service.setCategory((String) category.getValue());
      service.setLocation((String) locationField.getValue());
      service.setDescription(description.getText());
      service.setUrgency((String) urgency.getValue());
      service.setDate(date2.getText());
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
