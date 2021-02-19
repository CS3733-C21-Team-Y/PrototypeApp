package edu.wpi.yellowyetis;

import static org.junit.jupiter.api.Assertions.*;

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
