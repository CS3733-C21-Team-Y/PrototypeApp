package edu.wpi.cs3733.c21.teamY.dataops;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.cs3733.c21.teamY.entity.EmployeeClearanceInfo;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class EmployeeClearanceInfoTest {

  @Test
  public void testMarkAsCleared() {
    DataOperations.markAsCleared("admin");

    ArrayList<EmployeeClearanceInfo> listRecieved = DataOperations.getClearanceList();

    ArrayList<EmployeeClearanceInfo> listExpected = new ArrayList<>();

    EmployeeClearanceInfo adminInfoExepcted =
        new EmployeeClearanceInfo("Wilson", "Wong", "admin", true);
    listExpected.add(adminInfoExepcted);
    assertEquals(adminInfoExepcted.getFirstName(), listRecieved.get(0).getFirstName());
    assertEquals(adminInfoExepcted.getLastName(), listRecieved.get(0).getLastName());
    assertEquals(adminInfoExepcted.getEmployeeID(), listRecieved.get(0).getEmployeeID());
    assertEquals(adminInfoExepcted.getClearance(), listRecieved.get(0).getClearance());
  }

  @Test
  public void testMarkAsNotCleared() {
    DataOperations.markAsNotCleared("admin");
    EmployeeClearanceInfo adminInfoExepcted =
        new EmployeeClearanceInfo("Wilson", "Wong", "admin", false);

    ArrayList<EmployeeClearanceInfo> listRecieved = DataOperations.getClearanceList();

    assertEquals(adminInfoExepcted.getFirstName(), listRecieved.get(0).getFirstName());
    assertEquals(adminInfoExepcted.getLastName(), listRecieved.get(0).getLastName());
    assertEquals(adminInfoExepcted.getEmployeeID(), listRecieved.get(0).getEmployeeID());
    assertEquals(adminInfoExepcted.getClearance(), listRecieved.get(0).getClearance());
  }
}
