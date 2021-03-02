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
import javafx.scene.layout.StackPane;

public class AudioVisualController extends GenericServiceFormPage {

  @FXML private Button avClearBtn;
  @FXML private Button avSubmitBtn;
  @FXML private JFXComboBox avTypeComboBox;
  @FXML private JFXComboBox avLocationComboBox;
  @FXML private JFXDatePicker avDate;
  @FXML private JFXTextArea avDesc;
  @FXML private Button backBtn;

  private Settings settings;

  @FXML private StackPane stackPane;

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

    if (e.getSource() == backBtn) parent.loadRightSubPage("ServiceRequestManagerSubPage.fxml");
  }

  @FXML
  private void submitBtnClicked() {
    // put code for submitting a service request here

    String avType = (String) avTypeComboBox.getValue();
    String avLoc = (String) avLocationComboBox.getValue();
    System.out.println(avType);
    System.out.println(avLoc);
    System.out.println("hi" + avDesc.getText());
    if (avLoc.equals("") || avType.equals("") || avDesc.getText().equals("")) {
      nonCompleteForm(stackPane);
    } else {
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

      submittedPopUp(stackPane);
      avTypeComboBox.getSelectionModel().clearSelection();
      avTypeComboBox.setValue(null);
      avLocationComboBox.getSelectionModel().clearSelection();
      avLocationComboBox.setValue(null);
      avDate.setValue(null);
      avDesc.setText("");
    }
  }
}
