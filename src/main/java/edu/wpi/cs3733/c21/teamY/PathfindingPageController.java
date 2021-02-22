package edu.wpi.cs3733.c21.teamY;

import java.util.ArrayList;

import com.jfoenix.controls.JFXDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class PathfindingPageController {

  // connects the scenebuilder button to a code button
  // add buttons to other scenes here
  @FXML private Button toHomeBtn;
  @FXML private AnchorPane anchor;
  @FXML private MapController mapInsertController;
  @FXML private Button resetView;
  @FXML private StackPane stackPane;
  @FXML private ComboBox startLocationBox;
  @FXML private ComboBox endLocationBox;
  @FXML private Button toolTip;


  private ArrayList<Node> nodes = new ArrayList<Node>();
  private ArrayList<Edge> edges = new ArrayList<Edge>();
  private Graph graph;

  // unused constructor
  public PathfindingPageController() {}

  // this runs once the FXML loads in to attach functions to components
  @FXML
  private void initialize() {
    // attaches a handler to the button with a lambda expression
    toHomeBtn.setOnAction(e -> buttonClicked(e));

    anchor.setOnKeyPressed(
        e -> {
          mapInsertController.scrollOnPress(e);
          Rectangle viewWindow =
              new Rectangle(
                  0, 0, stackPane.getWidth(), mapInsertController.containerStackPane.getHeight());
          mapInsertController.containerStackPane.setClip(viewWindow);
        });
    anchor.setOnKeyReleased(e -> mapInsertController.scrollOnRelease(e));
    resetView.setOnAction(e -> mapInsertController.resetMapView());
    resetView.toFront();
    mapInsertController.containerStackPane.setOnScroll(e -> mapInsertController.zoom(e));


    JFXDialog dialog = new JFXDialog();
    dialog.setContent(
            new Label(
                    " Scroll to Zoom"
                            + "\n Hold CTRL + Scroll to Pan Up and down"
                            + "\n Hold SHIFT + Scroll to Pan left and right"
                            + "\n Reset brings back the original framing"));
    toolTip.setOnAction((action) -> dialog.show(stackPane));
    toolTip.toFront();

    mapInsertController.getFloorMenu().setText("Select A Floor");
    mapInsertController.changeImage(MapController.MAP_PAGE.PARKING);

    startLocationBox.setOnKeyPressed(
        e -> {
          if (e.getCode() == KeyCode.ENTER) {
            calculatePath();
          }
        });

    endLocationBox.setOnKeyPressed(
        e -> {
          if (e.getCode() == KeyCode.ENTER) {
            calculatePath();
          }
        });

    int i = 0;
    for (MenuItem menuItem : mapInsertController.getFloorMenu().getItems()) {
      int index = i;
      menuItem.setOnAction(
          e -> {
            mapInsertController.switchImage(e, mapInsertController.getMapOrder().get(index));

            mapInsertController.removeAllAdornerElements();
            mapInsertController.drawFromCSV(nodes, edges, mapInsertController.floorNumber);

            mapInsertController.updateMenuPreview(e, mapInsertController.getFloorMenu());
          });
      i++;
    }

    nodes = mapInsertController.loadNodesFromCSV();
    edges = mapInsertController.loadEdgesFromCSV();

    for (Node node : nodes) {
      startLocationBox.getItems().add(node.nodeID);
    }

    for (Node node : nodes) {
      endLocationBox.getItems().add(node.nodeID);
    }

    graph = new Graph(nodes, edges);
  }

  // button event handler
  @FXML
  private void buttonClicked(ActionEvent e) {
    // error handling for FXMLLoader.load
    try {
      // initializing stage
      Stage stage = null;

      if (e.getSource() == toHomeBtn) {
        // gets the current stage
        stage = (Stage) toHomeBtn.getScene().getWindow();
        // sets the new scene to the alex page
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("HomePage.fxml"))));

      } else {
      }

      // display new stage
      stage.show();
    } catch (Exception exp) {
    }
  }

  public void calculatePath() {
    if (startLocationBox.getValue() != null && endLocationBox.getValue() != null) {

      mapInsertController.clearSelection();
      ArrayList<Node> nodes =
          AStarAlgorithm.aStar(
              graph, (String) startLocationBox.getValue(), (String) endLocationBox.getValue());

      if (nodes != null) {
        for (int i = 0; i < nodes.size() - 1; i++) {
          MapController.CircleEx n =
              (MapController.CircleEx)
                  mapInsertController.getAdornerPane().getScene().lookup("#" + nodes.get(i).nodeID);
          MapController.CircleEx m =
              (MapController.CircleEx)
                  mapInsertController
                      .getAdornerPane()
                      .getScene()
                      .lookup("#" + nodes.get(i + 1).nodeID);

          if (n != null) {
            mapInsertController.selectCircle(n);
          }
          if (m != null) {
            mapInsertController.selectCircle(m);
          }

          if (n != null && m != null) {
            MapController.LineEx l =
                (MapController.LineEx)
                    mapInsertController
                        .getAdornerPane()
                        .getScene()
                        .lookup("#" + nodes.get(i).nodeID + "_" + nodes.get(i + 1).nodeID);

            if (l == null) {
              l =
                  (MapController.LineEx)
                      mapInsertController
                          .getAdornerPane()
                          .getScene()
                          .lookup("#" + nodes.get(i + 1).nodeID + "_" + nodes.get(i).nodeID);
            }

            if (l != null) mapInsertController.selectLine(l);
            else {
              System.out.println("Could not identify line");
            }
          }
        }
      }
    }
  }
}
