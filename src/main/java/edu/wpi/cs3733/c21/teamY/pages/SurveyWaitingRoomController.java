package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SurveyWaitingRoomController extends SubPage {

  @FXML private Label waitLabel;

  @FXML private JFXButton refreshButton;
  @FXML private JFXButton CovidBtn;
  @FXML private StackPane stackPane;

  @FXML
  private void initialize() {
    refreshButton.setOnAction(e -> checkStatus());
    Platform.runLater(
        () -> {
          checkStatus();
        });
    CovidBtn.setOnAction(
        e -> {
          try {
            visitCovidPage();
          } catch (IOException ioException) {
            ioException.printStackTrace();
          }
        });
  }

  private void visitCovidPage() throws IOException {
    BorderPane main = FXMLLoader.load(getClass().getResource("covid.fxml"));
    // main.setPrefSize(600, 900);
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setFitToHeight(true);
    scrollPane.setFitToWidth(true);
    scrollPane.setContent(main);
    Scene covidPage = new Scene(scrollPane);

    Stage stage = new Stage();
    stage.setScene(covidPage);
    stage.getIcons().add(new Image("/edu/wpi/cs3733/c21/teamY/images/BWHLogoShield.png"));
    stage.setTitle("covid info");
    stage.initModality(Modality.WINDOW_MODAL);
    //    stage.setMaxHeight(900);
    //    stage.setMinWidth(600);
    //    stage.setMaxWidth(900);
    stage.setWidth(600);
    stage.setHeight(900);

    stage.show();
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
