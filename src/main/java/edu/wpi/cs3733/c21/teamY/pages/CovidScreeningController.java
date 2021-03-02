package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;

public class CovidScreeningController extends RightPage {

  @FXML private JFXCheckBox posY;
  @FXML private JFXCheckBox posN;
  @FXML private JFXCheckBox sympY;
  @FXML private JFXCheckBox sympN;
  @FXML private JFXCheckBox closeY;
  @FXML private JFXCheckBox closeN;
  @FXML private JFXButton submitBtn;

  //  private Settings settings;

  public CovidScreeningController() {}

  @FXML
  private void initialize() {
    //    settings = Settings.getSettings();

    submitBtn.setOnAction(e -> submitBtnClicked());
  }

  @FXML
  private void submitBtnClicked() {

    boolean isPositive = (!posN.isSelected() && posY.isSelected());
    boolean hasSymp = (!sympN.isSelected() && sympY.isSelected());
    // check y/n status on each checkbox
    if (!isPositive && !hasSymp) {
      parent.loadRightSubPage("LoginPage.fxml");
    } else {
      // give instructions on how to go to covid entrance point
    }
    //
  }

  @FXML
  private void checkBoxClicked() {}
}
