package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;

public class MainPageController {
  @FXML private AnchorPane origRightPane;
  @FXML private AnchorPane origCenterPane;
  @FXML private JFXButton origSignInBtn;
  @FXML private JFXButton origNavigationBtn;
  @FXML private JFXButton origServiceRequestBtn;
  @FXML private JFXButton origAdminToolsBtn;
  @FXML private ColumnConstraints origCenterColumn;

  private JFXButton signInBtn;
  private JFXButton navigationBtn;
  private JFXButton serviceRequestBtn;
  private JFXButton adminToolsBtn;
  private AnchorPane rightPane;
  private AnchorPane centerPane;
  private ColumnConstraints centerColumn;
  //  @FXML private JFXButton SRMenuBtn;
  private static MainPageController instance;
  Settings settings;

  public MainPageController() {}

  public MainPageController(
      AnchorPane centerPane,
      AnchorPane rightPane,
      ColumnConstraints centerColumn,
      JFXButton signInBtn,
      JFXButton navigationBtn,
      JFXButton serviceRequestBtn,
      JFXButton adminToolsBtn) {
    this.settings = Settings.getSettings();
    this.centerPane = centerPane;
    this.rightPane = rightPane;
    this.centerColumn = centerColumn;
    this.signInBtn = signInBtn;
    this.navigationBtn = navigationBtn;
    this.serviceRequestBtn = serviceRequestBtn;
    this.adminToolsBtn = adminToolsBtn;
    loadRightSubPage("CovidController.fxml");
    // loadCenterSubPage("ServiceRequestNavigator.fxml");
  }

  @FXML
  private void initialize() {
    instance =
        new MainPageController(
            origCenterPane,
            origRightPane,
            origCenterColumn,
            origSignInBtn,
            origNavigationBtn,
            origServiceRequestBtn,
            origAdminToolsBtn);
    instance.setCenterColumnWidth(0);
    origNavigationBtn.setOnAction(e -> buttonClicked(e));
    origServiceRequestBtn.setOnAction(e -> buttonClicked(e));
    origAdminToolsBtn.setOnAction(e -> buttonClicked(e));
    origSignInBtn.setOnAction(e -> buttonClicked(e));
    instance.drawByPermissions();
  }

  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == navigationBtn) instance.loadRightSubPage("PathfindingPage.fxml");
    else if (e.getSource() == signInBtn) instance.loadRightSubPage("LoginPage.fxml");
    else if (e.getSource() == serviceRequestBtn) {
      instance.loadRightSubPage("ServiceRequestManagerSubPage.fxml");
      instance.loadCenterSubPage("ServiceRequestNavigator.fxml");
    } else if (e.getSource() == adminToolsBtn) instance.loadRightSubPage("AdminPage.fxml");
  }

  public void setCenterColumnWidth(double width) {
    centerColumn.setMinWidth(width);
    centerColumn.setPrefWidth(width);
    centerColumn.setMaxWidth(width);
    if (width == 0) {
      centerPane.setVisible(false);
    } else {
      centerPane.setVisible(true);
    }
  }

  public void loadRightSubPage(String fxml) {
    rightPane.getChildren().clear();
    FXMLLoader fxmlLoader = new FXMLLoader();
    try {
      Node node = fxmlLoader.load(getClass().getResource(fxml).openStream());
      RightPage controller = (RightPage) fxmlLoader.getController();
      controller.setParent(this);
      controller.loadNavigationBar();
      rightPane.getChildren().add(node);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void printWords() {
    System.out.println("Hello");
  }

  public void loadCenterSubPage(String fxml) {
    centerPane.getChildren().clear();
    FXMLLoader fxmlLoader = new FXMLLoader();
    try {
      Node node = fxmlLoader.load(getClass().getResource(fxml).openStream());
      CenterPage controller = fxmlLoader.getController();
      controller.setParent(this);
      centerPane.getChildren().add(node);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void drawByPermissions() {
    int perm = Settings.getSettings().getCurrentPermissions();
    boolean serviceAccess = false;
    boolean adminAccess = false;
    switch (perm) {
      case 0: // guest
        serviceAccess = false;
        adminAccess = false;
        break;
      case 1: // employee
        serviceAccess = true;
        adminAccess = false;
        break;
      case 2: // admin
        serviceAccess = true;
        adminAccess = true;
        break;
    }
    serviceRequestBtn.setVisible(serviceAccess);
    adminToolsBtn.setVisible(adminAccess);
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
