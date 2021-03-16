package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.EmailUtils;
import edu.wpi.cs3733.c21.teamY.dataops.GeneratePassword;
import edu.wpi.cs3733.c21.teamY.dataops.PasswordUtils;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

public class ForgotPasswordPageController extends SubPage {
  @FXML JFXButton submitBtn;
  @FXML JFXButton cancelBtn;
  @FXML JFXTextField emailField;
  @FXML StackPane stackPane;
  @FXML JFXDialog errorDialog;
  @FXML Label errorLabel;

  @FXML
  private void initialize() {
    submitBtn.setOnAction(e -> buttonClicked(e));
    submitBtn.setCursor(Cursor.HAND);
    cancelBtn.setOnAction(e -> buttonClicked(e));
    cancelBtn.setCursor(Cursor.HAND);
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
          String newPassword = GeneratePassword.generatePassword();
          System.out.println("Generated new password: " + newPassword);

          // Update DB with new password
          Boolean result = false;
          try {
            String salt = PasswordUtils.getSalt(newPassword.length());
            String securePass = PasswordUtils.generateSecurePassword(newPassword, salt);
            System.out.println(securePass);
            System.out.println(salt);
            result = DataOperations.updateUserPassword(securePass, emailExists);
            DataOperations.updateUserSalt(emailExists, salt);
          } catch (SQLException throwables) {
            throwables.printStackTrace();
          }
          System.out.println("We were able to update the db: " + result);

          // Send email with temp password
          if (result) {
            EmailUtils mailClient =
                new EmailUtils(
                    "passwordreset@yellowyetis.com",
                    emailField.getText(),
                    "Password Reset Request",
                    "Here is your new password for the Brigham and Womans Faulker Hospital: \n "
                        + newPassword,
                    "Email_Connection");
            mailClient.start();
            System.out.println("Should be done sending email");
            this.emailField.setText("");
            createPopUp(stackPane, "#5a5c94", "#FFFFFF", "Email Sent!");
          }
        }
      } else {
        errorLabel.setText(
            "The email address you submitted is not recognized by this system. Please try again\n(Click to dismiss)");
        errorDialog.show(stackPane);
      }
      //            parent.loadRightSubPage("LoginPage.fxml");
    }
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
}
