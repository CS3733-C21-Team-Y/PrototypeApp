package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.swing.*;

public class ServiceRequestManagerSubpageController {
  @FXML private JFXButton laundryBtn;
  @FXML private JFXButton maintenanceBtn;

  @FXML
  private void initialize() {
    laundryBtn.setOnAction(e -> pageButtonClicked(e));
  }

  @FXML
  private void pageButtonClicked(ActionEvent e) {
    Stage stage = (Stage) ((JFXButton) e.getSource()).getScene().getWindow();
    if (e.getSource() == laundryBtn)
      stage.setUserData(
          new StageInformation(
              ((StageInformation) stage.getUserData()).getCenterPaneFXML(), "LaundrySubPage.fxml"));
    else if (e.getSource() == maintenanceBtn)
      stage.setUserData(
          new StageInformation(
              ((StageInformation) stage.getUserData()).getCenterPaneFXML(),
              "MaintenanceSubPage.fxml"));

    try {
      stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("MaintenancePage.fxml"))));
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
    stage.show();
  }
}
