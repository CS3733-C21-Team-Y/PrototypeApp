package edu.wpi.cs3733.c21.teamY;

import com.sun.javafx.application.LauncherImpl;
import edu.wpi.cs3733.c21.teamY.pages.PreloaderPageController;
import java.sql.SQLException;

public class Main {

  public static void main(String[] args) throws SQLException {
    LauncherImpl.launchApplication(App.class, PreloaderPageController.class, args);
    // App.launch(App.class, args);
  }
}
