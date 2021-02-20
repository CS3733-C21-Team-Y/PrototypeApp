package edu.wpi.yellowyetis;

import edu.wpi.teamY.CSV;
import edu.wpi.teamY.Edge;
import edu.wpi.teamY.JDBCUtils;
import edu.wpi.teamY.Node;
import java.io.IOException;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

public class DatabaseOpsExamples {
  Node node1 = new Node("walk", 800.0, 900.0, "1", "Faulkner", "TestNode1", "Test", 'y', "node1");
  Node node2 = new Node("walk", 900.0, 1000.0, "1", "Faulkner", "TestNode2", "Test", 'y', "node2");
  Node node3 = new Node("walk", 900.0, 1000.0, "1", "Faulkner", "TestNode2", "Test", 'y', "node2");
  Edge edge1 = new Edge("edge1", "node1", "node2");
  Edge edge2 = new Edge("edge1", "node1", "node3");

  @Test
  public void FillDatabaseFromCSV()
      throws IllegalAccessException, ClassNotFoundException, IOException, InstantiationException,
          SQLException, NoSuchFieldException {
    JDBCUtils.fillTablesFromCSV();
  }

  // Functional!!
  @Test
  public void testInsert()
      throws NoSuchFieldException, SQLException, IllegalAccessException, InstantiationException,
          ClassNotFoundException {

    System.out.print(JDBCUtils.getConn());
    JDBCUtils.insert(9, node1, "Node");

    JDBCUtils.insert(9, node2, "Node");

    JDBCUtils.insert(3, edge1, "Edge");

    JDBCUtils.selectQuery("Node");
  }
  // FUNCTIONAL!
  @Test
  public void TestUpdate() throws SQLException {
    Node node1Update =
        new Node("walk", 800.0, 900.0, "1", "Faulkner", "TestNode1", "WORKS", 'y', "node1");
    JDBCUtils.update(edge2);
    JDBCUtils.update(node1Update);
  }

  @Test
  public void TestGenerateCSV() throws SQLException {

    CSV.generateEdgeCSV();
    CSV.DBtoCSV("NODE");
  }

  // Functional!!
  @Test
  public void TestDelete() throws SQLException {
    JDBCUtils.delete(node1);
    JDBCUtils.delete(edge1);
  }
}
