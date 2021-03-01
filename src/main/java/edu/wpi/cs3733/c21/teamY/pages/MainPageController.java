package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class MainPageController {
  @FXML private AnchorPane origRightPane;
  @FXML private AnchorPane origCenterPane;
  @FXML private JFXButton signInBtn;
  @FXML private JFXButton navigationBtn;
  @FXML private JFXButton serviceRequestBtn;
  @FXML private JFXButton adminToolsBtn;

  private AnchorPane rightPane;
  private AnchorPane centerPane;
  //  @FXML private JFXButton SRMenuBtn;
  private static MainPageController instance;

  public MainPageController() {}

  public MainPageController(AnchorPane centerPane, AnchorPane rightPane) {
    this.centerPane = centerPane;
    this.rightPane = rightPane;
    loadRightSubPage("LoginPage.fxml");
    loadCenterSubPage("ServiceRequestNavigator.fxml");
  }

  @FXML
  private void initialize() {
    instance = new MainPageController(origCenterPane, origRightPane);
    navigationBtn.setOnAction(e -> buttonClicked(e));
    serviceRequestBtn.setOnAction(e -> buttonClicked(e));
    adminToolsBtn.setOnAction(e -> buttonClicked(e));
    signInBtn.setOnAction(e -> buttonClicked(e));
  }

  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == navigationBtn) instance.loadRightSubPage("PathfindingPage.fxml");
    else if (e.getSource() == signInBtn) instance.loadRightSubPage("LoginPage.fxml");
    else if (e.getSource() == serviceRequestBtn)
      instance.loadRightSubPage("ServiceRequestManagerSubPage.fxml");
    else if (e.getSource() == adminToolsBtn) instance.loadRightSubPage("nodeEdgeDisplay.fxml");
  }

  public void loadRightSubPage(String fxml) {
    rightPane.getChildren().clear();
    FXMLLoader fxmlLoader = new FXMLLoader();
    try {
      Node node = fxmlLoader.load(getClass().getResource(fxml).openStream());
      RightPage controller = (RightPage) fxmlLoader.getController();
      controller.setParent(this);
      rightPane.getChildren().add(node);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void loadCenterSubPage(String fxml) {
    centerPane.getChildren().clear();
    FXMLLoader fxmlLoader = new FXMLLoader();
    try {
      Node node = fxmlLoader.load(getClass().getResource(fxml).openStream());
      ServiceRequestNavigatorController controller = fxmlLoader.getController();
      // controller.setParent(instance);
      centerPane.getChildren().add(node);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  //  public void loadSubPages() {
  //    Stage stage = (Stage) rightPane.getScene().getWindow();
  //    StageInformation info = (StageInformation) stage.getUserData();
  //    stage.setWidth(info.getWidth());
  //    stage.setHeight(info.getHeight());
  //
  //    centerPane.getChildren().clear();
  //    rightPane.getChildren().clear();
  //    try {
  //
  //
  //      Node centerNode = (Node)
  // FXMLLoader.load(getClass().getResource(info.getCenterPaneFXML()));
  //
  //      centerPane.getChildren().add(centerNode);
  //      rightPane
  //          .getChildren()
  //          .add((Node) FXMLLoader.load(getClass().getResource(info.getRightPaneFXML())));
  //    } catch (IOException e) {
  //      e.printStackTrace();
  //    }
  //  }
}
