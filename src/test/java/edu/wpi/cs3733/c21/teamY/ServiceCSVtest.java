package edu.wpi.cs3733.c21.teamY;

import java.io.IOException;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

public class ServiceCSVtest {
  Service service = new Service(1, "Laundry");
  Service service1 = new Service(1, "Laundry", "wash clothe", "office", "", "urgent", "1999/5");

  @Test
  public void testSaveServiceToCSV() {

    ServiceCSV.saveServiceToCSV(service);
    ServiceCSV.saveServiceToCSV(service1);
  }

  @Test
  public void testLoadCSVtoDB() throws IOException, SQLException {
    ServiceCSV.loadCSVtoDB();
  }
}
