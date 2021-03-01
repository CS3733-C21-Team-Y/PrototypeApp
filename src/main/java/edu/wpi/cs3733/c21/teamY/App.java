package edu.wpi.cs3733.c21.teamY;

import edu.wpi.cs3733.c21.teamY.pages.StageInformation;
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
    //    log.info("Starting Up");
    //    log.info("Attempting to load database from CSV file");
    //
    //    try {
    //      JDBCUtils.fillTablesFromCSV();
    //      CSV.loadCSVtoDB();
    //    } catch (Exception e) {
    //      e.printStackTrace();
    //      log.info("Error loading CSV into DB");
    //    }
    //
    //    try {
    //      ActiveGraph.initialize();
    //    } catch (SQLException e) {
    //      e.printStackTrace();
    //      log.info("Error initializing ActiveGraph");
    //    }
    //
    //    try {
    //      ActiveGraphNoStairs.initialize();
    //
    //    } catch (SQLException e) {
    //      e.printStackTrace();
    //      log.info("Error initializing ActiveGraphNoStairs");
    //    }
  }

  @Override
  public void start(Stage primaryStage) throws IOException {

    stage = primaryStage;

    stage.setOnCloseRequest(
        e -> System.out.println("put stuff here to run when the x button is pressed"));

    stage.setUserData(
        new StageInformation("ServiceRequestNavigator.fxml", "ServiceRequestManagerSubpage.fxml"));
    // makes the main fxml page into the primary page
    Parent main = FXMLLoader.load(getClass().getResource("pages/MainPage.fxml"));
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
