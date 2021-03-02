package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class ITSubPageController extends GenericServiceFormPage {

  @FXML private JFXButton clearBtn;
  @FXML private JFXButton backBtn;
  @FXML private JFXButton submitBtn;
  @FXML private JFXComboBox categoryComboBox;
  @FXML private JFXComboBox locationComboBox;
  @FXML private JFXComboBox affectsComboBox;
  @FXML private JFXTextArea description;

  @FXML private StackPane stackPane;

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

    if (categoryComboBox.getValue() == null
        || locationComboBox.getValue() == null
        || affectsComboBox.getValue() == null) {
      nonCompleteForm(stackPane);
    } else {

      Service service = new Service(this.IDCount, "IT Request");
      this.IDCount++;
      service.setCategory((String) categoryComboBox.getValue());
      service.setLocation((String) locationComboBox.getValue());
      service.setAdditionalInfo((String) affectsComboBox.getValue());
      service.setDescription(description.getText());
      service.setRequester(settings.getCurrentUsername());

      try {
        DataOperations.saveService(service);
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }

      submittedPopUp(stackPane);
      categoryComboBox.setValue(null);
      locationComboBox.setValue(null);
      affectsComboBox.setValue(null);
      description.setText("");
    }
  }
}
