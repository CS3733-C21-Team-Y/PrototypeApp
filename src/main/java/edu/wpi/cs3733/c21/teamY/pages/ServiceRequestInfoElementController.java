package edu.wpi.cs3733.c21.teamY.pages;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ServiceRequestInfoElementController {
  @FXML private Label title;
  @FXML private Label data;

  public void populateInformation(String title, String data) {
    this.title.setText(title);
    this.data.setText(data);
  }
}
