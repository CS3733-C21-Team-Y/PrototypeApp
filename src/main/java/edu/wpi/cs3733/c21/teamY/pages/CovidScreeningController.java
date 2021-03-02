package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDialog;
import java.awt.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class CovidScreeningController extends RightPage {

  @FXML private JFXCheckBox posY;
  @FXML private JFXCheckBox posN;
  @FXML private JFXCheckBox sympY;
  @FXML private JFXCheckBox sympN;
  @FXML private JFXCheckBox closeY;
  @FXML private JFXCheckBox closeN;
  @FXML private JFXButton submitBtn;
  @FXML private StackPane errorStackPane;

  //  private Settings settings;

  public CovidScreeningController() {}

  @FXML
  private void initialize() {
    //    settings = Settings.getSettings();

    posY.setOnAction(e -> posN.setSelected(false));
    posN.setOnAction(e -> posY.setSelected(false));
    sympY.setOnAction(e -> sympN.setSelected(false));
    sympN.setOnAction(e -> sympY.setSelected(false));
    closeN.setOnAction(e -> closeY.setSelected(false));
    closeY.setOnAction(e -> closeN.setSelected(false));
    submitBtn.setOnAction(e -> submitBtnClicked());
  }

  @FXML
  private void submitBtnClicked() {

    boolean isPositive = (!posN.isSelected() && posY.isSelected());
    boolean hasSymp = (!sympN.isSelected() && sympY.isSelected());
    boolean wasClose = (!closeN.isSelected() && closeY.isSelected());
    // check y/n status on each checkbox
    if (!isPositive && !hasSymp && !wasClose) {
      parent.loadRightSubPage("LoginPage.fxml");
    } else {
      JFXDialog submitted = new JFXDialog();
      Label message = new Label();
      message.setText("YOU HAVE COVID YOU FUCK");
      message.setStyle(" -fx-background-color: #efeff9");
      message.setStyle(" -fx-background-radius: 10");
      message.setStyle(" -fx-font-weight: bold");
      message.setStyle(" -fx-font-size: 30");
      message.setStyle(" -fx-text-fill: #5a5c94");
      submitted.setContent(message);
      submitted.show(errorStackPane);
    }
    //
  }

  @FXML
  private void checkBoxClicked() {}
}
