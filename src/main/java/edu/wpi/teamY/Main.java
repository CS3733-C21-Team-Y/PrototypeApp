package edu.wpi.teamY;

import java.sql.SQLException;

public class Main {

  public static void main(String[] args) throws SQLException {
    ActiveGraph.initialize();
    App.launch(App.class, args);
  }
}
