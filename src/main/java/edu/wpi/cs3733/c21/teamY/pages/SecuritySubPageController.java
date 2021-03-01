package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SecuritySubPageController extends GenericServiceFormPage {

  @FXML private JFXButton clearBtn;
  @FXML private JFXButton backBtn;
  @FXML private JFXButton submitBtn;
  @FXML private JFXComboBox category;
  @FXML private JFXComboBox locationBox;
  @FXML private JFXComboBox urgency;
  @FXML private JFXTimePicker time;
  @FXML private JFXDatePicker datePickerObject;
  @FXML private JFXTextArea description;

  Settings settings;

  public SecuritySubPageController() {}

  @FXML
  private void initialize() {
    settings = Settings.getSettings();
    backBtn.setOnAction(e -> buttonClicked(e));
    submitBtn.setOnAction(e -> submitBtnClicked());
    category.getItems().add("Guard");
    locationBox.getItems().add("Lobby");
    locationBox.getItems().add("Radiology");
    locationBox.getItems().add("Phlebotomy");
    urgency.getItems().add("Low");
    urgency.getItems().add("Medium");
    urgency.getItems().add("High");
  }

  private void submitBtnClicked() {
    Service service = new Service(this.IDCount, "Security");
    this.IDCount++;
    service.setCategory(category.getAccessibleText());
    service.setLocation(locationBox.getAccessibleText());
    service.setUrgency(urgency.getAccessibleText());
    service.setAdditionalInfo(time.getAccessibleText());
    service.setDate(datePickerObject.getValue().toString());
    service.setDescription(description.getText());
    service.setRequester(settings.getCurrentUsername());

    try {
      DataOperations.saveService(service);
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == backBtn) parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
  }
}
