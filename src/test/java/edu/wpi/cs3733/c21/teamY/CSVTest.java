package edu.wpi.cs3733.c21.teamY;

import edu.wpi.cs3733.c21.teamY.dataops.CSV;
import edu.wpi.cs3733.c21.teamY.entity.Edge;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class CSVTest {

  @Test
  public void TestGetListOfEdge() throws SQLException {
    ArrayList<Edge> edges = CSV.getListOfEdge();
    System.out.println(edges);
  }
}
