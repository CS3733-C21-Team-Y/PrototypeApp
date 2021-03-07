package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import java.io.IOException;
import java.util.ArrayList;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainPageController {
  @FXML private AnchorPane origRightPane;
  @FXML private AnchorPane origCenterPane;
  @FXML private JFXButton origSignInBtn;
  @FXML private JFXButton origNavigationBtn;
  @FXML private JFXButton origServiceRequestBtn;
  @FXML private JFXButton origAdminToolsBtn;
  @FXML private JFXButton origGoogleNavBtn;
  @FXML private JFXButton exitBtn;
  //  @FXML private ScrollPane scrollPane;

  private JFXButton signInBtn;
  private JFXButton navigationBtn;
  private JFXButton serviceRequestBtn;
  private JFXButton adminToolsBtn;
  private JFXButton googleNavBtn;
  private AnchorPane rightPane;
  private AnchorPane centerPane;
  private ColumnConstraints centerColumn;
  public boolean isDesktop;
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
      JFXButton signInBtn,
      JFXButton navigationBtn,
      JFXButton serviceRequestBtn,
      JFXButton adminToolsBtn,
      JFXButton googleNavBtn) {
    this.settings = Settings.getSettings();
    this.centerPane = centerPane;
    this.rightPane = rightPane;
    this.signInBtn = signInBtn;
    this.navigationBtn = navigationBtn;
    this.serviceRequestBtn = serviceRequestBtn;
    this.adminToolsBtn = adminToolsBtn;
    this.isDesktop = true;
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
    // exitBtn.setOnMouseClicked(e -> Platform.exit());
    exitBtn.setOnAction(e -> swapPlatforms());
    origGoogleNavBtn.setOnAction(e -> buttonClicked(e));
    exitBtn.setOnMouseClicked(e -> Platform.exit());
    instance.drawByPermissions();
    // instance.drawByPlatform();

    Tooltip.install(origNavigationBtn, origNavigationBtnTooltip);
    Tooltip.install(origAdminToolsBtn, origAdminToolsTooltip);
    Tooltip.install(exitBtn, exitBtnTooltip);
    Tooltip.install(origServiceRequestBtn, origServiceRequestTooltip);
    Tooltip.install(origSignInBtn, origSignInBtnTooltip);

    //    scrollPane = new ScrollPane();
    //    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    Platform.runLater(
        () -> {
          resize();
        });
  }

  public void resize() {
    Stage stage = (Stage) origRightPane.getScene().getWindow();
    if (instance.isDesktop) {
      stage.setMaximized(true);
    } else {
      stage.setMaximized(false);
      stage.setWidth(350);
      stage.setHeight(600);
    }
  }

  public void swapPlatforms() {
    Stage stage = (Stage) origRightPane.getScene().getWindow();
    FXMLLoader fxmlLoader = new FXMLLoader();
    try {
      Scene scene;
      if (instance.isDesktop) {
        scene = new Scene(fxmlLoader.load(getClass().getResource("MobileMainPage.fxml")));
        MainPageController controller = (MainPageController) fxmlLoader.getController();
        controller.instance.isDesktop = false;
      } else {
        scene = new Scene(fxmlLoader.load(getClass().getResource("MainPage.fxml")));
        MainPageController controller = (MainPageController) fxmlLoader.getController();
        controller.instance.isDesktop = true;
      }
      stage.setScene(scene);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void updateProfileBtn() {
    this.signInBtn.setText("Welcome\n" + this.settings.getCurrentUsername());
  }

  private void buttonClicked(ActionEvent e) {

    System.out.println("clicked");
    instance.setCenterColumnWidth(0);
    if (e.getSource() == origNavigationBtn) instance.loadRightSubPage("PathfindingPage.fxml");
    else if (e.getSource() == origSignInBtn) instance.loadRightSubPage("LoginPage.fxml");
    else if (e.getSource() == origServiceRequestBtn) {
      if (instance.isDesktop) {
        instance.loadCenterSubPage("ServiceRequestNavigator.fxml");
        instance.loadRightSubPage("ServiceRequestManagerSubPage.fxml");
      } else {
        instance.loadRightSubPage("ServiceRequestManagerSubPage.fxml");
        setCenterColumnWidth(0);
      }
    } else if (e.getSource() == origAdminToolsBtn) {
      instance.loadCenterSubPage("AdminPage.fxml");
      instance.loadRightSubPage("EditNodeTable.fxml");
    } else if (e.getSource() == origGoogleNavBtn) instance.loadRightSubPage("GoogleMaps.fxml");
  }

  public void setCenterColumnWidth(double width) {
    if (isDesktop) {
      centerPane.setMinWidth(width);
      centerPane.setPrefWidth(width);
      centerPane.setMaxWidth(width);

      updateCenterPaneVisibility(width);
    } else {
      centerPane.setMinHeight(width);
      centerPane.setPrefHeight(width);
      centerPane.setMaxHeight(width);
      if (width == 0) {
        centerPane.setVisible(false);
      } else {
        centerPane.setVisible(true);
      }
    }
  }

  public void animateCenterColumnWidth(double width) {
    if (isDesktop) {

      Timeline timeline = new Timeline();

      ArrayList<KeyValue> values = new ArrayList<KeyValue>();

      KeyValue kv2 = new KeyValue(centerPane.minWidthProperty(), width, Interpolator.EASE_IN);

      // KeyFrame kf = new KeyFrame(Duration.seconds(1), kv2);
      KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv2);
      // KeyFrame kf = new KeyFrame(Duration.seconds(1), kv2);
      timeline.getKeyFrames().add(kf);
      timeline.setOnFinished(event -> updateCenterPaneVisibility(width));
      // centerPane.setMinWidth(width);
      timeline.play();
    } else {
      centerPane.setMinHeight(width);
      centerPane.setPrefHeight(width);
      centerPane.setMaxHeight(width);
      if (width == 0) {
        centerPane.setVisible(false);
      } else {
        centerPane.setVisible(true);
      }
    }
    setCenterColumnWidth(width);
  }

  public void updateCenterPaneVisibility(double width) {
    // centerPane.setPrefWidth(width);
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
      SubPage controller = (SubPage) fxmlLoader.getController();
      controller.setParent(this);
      controller.drawByPlatform();
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
      SubPage controller = fxmlLoader.getController();
      controller.setParent(this);
      controller.drawByPlatform();
      centerPane.getChildren().add(node);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void drawByPlatform() {
    if (instance.isDesktop) {
      adminToolsBtn.setVisible(true);
    } else {
      adminToolsBtn.setVisible(false);
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
