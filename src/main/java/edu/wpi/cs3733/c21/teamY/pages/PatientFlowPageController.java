package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

public class PatientFlowPageController extends SubPage {

  @FXML private JFXButton ParkBtn;
  @FXML private JFXButton HospDirBtn;
  @FXML private JFXButton NavBtn;
  @FXML private JFXButton RequestBtn;
  @FXML private StackPane stackPane;

  @FXML
  private void initialize() {

    Platform.runLater(
        () -> {
          nurseCheckin();
        });

    ParkBtn.setOnAction(e -> pageButtonClicked(e));
    ParkBtn.setCursor(Cursor.HAND);
    HospDirBtn.setOnAction(e -> pageButtonClicked(e));
    HospDirBtn.setCursor(Cursor.HAND);
    NavBtn.setOnAction(e -> pageButtonClicked(e));
    NavBtn.setCursor(Cursor.HAND);
    RequestBtn.setOnAction(e -> pageButtonClicked(e));
    RequestBtn.setCursor(Cursor.HAND);
    // parent.setCenterColumnWidth(0);
  }

  private void nurseCheckin() {
    if (parent.isDesktop) {
      return;
    }
    createPopUp(
        stackPane,
        "#ff6666",
        "#fff9f9",
        "Due to pandemic, you will need to see a nurse"
            + "\n "
            + "when entering before being allowed in ");
  }

  @FXML
  private void pageButtonClicked(ActionEvent e) {

    if (e.getSource() == NavBtn) {
      if (parent.isDesktop) {
        parent.loadRightSubPage("NavigationMap.fxml");
        parent.loadCenterSubPage("PathfindingPage.fxml");
      } else {
        parent.loadCenterSubPage("NavigationMap.fxml");
        parent.loadRightSubPage("MobilePathfindingPage.fxml");
      }
    } else if (e.getSource() == RequestBtn) {
      parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
      if (parent.isDesktop) {

        parent.loadCenterSubPage("ServiceRequestNavigator.fxml");
        // parent.setCenterColumnWidth(350);
      } else {
        parent.setCenterColumnWidth(0);
      }
    } else if (e.getSource() == HospDirBtn) {
      parent.loadRightSubPage("GoogleMaps.fxml");
    } else if (e.getSource() == ParkBtn) {
      if (DataOperations.checkForCompletedCovidSurvey(
          Settings.getSettings().getCurrentUsername())) {
        parent.loadRightSubPage("SurveyWaitingRoom.fxml");
      } else {
        if (parent.isDesktop) {
          parent.loadRightSubPage("CovidScreening.fxml");
        } else if (!parent.isDesktop) {
          parent.loadRightSubPage("CovidScreeningMobile.fxml");
        }
      }
    }
  }

  private void createPopUp(
      StackPane stackPane, String backgroundColor, String textColor, String textContent) {
    JFXDialog submitted = new JFXDialog();
    //    submitted.setMaxWidth(200);
    //    submitted.setMaxHeight(300);

    Label message = new Label();
    //    JFXTextField message2=new JFXTextField();
    //    message2.setEditable(false);
    message.setStyle(
        " -fx-background-color: "
            + backgroundColor
            + "; -fx-background-radius: 6; -fx-font-size: 15; -fx-text-fill: "
            + textColor);
    message.setText(textContent);
    message.setMinHeight(100);
    message.setMinWidth(300);

    message.maxHeight(400);
    message.maxWidth(300);
    message.prefHeight(350);
    message.prefWidth(250);
    message.setAlignment(Pos.CENTER);
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
