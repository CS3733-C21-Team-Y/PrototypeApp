package edu.wpi.teamY;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App extends Application {

  Stage stage;
  Scene mainPage;

  @Override
  public void init() {
    log.info("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    stage = primaryStage;

    // makes the main fxml page into the primary page
    Parent main = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
    mainPage = new Scene(main);

    // sets the title of the window in the upper right
    primaryStage.setTitle("Brigham and Womens' Hospital");
    primaryStage.setScene(mainPage);
    primaryStage.show();
  }

  @Override
  public void stop() {
    log.info("Shutting Down");
  }
}
