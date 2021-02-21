package edu.wpi.teamY;

import java.io.IOException;
import java.sql.SQLException;

public class Main {

  public static void main(String[] args)
      throws SQLException, ClassNotFoundException, NoSuchFieldException, InstantiationException,
          IllegalAccessException, IOException {
    ActiveGraph.initialize();
    App.launch(App.class, args);
  }
}
