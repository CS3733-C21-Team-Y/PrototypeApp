package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class MedicineSubPageController extends GenericServiceFormPage {
  // connects the scenebuilder button to a code button
  // add buttons to other scenes here
  @FXML private JFXButton clearBtn;
  @FXML private JFXButton backBtn;
  @FXML private JFXButton submitBtn;
  @FXML private JFXTextField medicine;
  @FXML private JFXTextField patient;
  @FXML private JFXTextField date;
  @FXML private JFXTextField doctor;
  @FXML private StackPane stackPane;

  private Settings settings;

  // unused constructor
  public MedicineSubPageController() {}

  // this runs once the FXML loads in to attach functions to components
  @FXML
  private void initialize() {

    settings = Settings.getSettings();

    // attaches a handler to the button with a lambda expression
    backBtn.setOnAction(e -> buttonClicked(e));
    submitBtn.setOnAction(e -> submitBtnClicked());
    clearBtn.setOnAction(e -> clearButton());
  }

  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == backBtn) parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
  }

  private void clearButton() {
    patient.setText("");
    date.setText("");
    doctor.setText("");
    medicine.setText("");
  }

  @FXML
  private void submitBtnClicked() {
    // put code for submitting a service request here
    if (patient.getText().equals("")
        || date.getText().equals("")
        || doctor.getText().equals("")
        || medicine.getText().equals("")) {
      nonCompleteForm(stackPane);
    } else {

      Service service = new Service(this.IDCount, "Medicine");
      this.IDCount++;
      service.setCategory(medicine.getText());
      service.setDescription(patient.getText());
      service.setDate(date.getText());
      service.setAdditionalInfo(doctor.getText());
      service.setRequester(settings.getCurrentUsername());

      try {
        DataOperations.saveService(service);
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
      submittedPopUp(stackPane);
      clearButton();
    }
  }
}
