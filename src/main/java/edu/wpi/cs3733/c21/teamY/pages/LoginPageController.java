package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

public class LoginPageController extends RightPage {
  @FXML JFXTextField employeeIDTextField;
  @FXML JFXPasswordField passwordTextField;
  @FXML JFXButton loginBtn;
  @FXML JFXButton guestBtn;
  @FXML StackPane stackPane;
  @FXML JFXButton forgotPasswordBtn;
  @FXML JFXButton newAccount;

  @FXML
  private void initialize() {
    loginBtn.setOnAction(e -> buttonClicked(e));
    employeeIDTextField.setOnKeyPressed(e -> submit(e));
    passwordTextField.setOnKeyPressed(e -> submit(e));
    forgotPasswordBtn.setOnAction(e -> buttonClicked(e));
  }

  // needed outside of scope for submit()
  JFXDialog errorMsg = new JFXDialog();
  private boolean errorMsgDisplayed = false;

  private void submit(KeyEvent e) {
    if (e.getCode() == KeyCode.ENTER) {
      String tryID = employeeIDTextField.getText();
      String tryPwd = passwordTextField.getText();
      login(tryID, tryPwd);
    }
  }

  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == forgotPasswordBtn) {
      parent.loadRightSubPage("ForgotPasswordPage.fxml");
    }

    if (e.getSource() == loginBtn) {
      String tryID = employeeIDTextField.getText();
      String tryPwd = passwordTextField.getText();
      login(tryID, tryPwd);
    }
  }

  private void login(String tryID, String tryPwd) {
    try {
      if (DataOperations.findUser(tryID, tryPwd)) {
        parent.updateProfileBtn();

        if (Settings.getSettings().getCurrentPermissions() == 2) {
          parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
          if (parent.isDesktop) {
            parent.loadCenterSubPage("ServiceRequestNavigator.fxml");
            parent.setCenterColumnWidth(350);
          } else {
            parent.setCenterColumnWidth(0);
          }
          parent.drawByPermissions();
          return;
        }

        if (DataOperations.checkForCompletedCovidSurvey(
            Settings.getSettings().getCurrentUsername())) {
          parent.loadRightSubPage("SurveyWaitingRoom.fxml");
        } else {
          parent.loadRightSubPage("CovidScreening.fxml");
          // parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
          // parent.loadCenterSubPage("ServiceRequestNavigator.fxml");

        }
      } else {
        if (!errorMsgDisplayed) {
          errorMsgDisplayed = true;
          errorMsg.setContent(
              new Label("Username or password not recognized" + "\n please try again"));
          errorMsg.show(stackPane);
        } else {
          errorMsg.close();
          errorMsgDisplayed = false;
        }
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    parent.drawByPermissions();
  }
}
