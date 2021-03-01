package edu.wpi.cs3733.c21.teamY.pages;

import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;

public class AdminPageController extends RightPage {

  @FXML private SplitPane splitPane;
  @FXML private AnchorPane splitPaneLeft;
  @FXML private AnchorPane splitPaneRight;

  @FXML private MapController mapInsertController;
  @FXML private EditNodeTableController editNodeTableController;

  public AdminPageController() {}

  public void initialize() {
    Platform.runLater(() -> addMapPage());
    Platform.runLater(() -> addTablePage());
  }

  private void addMapPage() {
    FXMLLoader fxmlLoader = new FXMLLoader();
    try {
      Node node = fxmlLoader.load(getClass().getResource("MapUserControl.fxml").openStream());
      MapController controller = (MapController) fxmlLoader.getController();
      controller.setParent(parent);
      // call method before page load
      splitPaneLeft.getChildren().add(node);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void addTablePage() {
    FXMLLoader fxmlLoader = new FXMLLoader();
    try {
      Node node = fxmlLoader.load(getClass().getResource("EditNodeTable.fxml").openStream());
      EditNodeTableController controller = (EditNodeTableController) fxmlLoader.getController();
      controller.setParent(parent);
      // call method before page load
      splitPaneRight.getChildren().add(node);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
