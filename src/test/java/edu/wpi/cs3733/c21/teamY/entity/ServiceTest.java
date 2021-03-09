package edu.wpi.cs3733.c21.teamY.entity;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import java.sql.SQLException;
import org.junit.After;

class ServiceTest {

  Employee employee =
      new Employee(
          "employee1", "adsfasdf", "employeeID", "dfasdf", "asdfasd", 0, "asdf", "asdfsadf");
  Service service =
      new Service(
          12,
          "adfasdf",
          "asdfasdf",
          "asdfasdf",
          "asdfasdf",
          "asdfasdf",
          "asdfasdf",
          "asdfasdfa",
          "employeeID",
          0);
  Service service2 =
      new Service(
          12,
          "adfasdf",
          "asdfasdf",
          "asdfasdf",
          "asdfasdf",
          "asdfasdf",
          "asdfasdf",
          "asdfasdfa",
          "notvalid",
          0);

  //  @Test
  //  public void testInsertService() throws SQLException, IllegalAccessException {
  //    DataOperations.insert(employee);
  //    DataOperations.saveService(service);
  //    DataOperations.saveService(service2);
  //    DataOperations.removeService(service.getServiceID());
  //    DataOperations.deleteEmployee(employee);
  //  }

  @After
  public void deleteEntries() throws SQLException {
    DataOperations.deleteEmployee(employee);
  }
}
