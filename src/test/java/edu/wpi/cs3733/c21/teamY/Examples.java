package edu.wpi.cs3733.c21.teamY;

import java.sql.SQLException;
import org.junit.jupiter.api.Test;

public class Examples {

  @Test
  public void testInsert()
      throws NoSuchFieldException, SQLException, IllegalAccessException, InstantiationException,
          ClassNotFoundException {

    Node node1 = new Node("walk", 800.0, 900.0, "1", "Faulkner", "TestNode1", "Test", 'y', "node1");
    Node node2 =
        new Node("walk", 900.0, 1000.0, "1", "Faulkner", "TestNode2", "Test", 'y', "node2");

    System.out.print(JDBCUtils.getConn());

    Edge edge1 = new Edge("edge1", "node1", "node2");

    JDBCUtils.insert(10, node1, "Node");

    JDBCUtils.insert(10, node2, "Node");

    JDBCUtils.insert(3, edge1, "Edge");

    JDBCUtils.selectQuery("Node");
  }
}
