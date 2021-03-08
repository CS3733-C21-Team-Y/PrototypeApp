package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.PasswordUtils;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

public class LoginPageController extends SubPage {
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
    newAccount.setOnAction(e -> newAccountClicked(e));
  }

  private void newAccountClicked(ActionEvent e) {
    if (e.getSource() == newAccount) parent.loadRightSubPage("SignUpPage.fxml");
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

      String securePassword = DataOperations.findUserSecurePassword(tryID);
      String passwordSalt = DataOperations.findUserSalt(tryID);
      boolean passwordMatch =
          PasswordUtils.verifyUserPassword(tryPwd, securePassword, passwordSalt);

      if (securePassword.equals("none")) {
        errorMsgDisplayed = true;
        //          errorMsg.setContent(
        //              new Label("Username or password not recognized" + "\n please try again"));
        //          errorMsg.show(stackPane);
        unrecognizedUserOrPasswordPopUp(stackPane);
      }

      if (DataOperations.setUserSettings(tryID) || passwordMatch) {
        DataOperations.setUserSettings(tryID);
        submittedPopUp(stackPane);

        parent.updateProfileBtn();

        if (Settings.getSettings().getCurrentPermissions() == 3) {
          parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
          if (parent.isDesktop) {
            parent.loadCenterSubPage("ServiceRequestNavigator.fxml");
            // parent.setCenterColumnWidth(350);
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
          //          errorMsg.setContent(
          //              new Label("Username or password not recognized" + "\n please try again"));
          //          errorMsg.show(stackPane);
          unrecognizedUserOrPasswordPopUp(stackPane);
        } else {
          // errorMsg.close();
          errorMsgDisplayed = false;
        }
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    parent.drawByPermissions();
  }

  public void submittedPopUp(StackPane stackPane) {
    createPopUp(stackPane, "#5a5c94", "#ffffff", "log in successfully!");
  }

  public void unrecognizedUserOrPasswordPopUp(StackPane stackPane) {
    createPopUp(
        stackPane,
        "#ff6666",
        "#fff9f9",
        "Username or password not recognized" + "\n please try again");
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
