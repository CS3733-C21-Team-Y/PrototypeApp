package edu.wpi.yellowyetis;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class ServiceRequestPageController {

  // connects the scenebuilder button to a code button
  // add buttons to other scenes here
  @FXML private Button toHomePageBtn;
  @FXML private Button submitBtn;

  @FXML private MenuButton serviceMenu;
  @FXML private MenuItem maintenanceElement;
  @FXML private MenuItem laundryElement;

  // unused constructor
  public ServiceRequestPageController() {}

  // this runs once the FXML loads in to attach functions to components
  @FXML
  private void initialize() {
    // attaches a handler to the button with a lambda expression
    toHomePageBtn.setOnAction(e -> buttonClicked(e));
    submitBtn.setOnAction(e -> buttonClicked(e));
    laundryElement.setOnAction(e -> updateText(e));
    maintenanceElement.setOnAction(e -> updateText(e));
  }

  @FXML
  private void updateText(ActionEvent e) {
    if (e.getSource() == laundryElement) {
      serviceMenu.setText("Laundry");
    } else if (e.getSource() == maintenanceElement) {
      serviceMenu.setText("Maintenance");
    }
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

      } else if (e.getSource() == submitBtn) {
        if (serviceMenu.getText().equals("Laundry")) {
          stage = (Stage) toHomePageBtn.getScene().getWindow();
          stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("LaundryPage.fxml"))));
        } else if (serviceMenu.getText().equals("Maintenance")) {
          stage = (Stage) toHomePageBtn.getScene().getWindow();
          stage.setScene(
              new Scene(FXMLLoader.load(getClass().getResource("MaintenancePage.fxml"))));
        }
      } else {

      }

      // display new stage
      stage.show();
    } catch (Exception exp) {
    }
  }
}
