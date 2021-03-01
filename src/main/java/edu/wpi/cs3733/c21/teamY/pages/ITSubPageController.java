package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ITSubPageController extends GenericServiceFormPage {

  @FXML private JFXButton clearBtn;
  @FXML private JFXButton backBtn;
  @FXML private JFXButton submitBtn;
  @FXML private JFXComboBox categoryComboBox;
  @FXML private JFXComboBox locationComboBox;
  @FXML private JFXComboBox affectsComboBox;

  private Settings settings;

  public ITSubPageController() {}

  @FXML
  private void initialize() {

    settings = Settings.getSettings();

    backBtn.setOnAction(e -> buttonClicked(e));
    submitBtn.setOnAction(e -> submitBtnClicked());

    categoryComboBox.getItems().add("Hardware");
    categoryComboBox.getItems().add("Software");
    categoryComboBox.getItems().add("Login");
    locationComboBox.getItems().add("Nurse Station 1");
    locationComboBox.getItems().add("Nurse Station 2");
    locationComboBox.getItems().add("Admin Office");
    affectsComboBox.getItems().add("Floor Efficiency");
    affectsComboBox.getItems().add("Daily Tasks");
  }

  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == backBtn) parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
  }

  @FXML
  private void submitBtnClicked() {
    // put code for submitting a service request here

    Service service = new Service(this.IDCount, "IT Request");
    this.IDCount++;
    service.setCategory((String) categoryComboBox.getValue());
    service.setLocation((String) locationComboBox.getValue());
    service.setAdditionalInfo((String) affectsComboBox.getValue());
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
