package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;

public class MainPageController {
  @FXML private AnchorPane origRightPane;
  @FXML private AnchorPane origCenterPane;
  @FXML private JFXButton origSignInBtn;
  @FXML private JFXButton origNavigationBtn;
  @FXML private JFXButton origServiceRequestBtn;
  @FXML private JFXButton origAdminToolsBtn;
  @FXML private JFXButton origGoogleNavBtn;
  @FXML private ColumnConstraints origCenterColumn;
  @FXML private FontAwesomeIconView exitBtn;
  @FXML private ScrollPane scrollPane;

  private JFXButton signInBtn;
  private JFXButton navigationBtn;
  private JFXButton serviceRequestBtn;
  private JFXButton adminToolsBtn;
  private JFXButton googleNavBtn;
  private AnchorPane rightPane;
  private AnchorPane centerPane;
  private ColumnConstraints centerColumn;
  //  @FXML private JFXButton SRMenuBtn;
  private static MainPageController instance;
  Settings settings;

  // tooltip Initializations
  Tooltip origSignInBtnTooltip = new Tooltip("Navigate to Login Page");
  Tooltip origNavigationBtnTooltip = new Tooltip("Navigate to Navigation Page");
  Tooltip origServiceRequestTooltip = new Tooltip("Navigate to Request Page");
  Tooltip origAdminToolsTooltip = new Tooltip("Navigate to Admin Page");
  Tooltip exitBtnTooltip = new Tooltip("Exit Application");

  public MainPageController() {}

  public MainPageController(
      AnchorPane centerPane,
      AnchorPane rightPane,
      ColumnConstraints centerColumn,
      JFXButton signInBtn,
      JFXButton navigationBtn,
      JFXButton serviceRequestBtn,
      JFXButton adminToolsBtn,
      JFXButton googleNavBtn) {
    this.settings = Settings.getSettings();
    this.centerPane = centerPane;
    this.rightPane = rightPane;
    this.centerColumn = centerColumn;
    this.signInBtn = signInBtn;
    this.navigationBtn = navigationBtn;
    this.serviceRequestBtn = serviceRequestBtn;
    this.adminToolsBtn = adminToolsBtn;
    this.googleNavBtn = googleNavBtn;

    loadRightSubPage("LandingPage.fxml");

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
            origAdminToolsBtn,
            origGoogleNavBtn);
    instance.setCenterColumnWidth(0);
    origNavigationBtn.setOnAction(e -> buttonClicked(e));
    origServiceRequestBtn.setOnAction(e -> buttonClicked(e));
    origAdminToolsBtn.setOnAction(e -> buttonClicked(e));
    origSignInBtn.setOnAction(e -> buttonClicked(e));
    origGoogleNavBtn.setOnAction(e -> buttonClicked(e));
    exitBtn.setOnMouseClicked(e -> Platform.exit());
    instance.drawByPermissions();

    Tooltip.install(origNavigationBtn, origNavigationBtnTooltip);
    Tooltip.install(origAdminToolsBtn, origAdminToolsTooltip);
    Tooltip.install(exitBtn, exitBtnTooltip);
    Tooltip.install(origServiceRequestBtn, origServiceRequestTooltip);
    Tooltip.install(origSignInBtn, origSignInBtnTooltip);

    scrollPane = new ScrollPane();
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
  }

  public void updateProfileBtn() {
    this.signInBtn.setText("Welcome\n" + this.settings.getCurrentUsername());
  }

  private void buttonClicked(ActionEvent e) {

    System.out.println("clicked");
    if (e.getSource() == origNavigationBtn) instance.loadRightSubPage("PathfindingPage.fxml");
    else if (e.getSource() == origSignInBtn) instance.loadRightSubPage("LoginPage.fxml");
    else if (e.getSource() == origServiceRequestBtn) {
      instance.loadRightSubPage("ServiceRequestManagerSubPage.fxml");
      instance.loadCenterSubPage("ServiceRequestNavigator.fxml");
    } else if (e.getSource() == origAdminToolsBtn) instance.loadRightSubPage("AdminPage.fxml");
    else if (e.getSource() == origGoogleNavBtn) instance.loadRightSubPage("GoogleMaps.fxml");
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
