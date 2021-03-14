package edu.wpi.cs3733.c21.teamY;

import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.JDBCUtils;
import edu.wpi.cs3733.c21.teamY.entity.Edge;
import edu.wpi.cs3733.c21.teamY.entity.Node;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

public class DatabaseOpsExamples {

  Node node1 = new Node("walk", 800.0, 900.0, "1", "Faulkner", "TestNode1", "Test", 'y', "node1");
  Node node2 = new Node("walk", 900.0, 1000.0, "1", "Faulkner", "TestNode2", "Test", 'y', "node2");
  Node node3 = new Node("walk", 900.0, 1000.0, "1", "Faulkner", "TestNode2", "Test", 'y', "node2");
  Edge edge1 = new Edge("edge1", "node1", "node2");
  Edge edge2 = new Edge("edge1", "node1", "node3");

  //  @Test
  //  public void FillDatabaseFromCSV()
  //      throws IllegalAccessException, ClassNotFoundException, IOException,
  // InstantiationException,
  //          SQLException, NoSuchFieldException {
  //    JDBCUtils.fillTablesFromCSV();
  //  }

  @Test
  public void testInsert()
      throws NoSuchFieldException, SQLException, IllegalAccessException, InstantiationException,
          ClassNotFoundException {
    //    DataOperations.initDB();
    System.out.print(JDBCUtils.getConn());
    JDBCUtils.insert(9, node1, "Node");

    JDBCUtils.insert(9, node2, "Node");

    JDBCUtils.insert(3, edge1, "Edge");
  }

  @Test
  public void TestUpdate() throws SQLException {
    Node node1Update =
        new Node("walk", 800.0, 900.0, "1", "Faulkner", "TestNode1", "WORKS", 'y', "node1");
    JDBCUtils.update(edge2);
    JDBCUtils.update(node1Update);
    DataOperations.updateNodeCoordsOnly("node1", 900.0, 1000.0);
  }

  /*
  @Test
  public void TestGenerateCSV() throws SQLException {

    CSV.generateEdgeCSV();
    CSV.DBtoCSV("NODE");
  }

  // Functional!!

  @Test
  public void TestDelete() throws SQLException {
    JDBCUtils.deleteNode(node1.nodeID);
    JDBCUtils.deleteEdge(edge1.getEdgeID());
    // JDBCUtils.deleteEdge(new Edge("fakeNode", "node1", "node2"));
  }

   */
}
