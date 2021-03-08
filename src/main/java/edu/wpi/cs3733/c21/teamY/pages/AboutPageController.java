package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AboutPageController extends SubPage {
  @FXML private JFXButton backBtn;

  @FXML
  void initialize() {
    backBtn.setOnAction(e -> backBtnClicked(e));
  }

  private void backBtnClicked(ActionEvent e) {
    if (e.getSource() == backBtn) parent.loadRightSubPage("NavigationSubPage.fxml");
  }
}
