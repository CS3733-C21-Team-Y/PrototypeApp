package edu.wpi.cs3733.c21.teamY;

import edu.wpi.cs3733.c21.teamY.dataops.JDBCUtils;
import edu.wpi.cs3733.c21.teamY.dataops.ServiceRequestDBops;
import edu.wpi.cs3733.c21.teamY.entity.Edge;
import edu.wpi.cs3733.c21.teamY.entity.Node;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

public class Examples {

  @AfterAll
  public static void cleanDB() throws SQLException {
    JDBCUtils.deleteEdge("edge1");
    JDBCUtils.deleteNode("node1");
    JDBCUtils.deleteNode("node2");
    ServiceRequestDBops.removeService(500);
  }

  @Test
  public void testInsert()
      throws NoSuchFieldException, SQLException, IllegalAccessException, InstantiationException,
          ClassNotFoundException {

    Node node1 = new Node("walk", 800.0, 900.0, "1", "Faulkner", "TestNode1", "Test", 'y', "node1");
    Node node2 =
        new Node("walk", 900.0, 1000.0, "1", "Faulkner", "TestNode2", "Test", 'y', "node2");

    System.out.print(JDBCUtils.getConn());

    Service service1 =
        new Service(500, "testing", "description", "dummy location", "none", "low", "6", -1);

    Edge edge1 = new Edge("edge1", "node1", "node2");

    JDBCUtils.insert(9, node1, "Node");

    JDBCUtils.insert(9, node2, "Node");

    JDBCUtils.insert(3, edge1, "Edge");

    JDBCUtils.insert(8, service1, "Service");

    JDBCUtils.selectQuery("Node");
  }
}
