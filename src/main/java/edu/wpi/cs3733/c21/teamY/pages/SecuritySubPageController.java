package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SecuritySubPageController extends GenericServiceFormPage {

  @FXML private JFXButton clearBtn;
  @FXML private JFXButton backBtn;
  @FXML private JFXButton submitBtn;
  @FXML private JFXComboBox categoryComboBox;
  @FXML private JFXComboBox locationComboBox;
  @FXML private JFXComboBox urgencyComboBox;
  @FXML private JFXTimePicker serviceTimePicker;
  @FXML private JFXDatePicker serviceDatePicker;
  @FXML private JFXTextArea descriptionTextArea;

  public SecuritySubPageController() {}

  @FXML
  private void initialize() {

    backBtn.setOnAction(e -> buttonClicked(e));
    submitBtn.setOnAction(e -> submitBtnClicked());
  }

  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == backBtn) parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
  }

  @FXML
  private void submitBtnClicked() {
    // put code for submitting a service request here

    //    Service service = new Service(this.IDCount, "Security");
    //    this.IDCount++;
    //    service.setCategory((String) categoryComboBox.getValue());
    //    service.setDescription(descriptionTextArea.getText());
    //    service.setDate((String) serviceDatePicker.getValue());
    //    service.setAdditionalInfo(doctor.getText());
    //    service.setRequester(settings.getCurrentUsername());
    //
    //    try {
    //      DataOperations.saveService(service);
    //    } catch (SQLException throwables) {
    //      throwables.printStackTrace();
    //    } catch (IllegalAccessException e) {
    //      e.printStackTrace();
    //    }
  }
}
