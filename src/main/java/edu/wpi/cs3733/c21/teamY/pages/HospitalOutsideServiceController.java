package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class HospitalOutsideServiceController extends GenericServiceFormPage {

  @FXML private JFXButton clearBtn;
  @FXML private JFXButton backBtn;
  @FXML private JFXButton submitBtn;
  @FXML private JFXTextArea descriptionTextArea;
  @FXML private JFXTextField locationTextField;
  @FXML private JFXDatePicker serviceDate;

  @FXML private StackPane stackPane;

  private Settings settings;

  public HospitalOutsideServiceController() {}

  // this runs once the FXML loads in to attach functions to components
  @FXML
  private void initialize() {
    settings = Settings.getSettings();
    backBtn.setOnAction(e -> buttonClicked(e));
    submitBtn.setOnAction(e -> submitBtnClicked());
    clearBtn.setOnAction(e -> clearButton());
  }

  @FXML
  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == backBtn) parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
  }

  private void clearButton() {
    locationTextField.setText("");
    serviceDate.setValue(null);
    descriptionTextArea.setText("");
  }

  @FXML
  private void submitBtnClicked() {

    if (locationTextField.getText().equals("")
        || descriptionTextArea.getText().equals("")
        || serviceDate.getValue() == null) {
      nonCompleteForm(stackPane);
    } else {
      Service service = new Service(this.IDCount, "Outside Hospital");
      this.IDCount++;
      service.setLocation(locationTextField.getText());
      service.setDate(serviceDate.getValue().toString());
      service.setDescription(descriptionTextArea.getText());
      service.setRequester(settings.getCurrentUsername());

      try {
        DataOperations.saveService(service);
      } catch (IllegalAccessException | SQLException e) {
        e.printStackTrace();
      }

      submittedPopUp(stackPane);
      locationTextField.setText("");
      serviceDate.setValue(null);
      descriptionTextArea.setText("");
    }
  }
}
