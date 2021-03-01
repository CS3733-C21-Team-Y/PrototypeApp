package edu.wpi.cs3733.c21.teamY.pages;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HomePageController extends GenericPage {

  // connects the scenebuilder button to a code button
  // add buttons to other scenes here
  @FXML private Button toServiceRequestBtn;
  @FXML private Button toPathfindingBtn;
  @FXML private Button toMapEditBtn;
  @FXML private Button toRequestsManager;
  @FXML private Button exitBtn;
  @FXML private Label titleLabel;

  // unused constructor
  public HomePageController() {}

  // this runs once the FXML loads in to attach functions to components
  @FXML
  private void initialize() {
    // attaches a handler to the button with a lambda expression
    toServiceRequestBtn.setOnAction(e -> buttonClicked(e));
    toMapEditBtn.setOnAction(e -> buttonClicked(e));
    toPathfindingBtn.setOnAction(e -> buttonClicked(e));
    toRequestsManager.setOnAction(e -> buttonClicked(e));
    titleLabel.setText("Mobile\nAssistant");
  }

  // button event handler
  @FXML
  private void buttonClicked(ActionEvent e) {
    // error handling for FXMLLoader.load
    try {
      // initializing stage
      Stage stage = null;

      if (e.getSource() == toServiceRequestBtn) {
        // gets the current stage
        stage = (Stage) toServiceRequestBtn.getScene().getWindow();
        // sets the new scene to the alex page
        stage.setScene(
            new Scene(FXMLLoader.load(getClass().getResource("ServiceRequestPage.fxml"))));

      } else if (e.getSource() == toPathfindingBtn) {
        // gets the current stage
        stage = (Stage) toPathfindingBtn.getScene().getWindow();
        // sets the new scene to the alex page
        stage.setScene(
            new Scene(FXMLLoader.load(getClass().getResource("NavigationSubPage.fxml"))));

      } else if (e.getSource() == toMapEditBtn) {
        // gets the current stage
        stage = (Stage) toMapEditBtn.getScene().getWindow();
        // sets the new scene to the alex page
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("nodeEdgeDisplay.fxml"))));
      } else if (e.getSource() == toRequestsManager) {
        // gets the current stage
        stage = (Stage) toRequestsManager.getScene().getWindow();
        // sets the new scene to the requests manager page
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("RequestsManager.fxml"))));
      }

      // display new stage
      assert stage != null;
      stage.show();
    } catch (Exception exp) {
      exp.printStackTrace();
    }
  }
}
