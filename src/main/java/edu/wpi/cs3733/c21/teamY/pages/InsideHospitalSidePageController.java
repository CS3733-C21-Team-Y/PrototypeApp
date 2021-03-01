package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class InsideHospitalSidePageController extends GenericServiceFormPage {

  @FXML private JFXButton clearBtn;
  @FXML private JFXButton backBtn;
  @FXML private JFXButton submitBtn;

  public InsideHospitalSidePageController() {}

  @FXML
  private void initialize() {

    backBtn.setOnAction(e -> buttonClicked(e));
    // submitBtn.setOnAction(e -> submitBtnClicked());
  }

  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == backBtn) parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
  }
}
