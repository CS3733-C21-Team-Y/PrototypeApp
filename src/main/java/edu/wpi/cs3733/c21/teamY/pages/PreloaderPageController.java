package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXProgressBar;
import javafx.application.Preloader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PreloaderPageController extends Preloader {
  Stage stage;
  @FXML private JFXProgressBar progressBar;

  @FXML
  public void start(Stage stage) throws Exception {
    this.stage = stage;
    progressBar = new JFXProgressBar();
    Parent loadingPage = FXMLLoader.load(getClass().getResource("LoadingPage.fxml"));
    Scene scene = new Scene(loadingPage, 500, 500);
    progressBar.setStyle("-fx-accent: #26274d");
    stage.setScene(scene);
    stage.show();
  }

  @Override
  public void handleProgressNotification(ProgressNotification pn) {
    progressBar.setProgress(pn.getProgress());
  }

  @Override
  public void handleStateChangeNotification(StateChangeNotification evt) {
    if (evt.getType() == StateChangeNotification.Type.BEFORE_START) {
      stage.hide();
    }
  }
}
