package edu.wpi.cs3733.c21.teamY.pages;

import edu.wpi.cs3733.c21.teamY.entity.Service;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ServiceRequestElementController {
  @FXML private Label type;
  @FXML private Label test;
  @FXML private Label serviceID;
  @FXML private Label status;

  @FXML
  private void initialize() {}

  public void populateInformation(Service service) {
    type.setText(service.getType());
    test.setText(service.getLocation());
    serviceID.setText("ID #: " + service.getServiceID());
    if (service.getStatus() == -1) {
      status.setText("INCOMPLETE");
    } else if (service.getStatus() == 0) {
      status.setText("IN PROGRESS");
    } else if (service.getStatus() == 1) {
      status.setText("COMPLETE");
    }
  }

  public void openRequest() {}
}
