package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class CovidScreeningController extends RightPage{

  @FXML private JFXCheckBox posY;
  @FXML private JFXCheckBox posN;
  @FXML private JFXCheckBox sympY;
  @FXML private JFXCheckBox sympN;
  @FXML private JFXCheckBox closeY;
  @FXML private JFXCheckBox closeN;
  @FXML private JFXButton submitBtn;

  public CovidScreeningController() {}

  private void initialize() {
    submitBtn.setOnAction(e -> submitBtnClicked());
    posY.setSelected(false);
    posN.setSelected(false);
    sympY.setSelected(false);
    sympN.setSelected(false);
    closeY.setSelected(false);
    closeN.setSelected(false);
  }

  @FXML
  private void submitBtnClicked() {
    // check y/n status on each checkbox
        if (posN.isSelected() && sympN.isSelected() && closeN.isSelected()){

        } else {
          // give instructions on how to go to covid entrance point
        }
    //

  }
}
