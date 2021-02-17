package edu.wpi.yellowyetis;

import java.sql.SQLException;

public class Main {

  public static void main(String[] args) throws SQLException {
    CSV.generateEdgeCSV();
    // CSV.SQLexportCSV();
    // App.launch(App.class, args);
  }
}
