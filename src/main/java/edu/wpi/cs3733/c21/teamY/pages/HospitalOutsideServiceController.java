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

public class HospitalOutsideServiceController extends GenericServiceFormPage {

  @FXML private JFXButton clearBtn;
  @FXML private JFXButton backBtn;
  @FXML private JFXButton submitBtn;
  @FXML private JFXTextArea descriptionTextArea;
  @FXML private JFXTextField locationTextField;
  @FXML private JFXDatePicker serviceDate;

  private Settings settings;

  public HospitalOutsideServiceController() {}

  // this runs once the FXML loads in to attach functions to components
  @FXML
  private void initialize() {
    settings = Settings.getSettings();
    backBtn.setOnAction(e -> buttonClicked(e));
    submitBtn.setOnAction(e -> submitBtnClicked());
  }

  @FXML
  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == backBtn) parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
  }

  @FXML
  private void submitBtnClicked() {
    Service service = new Service(this.IDCount, "Outside Hospital");
    this.IDCount++;
    service.setLocation(locationTextField.getText());
    service.setDate(serviceDate.getValue().toString());
    service.setDescription(descriptionTextArea.getText());
    service.setRequester(settings.getCurrentUsername());
    System.out.println("here");
    System.out.println(service);
    try {
      DataOperations.saveService(service);
      System.out.println("outside hospital save service successfully");
    } catch (IllegalAccessException | SQLException e) {
      e.printStackTrace();
    }
  }
}
