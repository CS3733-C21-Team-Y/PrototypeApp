package edu.wpi.cs3733.c21.teamY.pages;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class ServiceRequestInfoElementController {
  @FXML private Label title;
  @FXML private Label data;
  @FXML private AnchorPane annoyingVbox;

  public HBox getDataHBox() {
    return dataHBox;
  }

  public Label getData() {
    return data;
  }

  @FXML private HBox dataHBox;

  public void populateInformation(String title, String data, boolean desktop) {
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

    if (desktop) {
      annoyingVbox.setPrefWidth(1200);
      annoyingVbox.setMinWidth(800);

    } else {
      annoyingVbox.setPrefWidth(5);
    }

    this.title.setText(title);
    this.data.setText(newData);
  }
}
