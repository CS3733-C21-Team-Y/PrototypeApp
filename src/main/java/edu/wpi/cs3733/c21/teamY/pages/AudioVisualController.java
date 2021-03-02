package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AudioVisualController extends GenericServiceFormPage {

  @FXML private Button avClearBtn;
  @FXML private Button avSubmitBtn;
  @FXML private JFXComboBox avTypeComboBox;
  @FXML private JFXComboBox avLocationComboBox;
  @FXML private JFXDatePicker avDate;
  @FXML private JFXTextArea avDesc;
  @FXML private Button backBtn;

  private Settings settings;

  public AudioVisualController() {}

  @FXML
  public void initialize() {

    settings = Settings.getSettings();
    // add combobox items
    avTypeComboBox.getItems().add("Patient TV");
    avTypeComboBox.getItems().add("Lecture Hall Setup");
    avLocationComboBox.getItems().add("RM 124");
    avLocationComboBox.getItems().add("Lecture Hall 1");

    // initialize buttons
    // avClearBtn.setOnAction(e -> serviceButtonClicked(e, "AudioVisualSubPage.fxml"));
    avSubmitBtn.setOnAction(e -> submitBtnClicked());
    backBtn.setOnAction(e -> buttonClicked(e));
  }

  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == backBtn) parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
  }

  @FXML
  private void submitBtnClicked() {
    // put code for submitting a service request here

    Service service = new Service(this.IDCount, "Audio Visual");
    this.IDCount++;
    service.setCategory((String) avTypeComboBox.getValue());
    service.setLocation((String) avLocationComboBox.getValue());
    service.setDate(avDate.getValue().toString());
    service.setDescription(avDesc.getText());
    service.setRequester(settings.getCurrentUsername());

    try {
      DataOperations.saveService(service);
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }
}
