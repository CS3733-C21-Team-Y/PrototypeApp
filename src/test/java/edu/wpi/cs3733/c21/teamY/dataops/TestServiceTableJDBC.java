package edu.wpi.cs3733.c21.teamY.dataops;

import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

public class TestServiceTableJDBC {

  Service service1 =
      new Service(
          1,
          "Maintenance",
          "Broken door in atrium",
          "Atrium",
          "PropertyFix",
          "Very",
          " asdf",
          "{yeehaw: yeeyee}",
          0);
  Service service2 =
      new Service(
          2,
          "Maintenance",
          "Tile Removed from walkway",
          "outide",
          "PropertyFix",
          "Very",
          " asdf",
          "{yeehaw: yeeyee}",
          0);

  @Test
  public void testServiceInsert() throws SQLException, IllegalAccessException {
    DataOperations.saveService(service1);
    DataOperations.saveService(service2);
  }

  @Test
  public void testUpdateStatus() throws SQLException {
    DataOperations.updateServiceStatus(service1, 1);
  }

  //  @Test
  //  public void testExportService() throws SQLException {
  //    System.out.println(DataOperations.exportService("Maintenance", ""));
  //  }

  @Test
  public void testDeleteService() throws SQLException {
    DataOperations.removeService(service2.getServiceID());
  }
}
