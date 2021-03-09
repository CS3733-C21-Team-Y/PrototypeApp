package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class NavigationMapController extends SubPage {

  @FXML private SplitPane splitPane;
  @FXML private AnchorPane splitPaneTop;

  private MapController mapInsertController;

  @FXML private Pane anchor;
  @FXML public JFXButton expandBtn;
  @FXML public FontAwesomeIconView expandIcon;

  private boolean expanded = true;

  public NavigationMapController() {}

  public void initialize() {

    expandBtn.setOnAction(e -> expandPathfinder(e));
    Platform.runLater(
        () -> {
          addMapPage();
          if (!parent.isDesktop) {
            splitPaneTop.setPadding(new Insets(0, 15, 0, 0));
          }
        });
  }

  public MapController getMapInsertController() {
    return mapInsertController;
  }

  private void expandPathfinder(ActionEvent e) {
    if (!expanded) {
      parent.animateCenterColumnWidth(400);
      expandIcon.setGlyphName("ANGLE_DOUBLE_LEFT");
    } else {
      parent.animateCenterColumnWidth(0);
      expandIcon.setGlyphName("ANGLE_DOUBLE_RIGHT");
    }
    expanded = !expanded;
  }

  @Override
  public void loadNavigationBar() {
    if (parent.isDesktop) parent.setCenterColumnWidth(0.0001);
    else parent.setCenterColumnWidth(200);
  }

  @Override
  public void drawByPlatform() {
    if (!parent.isDesktop) {
      expandBtn.setVisible(false);
    } else {
      expandBtn.setVisible(false);
    }
  }

  private void addMapPage() {
    FXMLLoader fxmlLoader = new FXMLLoader();
    try {
      Node node = fxmlLoader.load(getClass().getResource("MapUserControl.fxml").openStream());
      mapInsertController = (MapController) fxmlLoader.getController();
      mapInsertController.setParent(parent);
      // call method before page load
      splitPaneTop.getChildren().add(node);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
