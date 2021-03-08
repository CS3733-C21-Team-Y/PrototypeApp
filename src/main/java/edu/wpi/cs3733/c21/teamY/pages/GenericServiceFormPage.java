package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.c21.teamY.dataops.CSV;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class GenericServiceFormPage extends SubPage {

  @Override
  public void loadNavigationBar() {
    parent.setCenterColumnWidth(350);
  }

  public GenericServiceFormPage() {
    super();
  }

  public void submittedPopUp(StackPane stackPane) {
    createPopUp(stackPane, "#5a5c94", "#ffffff", "Request Submitted!");
  }

  public void clearIncomplete(JFXTextArea input) {
    input.setStyle("-fx-border-color: transparent; -fx-border-radius: 10");
  }

  public void clearIncomplete(JFXTextField input) {
    input.setStyle("-fx-border-color: transparent; -fx-border-radius: 10");
  }

  public void clearIncomplete(JFXComboBox input) {
    input.setStyle("-fx-border-color: transparent; -fx-border-radius: 10");
  }

  public void clearIncomplete(JFXDatePicker input) {
    input.setStyle("-fx-border-color: transparent; -fx-border-radius: 10");
  }

  public void clearIncomplete(JFXTimePicker input) {
    input.setStyle("-fx-border-color: transparent; -fx-border-radius: 10");
  }

  public void incomplete(JFXTextArea input) {
    input.setStyle("-fx-border-color: red; -fx-border-radius: 0");
  }

  public void incomplete(JFXTextField input) {
    input.setStyle("-fx-border-color: red; -fx-border-radius: 0");
  }

  public void incomplete(JFXComboBox input) {
    input.setStyle("-fx-border-color: red; -fx-border-radius: 0; -fx-text-fill: white");
  }

  public void incomplete(JFXDatePicker input) {
    input.setStyle("-fx-border-color: red; -fx-border-radius: 0");
  }

  public void incomplete(JFXTimePicker input) {
    input.setStyle("-fx-border-color: red; -fx-border-radius: 0");
  }

  public void nonCompleteForm(StackPane stackPane) {
    createPopUp(stackPane, "#ff6666", "#fff9f9", "Not all fields filled out");
  }

  /**
   * Creates a generic popup that is dismissed by clicking on the screen
   *
   * @param stackPane
   * @param backgroundColor
   * @param textColor
   * @param textContent
   */
  private void createPopUp(
      StackPane stackPane, String backgroundColor, String textColor, String textContent) {
    JFXDialog submitted = new JFXDialog();

    Label message = new Label();
    message.setStyle(
        " -fx-background-color: "
            + backgroundColor
            + "; -fx-background-radius: 6; -fx-font-size: 25; -fx-text-fill: "
            + textColor);
    message.setText(textContent);
    message.maxHeight(70);
    message.maxWidth(300);
    message.prefHeight(70);
    message.prefWidth(250);
    Insets myInset = new Insets(10);
    message.setPadding(myInset);
    BorderStroke myStroke =
        new BorderStroke(
            Paint.valueOf(backgroundColor),
            new BorderStrokeStyle(null, null, null, 6, 1, null),
            new CornerRadii(6),
            new BorderWidths(3));
    Border myB = new Border(myStroke);
    message.setBorder(myB);

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
