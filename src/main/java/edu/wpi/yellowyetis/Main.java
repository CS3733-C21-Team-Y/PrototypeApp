package edu.wpi.yellowyetis;

import java.sql.SQLException;

public class Main {

  public static void main(String[] args) {
    ActiveGraph.initialize();
    App.launch(App.class, args);
  }
}
