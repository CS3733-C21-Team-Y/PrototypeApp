package edu.wpi.cs3733.c21.teamY;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ServiceRequestInfoPageController {

  @FXML private Button completeRequest;
  @FXML private Label locationInfo;
  @FXML private Label dateInfo;
  @FXML private Label requestInfo;

  public ServiceRequestInfoPageController() {}

  @FXML
  private void initialize() {
    completeRequest.setOnAction(e -> buttonClicked(e));
  }

  @FXML
  public void buttonClicked(ActionEvent e) {
    if (e.getSource() == completeRequest) {}
  }
}
