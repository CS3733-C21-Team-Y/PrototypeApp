package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.jensd.fx.glyphs.octicons.OctIconView;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;

public class ServiceRequestManagerSubpageController extends SubPage {

  @FXML private JFXButton laundryBtn;
  @FXML private JFXButton maintenanceBtn;
  @FXML private JFXButton AVBtn;
  @FXML private JFXButton floralBtn;
  @FXML private JFXButton languageBtn;
  @FXML private JFXButton giftBtn;
  @FXML private JFXButton medicineBtn;
  @FXML private JFXButton securityBtn;
  @FXML private JFXButton insideHosBtn;
  @FXML private JFXButton computerBtn;
  @FXML private JFXButton outsideHosBtn;
  @FXML private JFXButton sanitizationBtn;
  @FXML private ScrollPane scrollPane;
  @FXML private JFXButton backBtn;

  @FXML private MaterialDesignIconView laundryIcon;
  @FXML private OctIconView maintenanceIcon;
  @FXML private MaterialDesignIconView AVIcon;
  @FXML private MaterialDesignIconView floralIcon;
  @FXML private FontAwesomeIconView languageIcon;
  @FXML private OctIconView giftIcon;
  @FXML private MaterialDesignIconView medicineIcon;
  @FXML private MaterialDesignIconView securityIcon;
  @FXML private MaterialDesignIconView insideIcon;
  @FXML private MaterialDesignIconView computerIcon;
  @FXML private MaterialDesignIconView outsideIcon;
  @FXML private MaterialDesignIconView sanitationIcon;
  Scene scene;

  //
  //  MaterialDesignIconView laundryIconView = new MaterialDesignIconView(laundryIcon);
  //  OctIconView maintenanceIconView = new OctIconView(maintenanceIcon);
  //  MaterialDesignIconView avIconView = new MaterialDesignIconView(AVIcon);
  //  MaterialDesignIconView floralIconView = new MaterialDesignIconView(floralIcon);
  //  // FontAwesomeIconView languageIconView = new FontAwesomeIconView(languageIcon);
  //  OctIconView giftIconView = new OctIconView(giftIcon);
  //  MaterialDesignIconView medicineIconView = new MaterialDesignIconView(medicineIcon);
  //  MaterialDesignIconView securityIvonView = new MaterialDesignIconView(securityIcon);
  //  MaterialDesignIconView insideIconView = new MaterialDesignIconView(insideIcon);
  //  MaterialDesignIconView computerIconView = new MaterialDesignIconView(computerIcon);
  //  MaterialDesignIconView outsideIconView = new MaterialDesignIconView(outsideIcon);
  //  MaterialDesignIconView sanitationIconView = new MaterialDesignIconView(sanitationIcon);

  @FXML
  private void initialize() {
    laundryBtn.setOnAction(e -> pageButtonClicked(e));
    maintenanceBtn.setOnAction(e -> pageButtonClicked(e));
    AVBtn.setOnAction(e -> pageButtonClicked(e));
    floralBtn.setOnAction(e -> pageButtonClicked(e));
    languageBtn.setOnAction(e -> pageButtonClicked(e));
    giftBtn.setOnAction(e -> pageButtonClicked(e));
    medicineBtn.setOnAction(e -> pageButtonClicked(e));
    securityBtn.setOnAction(e -> pageButtonClicked(e));
    insideHosBtn.setOnAction(e -> pageButtonClicked(e));
    computerBtn.setOnAction(e -> pageButtonClicked(e));
    outsideHosBtn.setOnAction(e -> pageButtonClicked(e));
    sanitizationBtn.setOnAction(e -> pageButtonClicked(e));
    backBtn.setOnAction(e -> pageButtonClicked(e));

    Platform.runLater(
        () -> {
          setUpIconSize();
          scene = laundryBtn.getScene();
          scene
              .heightProperty()
              .addListener(
                  new ChangeListener<Number>() {
                    @Override
                    public void changed(
                        ObservableValue<? extends Number> observable,
                        Number oldValue,
                        Number newValue) {
                      double scale = .08 * (double) newValue;
                      laundryIcon.setSize(String.valueOf(scale));
                      maintenanceIcon.setSize(String.valueOf(scale));
                      giftIcon.setSize(String.valueOf(scale));
                      AVIcon.setSize(String.valueOf(scale));
                      floralIcon.setSize(String.valueOf(scale));
                      languageIcon.setSize(String.valueOf(scale));
                      insideIcon.setSize(String.valueOf(scale));
                      insideIcon.setSize(String.valueOf(scale));
                      insideIcon.setSize(String.valueOf(scale));
                      medicineIcon.setSize(String.valueOf(scale));
                      securityIcon.setSize(String.valueOf(scale));
                      computerIcon.setSize(String.valueOf(scale));
                      outsideIcon.setSize(String.valueOf(scale));
                      sanitationIcon.setSize(String.valueOf(scale));
                    }

                    // scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

                  });
        });
  }

