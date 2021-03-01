package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ServiceRequestManagerSubpageController extends RightPage {
  @FXML private JFXButton laundryBtn;
  @FXML private JFXButton maintenanceBtn;
  @FXML private JFXButton AVBtn;
  @FXML private JFXButton floralBtn;

  @FXML
  private void initialize() {
    laundryBtn.setOnAction(e -> pageButtonClicked(e));
    maintenanceBtn.setOnAction(e -> pageButtonClicked(e));
    AVBtn.setOnAction(e -> pageButtonClicked(e));
    floralBtn.setOnAction(e -> pageButtonClicked(e));
  }

  @FXML
  private void pageButtonClicked(ActionEvent e) {

    if (e.getSource() == laundryBtn) parent.loadRightSubPage("LaundrySubPage.fxml");
    else if (e.getSource() == maintenanceBtn) parent.loadRightSubPage("MaintenanceSubPage.fxml");
    else if (e.getSource() == AVBtn) parent.loadRightSubPage("AudioVisualSubPage.fxml");
    else if (e.getSource() == floralBtn) parent.loadRightSubPage("FloralDeliverySubPage.fxml");
  }
}
