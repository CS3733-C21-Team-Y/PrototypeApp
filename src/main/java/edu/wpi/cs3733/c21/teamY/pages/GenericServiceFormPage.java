package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXDialog;
import edu.wpi.cs3733.c21.teamY.dataops.CSV;
import edu.wpi.cs3733.c21.teamY.dataops.JDBCUtils;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GenericServiceFormPage extends RightPage {

  public int IDCount;

  @Override
  public void loadNavigationBar() {
    parent.setCenterColumnWidth(350);
  }

  public GenericServiceFormPage() {
    super();
    try {
      ArrayList<Service> services = JDBCUtils.exportService("", "");
      IDCount = services.size();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public void submittedPopUp(StackPane stackPane) {
    JFXDialog submitted = new JFXDialog();
    Label message = new Label();
    message.setText("Request Submitted!");
    message.setStyle(" -fx-background-color: #efeff9");
    message.setStyle(" -fx-background-radius: 10");
    message.setStyle(" -fx-font-size: 15");
    message.setStyle(" -fx-text-fill: #5a5c94");
    submitted.setContent(message);
    submitted.show(stackPane);
  }

  public void nonCompleteForm(StackPane stackPane) {
    JFXDialog submitted = new JFXDialog();
    Label message = new Label();
    message.setStyle(" -fx-background-color: #efeff9");
    message.setStyle(" -fx-background-radius: 10");
    message.setStyle(" -fx-font-size: 50");
    message.setStyle(" -fx-text-fill: #5a5c94");
    message.setText("Not all forms filled out");

    submitted.setContent(message);
    submitted.show(stackPane);
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
        // Attempts saving the service DB to a CSV file
        CSV.DBtoCSV("SERVICE");

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
