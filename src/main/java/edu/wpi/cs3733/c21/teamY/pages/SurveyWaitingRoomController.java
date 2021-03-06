package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SurveyWaitingRoomController extends RightPage {

  @FXML private Label waitLabel;

  @FXML private JFXButton refreshButton;

  @FXML
  private void initialize() {
    refreshButton.setOnAction(e -> checkStatus());
  }

  private void checkStatus() {
    if (DataOperations.checkServiceStatus(Settings.getSettings().getCurrentUsername()) == 1) {
      parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
      parent.loadCenterSubPage("ServiceRequestNavigator.fxml");
      parent.drawByPermissions();
    }
  }
}
