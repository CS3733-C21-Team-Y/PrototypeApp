package edu.wpi.yellowyetis;

import java.sql.SQLException;
import java.util.ArrayList;

import edu.wpi.teamY.CSV;
import edu.wpi.teamY.Edge;
import org.junit.jupiter.api.Test;

class CSVTest {

  @Test
  public void TestGetListOfEdge() throws SQLException {
    ArrayList<Edge> edges = CSV.getListOfEdge();
    System.out.println(edges);
  }
}
