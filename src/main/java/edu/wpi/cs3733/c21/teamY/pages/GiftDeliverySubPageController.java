package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class GiftDeliverySubPageController extends GenericServiceFormPage {

  @FXML private JFXButton clearBtn;
  @FXML private JFXButton backBtn;
  @FXML private JFXButton submitBtn;
  @FXML private JFXComboBox locationField;
  @FXML private JFXComboBox giftType;
  @FXML private JFXTextField description;

  private Settings settings;

  public GiftDeliverySubPageController() {}

  // this runs once the FXML loads in to attach functions to components
  @FXML
  private void initialize() {

    settings = Settings.getSettings();

    backBtn.setOnAction(e -> buttonClicked(e));
    submitBtn.setOnAction(e -> submitBtnClicked());
  }

  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == backBtn) parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
  }

  @FXML
  private void submitBtnClicked() {
    // put code for submitting a service request here

    Service service = new Service(this.IDCount, "Gift Delivery");
    this.IDCount++;
    service.setCategory((String) giftType.getValue());
    service.setLocation((String) locationField.getValue());
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
