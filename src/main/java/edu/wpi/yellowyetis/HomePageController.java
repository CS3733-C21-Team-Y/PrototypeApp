package edu.wpi.yellowyetis;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class HomePageController {

  // connects the scenebuilder button to a code button
  // add buttons to other scenes here
  @FXML private Button toServiceRequestBtn;
  @FXML private Button toPathfindingBtn;
  @FXML private Button toMapEditBtn;

  // unused constructor
  public HomePageController() {}

  // this runs once the FXML loads in to attach functions to components
  @FXML
  private void initialize() {
    // attaches a handler to the button with a lambda expression
    toServiceRequestBtn.setOnAction(e -> buttonClicked(e));
    toMapEditBtn.setOnAction(e -> buttonClicked(e));
    toPathfindingBtn.setOnAction(e -> buttonClicked(e));

    toMapEditBtn.setOnMouseEntered(e -> enterButtonColor(e));
    toMapEditBtn.setOnMouseExited(e -> exitButtonColor(e));
    toServiceRequestBtn.setOnMouseEntered(e -> enterButtonColor(e));
    toServiceRequestBtn.setOnMouseExited(e -> exitButtonColor(e));
    toPathfindingBtn.setOnMouseEntered(e -> enterButtonColor(e));
    toPathfindingBtn.setOnMouseExited(e -> exitButtonColor(e));
  }

  private void enterButtonColor(MouseEvent e) {
    ((Button) e.getSource())
        .setStyle(
            "-fx-background-color: #656565; -fx-font-family: arial; -fx-font-weight: bold; -fx-text-fill: #d8d8d8;");
  }

  private void exitButtonColor(MouseEvent e) {

    ((Button) e.getSource())
        .setStyle(
            "-fx-background-color: #d8d8d8; -fx-font-family: arial; -fx-font-weight: bold; -fx-text-fill: #656565;");
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
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("MapBase.fxml"))));

      } else if (e.getSource() == toMapEditBtn) {
        // gets the current stage
        stage = (Stage) toMapEditBtn.getScene().getWindow();
        // sets the new scene to the alex page
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("nodeEdgeDisplay.fxml"))));

      } else {

      }

      // display new stage
      stage.show();
    } catch (Exception exp) {
      exp.printStackTrace();
    }
  }
}