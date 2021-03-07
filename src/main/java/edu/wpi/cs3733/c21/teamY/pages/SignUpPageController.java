package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.entity.Employee;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class SignUpPageController extends SubPage {
  @FXML private Button clearBtn;
  @FXML private Button signupBtn;
  @FXML private Button exitBtn;
  @FXML private JFXTextField usernameTextField;
  @FXML private JFXTextField passwordTextField;
  @FXML private JFXTextField firstnameTextField;
  @FXML private JFXTextField lastnameTextField;
  @FXML private JFXTextField emailTextField;
  @FXML private StackPane stackPane;

  public SignUpPageController() {}

  @FXML
  private void initialize() {
    exitBtn.setOnAction(e -> exitBtnClicked(e));
    signupBtn.setOnAction(e -> signupBtnClicked());
    clearBtn.setOnAction(e -> clearButtonClicked());
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
    JFXDialog signup = new JFXDialog();
    Label errorMessage = new Label();
    errorMessage.setStyle(" -fx-background-color: #efeff9");
    errorMessage.setStyle(" -fx-background-radius: 10");
    errorMessage.setStyle(" -fx-font-size: 50");
    errorMessage.setStyle(" -fx-text-fill: #5a5c94");
    if (usernameTextField.getText().equals("")
        || passwordTextField.getText().equals("")
        || firstnameTextField.getText().equals("")
        || lastnameTextField.getText().equals("")
        || emailTextField.getText().equals("")) {

      errorMessage.setText("Not all forms filled out");

      signup.setContent(errorMessage);
      signup.show(stackPane);
    } else if (!emailTextField
        .getText()
        .toUpperCase()
        .matches("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$")) {
      errorMessage.setText(
          "The email address you submitted is not recognized by this system. Please try again\n(Click to dismiss)");
      signup.setContent(errorMessage);
      signup.show(stackPane);
    } else {
      Employee employee =
          new Employee(
              firstnameTextField.getText(),
              lastnameTextField.getText(),
              usernameTextField.getText(),
              passwordTextField.getText(),
              emailTextField.getText());
      try {
        DataOperations.createAccount(employee);
      } catch (SQLException throwables) {
        System.out.println("create account failed");
        throwables.printStackTrace();
      }
      errorMessage.setText("account created successfully!");
      signup.setContent(errorMessage);
      signup.show(stackPane);
    }
  }
}
