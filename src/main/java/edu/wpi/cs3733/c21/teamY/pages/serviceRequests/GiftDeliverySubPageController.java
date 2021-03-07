package edu.wpi.cs3733.c21.teamY.pages.serviceRequests;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import edu.wpi.cs3733.c21.teamY.pages.GenericServiceFormPage;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class GiftDeliverySubPageController extends GenericServiceFormPage {

  @FXML private JFXButton clearBtn;
  @FXML private JFXButton backBtn;
  @FXML private JFXButton submitBtn;
  @FXML private JFXComboBox locationField;
  @FXML private JFXComboBox giftType;
  @FXML private JFXTextField description;
  @FXML private JFXDatePicker datePicker;

  private Settings settings;

  @FXML private StackPane stackPane;

  public GiftDeliverySubPageController() {}

  // this runs once the FXML loads in to attach functions to components
  @FXML
  private void initialize() {

    settings = Settings.getSettings();

    backBtn.setOnAction(e -> buttonClicked(e));
    submitBtn.setOnAction(e -> submitBtnClicked());
    clearBtn.setOnAction(e -> clearButton());

    locationField.getItems().add("Room 142");
    locationField.getItems().add("Room 736");
    locationField.getItems().add("Room 246");
    giftType.getItems().add("Teddy Bear");
    giftType.getItems().add("Snack Basket");
    giftType.getItems().add("Blanket");
  }

  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == backBtn) parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
  }

  private void clearButton() {
    giftType.setValue(null);
    locationField.setValue(null);
    description.setText("");
    datePicker.setValue(null);
  }

  @FXML
  private void submitBtnClicked() {
    // put code for submitting a service request here

    if (!giftType.hasProperties()) {
      nonCompleteForm(stackPane);
    }

    Service service = new Service(this.IDCount, "Gift Delivery");
    this.IDCount++;
    service.setCategory((String) giftType.getValue());
    service.setLocation((String) locationField.getValue());
    service.setDescription(description.getText());
    service.setDate(datePicker.getValue().toString());
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