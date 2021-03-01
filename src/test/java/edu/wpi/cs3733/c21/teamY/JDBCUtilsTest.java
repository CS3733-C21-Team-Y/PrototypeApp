package edu.wpi.cs3733.c21.teamY;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.cs3733.c21.teamY.dataops.CSV;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.JDBCUtils;
import edu.wpi.cs3733.c21.teamY.entity.Employee;
import java.io.IOException;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

class JDBCUtilsTestEmployee {
  Employee employee1 = new Employee("John", "Deer", "employee1", 1, "ICU");
  Employee employee2 = new Employee("Jane", "Doe", "employee2", 3, "administration");
  Employee employee3 = new Employee("Jonathan", "Deer", "employee1", 2, "UpdatedICU");

  @Test
  public void TestInsert_Employee() throws SQLException {
    DataOperations.insert(employee1);
    DataOperations.insert(employee2);
    // visual check works -- inserts properly
  }

  @Test
  public void TestUpdate_Employee() throws SQLException {
    DataOperations.update(employee3);
    // visual check works -- updates properly
  }

  @Test
  public void TestDelete_Employee() throws SQLException {
    DataOperations.deleteEmployee(employee1);
    DataOperations.deleteEmployee(employee2.getEmployeeID());
    // visual check - works -- deletes properly
  }

  @Test
  public void TestExportListOfEmployee() throws SQLException {
    System.out.println(JDBCUtils.exportListOfEmployee().get(0).toString());
  }

  @Test
  public void TestLoadEmployeeCSVtoDB() throws IOException, SQLException {
    CSV.loadCSVtoDBEmployee();
  }

  @Test
  public void TestNewExportListOfService() throws SQLException {
    int i = JDBCUtils.exportService("", "").size();
    System.out.println(i);
  }
}