  public void setUpIconSize() {

    scene = laundryBtn.getScene();
    double x = scene.getWidth();

    if (!parent.isDesktop) {
      laundryBtn.setStyle("-fx-font-size:8");
      maintenanceBtn.setStyle("-fx-font-size:8");
      AVBtn.setStyle("-fx-font-size:8");
      floralBtn.setStyle("-fx-font-size:8");
      languageBtn.setStyle("-fx-font-size:8");
      giftBtn.setStyle("-fx-font-size:8");
      medicineBtn.setStyle("-fx-font-size:8");
      securityBtn.setStyle("-fx-font-size:8");
      insideHosBtn.setStyle("-fx-font-size:8");
      sanitizationBtn.setStyle("-fx-font-size:8");
    }

    if (parent.isDesktop) {
      laundryBtn.setStyle("-fx-font-size:12");
      maintenanceBtn.setStyle("-fx-font-size:12");
      AVBtn.setStyle("-fx-font-size:12");
      floralBtn.setStyle("-fx-font-size:12");
      languageBtn.setStyle("-fx-font-size:12");
      giftBtn.setStyle("-fx-font-size:12");
      medicineBtn.setStyle("-fx-font-size:12");
      securityBtn.setStyle("-fx-font-size:12");
      insideHosBtn.setStyle("-fx-font-size:12");
      sanitizationBtn.setStyle("-fx-font-size:12");
      double scale = .05 * x;
      laundryIcon.setSize(String.valueOf(scale));
      maintenanceIcon.setSize(String.valueOf(scale));
      giftIcon.setSize(String.valueOf(scale));
      AVIcon.setSize(String.valueOf(scale));
      floralIcon.setSize(String.valueOf(scale));
      languageIcon.setSize(String.valueOf(scale));
      insideIcon.setSize(String.valueOf(scale));
      insideIcon.setSize(String.valueOf(scale));
      insideIcon.setSize(String.valueOf(scale));
      medicineIcon.setSize(String.valueOf(scale));
      securityIcon.setSize(String.valueOf(scale));
      computerIcon.setSize(String.valueOf(scale));
      outsideIcon.setSize(String.valueOf(scale));
      sanitationIcon.setSize(String.valueOf(scale));
    }
  }

  @Override
  public void drawByPlatform() {
    System.out.println(parent.isDesktop);
    if (parent.isDesktop) {
      backBtn.setVisible(false);
    }
  }

  @Override
  public void loadNavigationBar() {
    if (parent.isDesktop) parent.animateCenterColumnWidth(350);
    else parent.setCenterColumnWidth(0);
  }

  @FXML
  private void pageButtonClicked(ActionEvent e) {

    if (e.getSource() == laundryBtn) parent.loadRightSubPage("serviceRequests/LaundrySubPage.fxml");
    else if (e.getSource() == maintenanceBtn)
      parent.loadRightSubPage("serviceRequests/MaintenanceSubPage.fxml");
    else if (e.getSource() == AVBtn)
      parent.loadRightSubPage("serviceRequests/AudioVisualSubPage.fxml");
    else if (e.getSource() == floralBtn)
      parent.loadRightSubPage("serviceRequests/FloralDeliverySubpage.fxml");
    else if (e.getSource() == languageBtn)
      parent.loadRightSubPage("serviceRequests/LanguageSubPage.fxml");
    else if (e.getSource() == giftBtn)
      parent.loadRightSubPage("serviceRequests/GiftDeliverySubpage.fxml");
    else if (e.getSource() == medicineBtn)
      parent.loadRightSubPage("serviceRequests/MedicineSubPage.fxml");
    else if (e.getSource() == securityBtn)
      parent.loadRightSubPage("serviceRequests/SecuritySubPage.fxml");
    else if (e.getSource() == insideHosBtn)
      parent.loadRightSubPage("serviceRequests/InsideHospitalSubpage.fxml");
    else if (e.getSource() == computerBtn)
      parent.loadRightSubPage("serviceRequests/ITSubpage.fxml");
    else if (e.getSource() == outsideHosBtn)
      parent.loadRightSubPage("serviceRequests/OutsideHospitalSubPage.fxml");
    else if (e.getSource() == sanitizationBtn)
      parent.loadRightSubPage("serviceRequests/SanitizationSubPage.fxml");
    else if (e.getSource() == backBtn) parent.loadRightSubPage("ServiceRequestNavigator.fxml");
  }
}
