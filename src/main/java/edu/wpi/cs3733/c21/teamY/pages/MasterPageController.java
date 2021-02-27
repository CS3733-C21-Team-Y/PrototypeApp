package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;

public class MasterPageController {
  @FXML private JFXButton SRMenuBtn;

  @FXML
  private void initialize() {
    SRMenuBtn.setText("\nService\nRequest");
  }
}
