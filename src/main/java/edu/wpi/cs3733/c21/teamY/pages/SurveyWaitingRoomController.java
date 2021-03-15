package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

public class SurveyWaitingRoomController extends SubPage {

  @FXML private Label waitLabel;

  @FXML private JFXButton refreshButton;
  @FXML private StackPane stackPane;

  @FXML
  private void initialize() {
    refreshButton.setOnAction(e -> checkStatus());
    Platform.runLater(
        () -> {
          checkStatus();
        });
  }

  private void checkStatus() {
    int status = DataOperations.checkSurveyStatus(Settings.getSettings().getCurrentUsername());
    if (status == 1 || status == 0) {
      //      parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
      //      parent.loadCenterSubPage("ServiceRequestNavigator.fxml");
      if (parent.isDesktop) {
        parent.loadRightSubPage("NavigationMap.fxml");
        parent.loadCenterSubPage("PathfindingPage.fxml");
      } else {
        parent.loadCenterSubPage("NavigationMap.fxml");
        parent.loadRightSubPage("MobilePathfindingPage.fxml");
      }
    } else if (status == -1) {
      createPopUp(stackPane, "#ff6666", "#fff9f9", "Your Response is Pending Review");
    }
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
