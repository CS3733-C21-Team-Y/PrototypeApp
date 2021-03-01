package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AudioVisualController extends RightPage {

  @FXML private Button avClearBtn;
  @FXML private Button avSubmitBtn;
  @FXML private JFXComboBox avTypeComboBox;
  @FXML private JFXComboBox avLocationComboBox;
  @FXML private JFXDatePicker avDate;
  @FXML private JFXTextArea avDesc;

  public AudioVisualController() {}

  @FXML
  public void initialize() {
    // add combobox items
    avTypeComboBox.getItems().add("Patient TV");
    avTypeComboBox.getItems().add("Lecture Hall Setup");
    avLocationComboBox.getItems().add("RM 124");
    avLocationComboBox.getItems().add("Lecture Hall 1");

    // initialize buttons
    // avClearBtn.setOnAction(e -> serviceButtonClicked(e, "AudioVisualSubPage.fxml"));
    avSubmitBtn.setOnAction(e -> avSubmitClicked());

    // initialize text area
    avDesc = new JFXTextArea();

    // initialize date picker
    avDate = new JFXDatePicker();
  }

  private void avSubmitClicked() {}
}
