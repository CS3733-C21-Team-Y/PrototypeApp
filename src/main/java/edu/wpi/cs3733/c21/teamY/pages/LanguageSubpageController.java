package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LanguageSubpageController extends GenericServiceFormPage {

  @FXML private JFXButton testBtn;
  @FXML private JFXButton backBtn;
  @FXML private JFXButton testBtn2;
  @FXML private JFXComboBox langOptions;
  @FXML private JFXComboBox urgency;
  @FXML private JFXTextField location;
  @FXML private JFXTextArea description;

  private Settings settings;

  public LanguageSubpageController() {}

  @FXML
  private void initialize() {

    settings = Settings.getSettings();

    backBtn.setOnAction(e -> buttonClicked(e));
    testBtn2.setOnAction(e -> submitBtnClicked());

    langOptions.getItems().add("Spanish");
    langOptions.getItems().add("French");
    langOptions.getItems().add("Chinese");
    langOptions.getItems().add("Portuguese");
    langOptions.getItems().add("Vietnamese");
    urgency.getItems().add("Emergency");
    urgency.getItems().add("High");
    urgency.getItems().add("Medium");
    urgency.getItems().add("Low");
  }

  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == backBtn) parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
  }

  @FXML
  private void submitBtnClicked() {
    // put code for submitting a service request here

    Service service = new Service(this.IDCount, "Language");
    this.IDCount++;
    service.setCategory((String) langOptions.getValue());
    // service.setLocation(location.getText());
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
}
