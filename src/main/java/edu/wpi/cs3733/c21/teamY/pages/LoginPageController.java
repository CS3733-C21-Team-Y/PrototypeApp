package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class LoginPageController extends RightPage {
  @FXML JFXTextField employeeIDTextField;
  @FXML JFXPasswordField passwordTextField;
  @FXML JFXButton loginBtn;
  @FXML JFXButton guestBtn;
  @FXML StackPane stackPane;

  @FXML
  private void initialize() {
    loginBtn.setOnAction(e -> buttonClicked(e));
  }

  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == loginBtn) {
      String tryID = employeeIDTextField.getText();
      String tryPwd = passwordTextField.getText();

      try {
        if (DataOperations.findUser(tryID, tryPwd)) {
          parent.updateProfileBtn();
          parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
          parent.loadCenterSubPage("ServiceRequestNavigator.fxml");
          parent.drawByPermissions();
        } else {
          JFXDialog errorMsg = new JFXDialog();
          errorMsg.setContent(
              new Label("Username or password not recognized" + "\n please try again"));
          errorMsg.show(stackPane);
        }
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }
    }
  }
}
