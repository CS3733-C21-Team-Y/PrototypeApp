package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;

public class LoginPageController extends RightPage {
  @FXML JFXTextField employeeIDTextField;
  @FXML JFXPasswordField passwordTextField;
  @FXML JFXButton loginBtn;
  @FXML JFXButton guestBtn;

  @FXML
  private void initialize() {
    loginBtn.setOnAction(e -> buttonClicked(e));
  }

  private void buttonClicked(ActionEvent e) {
      if(e.getSource() == loginBtn) {
        String tryID = employeeIDTextField.getText();
        String tryPwd = passwordTextField.getText();

        //login???
      }
  }
}
