package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class ForgotPasswordPageController extends RightPage {
  @FXML JFXButton submitBtn;
  @FXML JFXButton cancelBtn;
  @FXML JFXTextField emailField;
  @FXML StackPane stackPane;
  @FXML JFXDialog errorDialog;
  @FXML Label errorLabel;

  @FXML
  private void initialize() {
    submitBtn.setOnAction(e -> buttonClicked(e));
    cancelBtn.setOnAction(e -> buttonClicked(e));
  }

  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == cancelBtn) {
      emailField.setText("");
      parent.loadRightSubPage("LoginPage.fxml");
    }

    if (e.getSource() == submitBtn) {
      if (emailField.getText().toUpperCase().matches("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$")) {
        //        System.out.println(emailField.getText());
        // if supplied email address exists in the table: Generate temp password, update DB with
        // temp password (bad practice but whatever), send email with password

        String emailExists = "false";
        try {
          emailExists = DataOperations.findUserByEmail(emailField.getText());
        } catch (SQLException throwables) {
          throwables.printStackTrace();
        }

        if (emailExists.equals("false")) {
          //          System.out.println("Something happened, here's the value: " + emailExists);
        } else {
          System.out.println(emailExists);
          // Generate Password
          // TODO: Copy from Yonghua's Captcha Generator

          // Update DB with new password
          // TODO: Need to write SQL stuff

          // Send email with temp password
          // TODO: Need to read some tutorials

        }
      } else {
        errorLabel.setText(
            "The email address you submitted is not recognized by this system. Please try again\n(Click to dismiss)");
        errorDialog.show(stackPane);
      }
      //            parent.loadRightSubPage("LoginPage.fxml");
    }
  }
}
