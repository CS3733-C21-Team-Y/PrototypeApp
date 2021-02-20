package edu.wpi.teamY;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ServiceRequestPageController {

  // connects the scenebuilder button to a code button
  // add buttons to other scenes here
  @FXML private Button toHomePageBtn;
  @FXML private Button toLaundryBtn;
  @FXML private Button toMaintenanceBtn;

  // unused constructor
  public ServiceRequestPageController() {}

  // this runs once the FXML loads in to attach functions to components
  @FXML
  private void initialize() {
    // attaches a handler to the button with a lambda expression
    toHomePageBtn.setOnAction(e -> buttonClicked(e));
    toLaundryBtn.setOnAction(e -> buttonClicked(e));
    toMaintenanceBtn.setOnAction(e -> buttonClicked(e));
  }

  // button event handler
  @FXML
  private void buttonClicked(ActionEvent e) {
    // error handling for FXMLLoader.load
    try {
      // initializing stage
      Stage stage = null;

      if (e.getSource() == toHomePageBtn) {
        // gets the current stage
        stage = (Stage) toHomePageBtn.getScene().getWindow();
        // sets the new scene to the alex page
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("HomePage.fxml"))));

      } else if (e.getSource() == toLaundryBtn) {
        // gets the current stage
        stage = (Stage) toLaundryBtn.getScene().getWindow();
        // sets the new scene to the alex page
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("LaundryPage.fxml"))));
      } else if (e.getSource() == toMaintenanceBtn) {
        // gets the current stage
        stage = (Stage) toMaintenanceBtn.getScene().getWindow();
        // sets the new scene to the alex page
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("MaintenancePage.fxml"))));
      } else {

      }

      // display new stage
      stage.show();
    } catch (Exception exp) {
    }
  }
}
