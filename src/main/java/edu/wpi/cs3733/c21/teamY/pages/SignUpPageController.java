package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.PasswordUtils;
import edu.wpi.cs3733.c21.teamY.entity.Employee;
import java.sql.SQLException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

public class SignUpPageController extends SubPage {
  @FXML private Button clearBtn;
  @FXML private Button signupBtn;
  @FXML private Button exitBtn;
  @FXML private JFXTextField usernameTextField;
  @FXML private JFXPasswordField passwordTextField;
  @FXML private JFXPasswordField passwordCheck;
  @FXML private JFXTextField firstnameTextField;
  @FXML private JFXTextField lastnameTextField;
  @FXML private JFXTextField emailTextField;
  @FXML private StackPane stackPane;
  public static boolean success = false;

  public SignUpPageController() {}

  @FXML
  private void initialize() {
    exitBtn.setOnAction(e -> exitBtnClicked(e));
    exitBtn.setCursor(Cursor.HAND);
    signupBtn.setOnAction(e -> signupBtnClicked());
    signupBtn.setCursor(Cursor.HAND);
    clearBtn.setOnAction(e -> clearButtonClicked());
    clearBtn.setCursor(Cursor.HAND);
    passwordTextField
        .textProperty()
        .addListener(
            new ChangeListener<String>() {
              @Override
              public void changed(
                  ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length() > 20) {
                  passwordTextField.setText(oldValue);
                  createPopUp(
                      stackPane, "#ff6666", "#fff9f9", "20 chracters at maximum is allowed");
                }
              }
            });
    success = false;
  }

  private void exitBtnClicked(ActionEvent e) {
    if (e.getSource() == exitBtn) parent.loadRightSubPage("LoginPage.fxml");
  }

  private void clearButtonClicked() {
    usernameTextField.setText("");
    passwordTextField.setText("");
    firstnameTextField.setText("");
    lastnameTextField.setText("");
    emailTextField.setText("");
  }

  private void signupBtnClicked() {
    clearIncomplete(usernameTextField);
    clearIncomplete(passwordTextField);
    clearIncomplete(passwordCheck);
    clearIncomplete(firstnameTextField);
    clearIncomplete(lastnameTextField);
    clearIncomplete(emailTextField);
    if (usernameTextField.getText().equals("")
        || passwordTextField.getText().equals("")
        || passwordCheck.getText().equals("")
        || firstnameTextField.getText().equals("")
        || lastnameTextField.getText().equals("")
        || emailTextField.getText().equals("")) {
      if (usernameTextField.getText().equals("")) {
        incomplete(usernameTextField);
      }
      if (passwordTextField.getText().equals("")) {
        incomplete(passwordTextField);
      }
      if (passwordCheck.getText().equals("")) {
        incomplete(passwordCheck);
      }
      if (firstnameTextField.getText().equals("")) {
        incomplete(firstnameTextField);
      }
      if (lastnameTextField.getText().equals("")) {
        incomplete(lastnameTextField);
      }
      if (emailTextField.getText().equals("")) {
        incomplete(emailTextField);
      }

      nonCompleteForm(stackPane);
    } else if (passwordTextField.getText().length() < 7) {
      createPopUp(stackPane, "#ff6666", "#fff9f9", "password should have at least 7 characters");

    } else if (!passwordTextField.getText().equals(passwordCheck.getText())) {
      createPopUp(stackPane, "#ff6666", "#fff9f9", "password entered not consistent");

    } else if (!emailTextField
        .getText()
        .toUpperCase()
        .matches("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$")) {
      wrongEmailFormat(stackPane);
    } else {
      String salt = PasswordUtils.getSalt(passwordTextField.getText().length());
      Employee employee =
          new Employee(
              firstnameTextField.getText(),
              lastnameTextField.getText(),
              usernameTextField.getText(),
              PasswordUtils.generateSecurePassword(passwordTextField.getText(), salt),
              emailTextField.getText(),
              salt);
      try {
        DataOperations.createAccount(employee);
        success = true;
        parent.loadRightSubPage("LoginPage.fxml");
      } catch (SQLException throwables) {
        System.out.println("create account failed");
        throwables.printStackTrace();
      }
    }
  }

  public void clearIncomplete(JFXTextField input) {
    input.setStyle("-fx-border-color: transparent; -fx-border-radius: 10");
  }

  public void clearIncomplete(JFXPasswordField input) {
    input.setStyle("-fx-border-color: transparent; -fx-border-radius: 10");
  }

  public void incomplete(JFXTextField input) {
    input.setStyle("-fx-border-color: red; -fx-border-radius: 0");
  }

  public void incomplete(JFXPasswordField input) {
    input.setStyle("-fx-border-color: red; -fx-border-radius: 0");
  }

  public void submittedPopUp(StackPane stackPane) {
    createPopUp(stackPane, "#5a5c94", "#ffffff", "Account Created!");
  }

  public void nonCompleteForm(StackPane stackPane) {
    createPopUp(stackPane, "#ff6666", "#fff9f9", "Not all fields filled out");
  }

  public void wrongEmailFormat(StackPane stackPane) {
    createPopUp(stackPane, "#ff6666", "#fff9f9", "invalid email address");
  }

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
