package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class PatientFlowPageController extends SubPage {

  @FXML private JFXButton ParkBtn;
  @FXML private JFXButton HospDirBtn;
  @FXML private JFXButton NavBtn;
  @FXML private JFXButton RequestBtn;

  @FXML
  private void initialize() {
    ParkBtn.setOnAction(e -> pageButtonClicked(e));
    HospDirBtn.setOnAction(e -> pageButtonClicked(e));
    NavBtn.setOnAction(e -> pageButtonClicked(e));
    RequestBtn.setOnAction(e -> pageButtonClicked(e));
    // parent.setCenterColumnWidth(0);
  }

  @FXML
  private void pageButtonClicked(ActionEvent e) {

    if (e.getSource() == NavBtn) {
      parent.loadRightSubPage("NavigationSubPage.fxml");
    } else if (e.getSource() == RequestBtn) {
      parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
    } else if (e.getSource() == HospDirBtn) {
      parent.loadRightSubPage("GoogleMaps.fxml");
    } else if (e.getSource() == ParkBtn) {
      parent.loadRightSubPage("CovidScreening.fxml");
    }
  }
}
