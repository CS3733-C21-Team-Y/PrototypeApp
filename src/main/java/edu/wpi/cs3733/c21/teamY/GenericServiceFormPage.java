package edu.wpi.cs3733.c21.teamY;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class GenericServiceFormPage extends GenericPage {

  public GenericServiceFormPage() {
    super();
  }

  // button event handler
  @FXML
  protected void serviceButtonClicked(ActionEvent e, String fxmlFile) {
    // error handling for FXMLLoader.load
    try {
      // initializing stage
      Stage stage = null;
      stage = (Stage) ((Button) e.getSource()).getScene().getWindow();
      if (((Button) e.getSource()).getText().equals("Back")
          || ((Button) e.getSource()).getText().equals("Cancel")) {
        // sets the new scene to the alex page
        stage.setScene(
            new Scene(FXMLLoader.load(getClass().getResource("ServiceRequestPage.fxml"))));
      } else if (((Button) e.getSource()).getText().equals("Clear")) {
        // sets the new scene to itself, effectively clearing it
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource(fxmlFile))));
      }

      // display new stage
      stage.show();
    } catch (Exception exp) {
    }
  }
}
