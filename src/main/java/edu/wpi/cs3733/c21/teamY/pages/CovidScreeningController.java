package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import edu.wpi.cs3733.c21.teamY.entity.ActiveGraph;
import edu.wpi.cs3733.c21.teamY.entity.Node;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class CovidScreeningController extends SubPage {

  @FXML private JFXCheckBox posY;
  @FXML private JFXCheckBox posN;
  @FXML private JFXCheckBox sympY;
  @FXML private JFXCheckBox sympN;
  @FXML private JFXCheckBox closeY;
  @FXML private JFXCheckBox closeN;
  @FXML private JFXButton submitBtn;
  @FXML private Label errorLabel;
  @FXML private StackPane errorStackPane;
  @FXML private JFXDialog errorDialog;
  @FXML private JFXComboBox<String> parkingBox;

  //  private Settings settings;

  public CovidScreeningController() {
    super();
  }

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

    fillComboBox();

  }

  @FXML
  private void fillComboBox() {

    parkingBox.getItems().remove(0, parkingBox.getItems().size());

    for (Node node : ActiveGraph.getNodes()) {
      if (node.getNodeType().equals("PARK")) {
        parkingBox.getItems().add(node.longName);
      }
    }
  }

  @FXML
  private void submitBtnClicked() {
    boolean isEmpty =
        (!posY.isSelected()
            && !posN.isSelected()
            && !sympY.isSelected()
            && !sympN.isSelected()
            && closeY.isSelected()
            && closeN.isSelected());
    boolean allResp =
        (posN.isSelected() || posY.isSelected())
            && (sympN.isSelected() || sympY.isSelected())
            && (closeN.isSelected() || closeY.isSelected())
            && !parkingBox.getSelectionModel().isEmpty();
    boolean isPositive = (!posN.isSelected() && posY.isSelected());
    boolean hasSymp = (!sympN.isSelected() && sympY.isSelected());
    boolean wasClose = (!closeN.isSelected() && closeY.isSelected());
    // check y/n status on each checkbox
    if (isEmpty || !allResp) {
      errorLabel.setWrapText(true);
      errorLabel.setText("Please Select an option for each question");
      errorDialog.show(errorStackPane);
    }
    //    else if (!isPositive && !hasSymp && !wasClose) {
    //      parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
    //      parent.loadCenterSubPage("ServiceRequestNavigator.fxml");
    //      // parent.loadRightSubPage("LoginPage.fxml");
    //    }
    else {
      Service service = new Service(this.IDCount, "Covid Form");
      this.IDCount++;
      service.setDescription(createDescription(isPositive, hasSymp, wasClose));
      service.setRequester(Settings.getSettings().getCurrentUsername());
      service.setLocation(parkingBox.getValue());

      try {
        DataOperations.saveParkingSpot(
            ActiveGraph.getActiveGraph().longNodes.get(parkingBox.getValue()).nodeID,
            Settings.getSettings().getCurrentUsername());
      } catch (SQLException e) {
        e.printStackTrace();
      }

      try {
        DataOperations.saveService(service);
      } catch (SQLException | IllegalAccessException e) {
        e.printStackTrace();
      }
      // parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
      // parent.loadCenterSubPage("ServiceRequestNavigator.fxml");
      //      errorLabel.setWrapText(true);
      //      errorLabel.setText("Please wait for a member of our staff to review your responses.");

      // errorDialog.show(errorStackPane);
      // errorLabel.setStyle(" -fx-background-color: #efeff9");
      // errorLabel.setStyle(" -fx-background-radius: 10");
      // errorLabel.setStyle(" -fx-font-weight: bold");
      // errorLabel.setStyle(" -fx-font-size: 30");
      // errorLabel.setStyle(" -fx-text-fill: #5a5c94");
      // errorDiolog.setContent(errorLabel);

      parent.loadRightSubPage("SurveyWaitingRoom.fxml");
    }
    //
  }

  public String createDescription(boolean isPos, boolean hasSymp, boolean wasClose) {
    StringBuilder description = new StringBuilder();
    description.append("Pos: ");
    if (isPos) {
      description.append("Yes, ");
    } else {
      description.append("No, ");
    }
    description.append("Symptoms: ");
    if (hasSymp) {
      description.append("Yes, ");
    } else {
      description.append("No, ");
    }
    description.append("Contact: ");
    if (wasClose) {
      description.append("Yes.");
    } else {
      description.append("No.");
    }
    return description.toString();
  }

  @FXML
  private void checkBoxClicked() {}
}
