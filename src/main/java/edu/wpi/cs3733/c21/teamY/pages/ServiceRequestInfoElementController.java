package edu.wpi.cs3733.c21.teamY.pages;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class ServiceRequestInfoElementController {
  @FXML private Label title;
  @FXML private Label data;
  @FXML private AnchorPane annoyingVbox;

  public void populateInformation(String title, String data) {
    String newData = "";
    int count = 0;
    int numLines = 1;
    for (int i = 0; i < data.length(); i++) {
      newData += data.charAt(i);
      if (count >= 40 && data.charAt(i) == (' ')) {
        newData += "\n";
        numLines++;
        count = 0;
      }
      count++;
    }
    if (numLines == 1) {
      annoyingVbox.setPrefHeight(40);
    } else {
      annoyingVbox.setPrefHeight(numLines * 30);
    }
    this.title.setText(title);
    this.data.setText(newData);
  }
}
