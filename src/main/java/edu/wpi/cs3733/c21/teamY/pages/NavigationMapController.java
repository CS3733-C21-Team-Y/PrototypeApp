package edu.wpi.cs3733.c21.teamY.pages;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class NavigationMapController extends SubPage {

  @FXML private SplitPane splitPane;
  @FXML private AnchorPane splitPaneTop;

  private MapController mapInsertController;

  @FXML private Pane anchor;

  public NavigationMapController() {}

  public void initialize() {

    addMapPage();
  }

  public MapController getMapInsertController() {
    return mapInsertController;
  }

  @Override
  public void loadNavigationBar() {
    parent.setCenterColumnWidth(400);
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
