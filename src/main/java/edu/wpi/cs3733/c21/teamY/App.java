package edu.wpi.cs3733.c21.teamY;

import java.io.IOException;
import java.sql.SQLException;
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
    log.info("Attempting to load database from CSV file");

    try {
      ActiveGraph.initialize();
    } catch (SQLException e) {
      e.printStackTrace();
      log.info("Error initializing ActiveGraph");
    }

    try {
      ActiveGraphNoStairs.initialize();

    } catch (SQLException e) {
      e.printStackTrace();
      log.info("Error initializing ActiveGraphNoStairs");
    }

    try {
      JDBCUtils.fillTablesFromCSV();
    } catch (Exception e) {
      e.printStackTrace();
      log.info("Error loading CSV into DB");
    }
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
