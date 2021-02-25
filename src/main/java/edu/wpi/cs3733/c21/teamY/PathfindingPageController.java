package edu.wpi.cs3733.c21.teamY;

import com.jfoenix.controls.JFXDialog;
import edu.wpi.cs3733.c21.teamY.algorithms.AStarAlgorithm;
import edu.wpi.cs3733.c21.teamY.algorithms.DijkstrasAlgorithm;
import edu.wpi.cs3733.c21.teamY.entity.*;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
  @FXML private CheckBox bathroomCheck;
  @FXML private CheckBox cafeCheck;
  @FXML private CheckBox kioskCheck;
  @FXML private CheckBox noStairsCheckBox;

  @FXML private Slider zoomSlider;
  @FXML private Button upButton;
  @FXML private Button downButton;
  @FXML private Button leftButton;
  @FXML private Button rightButton;
  @FXML private Button zoomInButton;
  @FXML private Button zoomOutButton;
  @FXML private Label zoomLabel;

  private ArrayList<Node> nodes = new ArrayList<Node>();
  private ArrayList<Edge> edges = new ArrayList<Edge>();
  private Graph graph;

  private boolean pathActive = false;

  // unused constructor
  public PathfindingPageController() {}

  // this runs once the FXML loads in to attach functions to components
  @FXML
  private void initialize() {
    // attaches a handler to the button with a lambda expression
    toHomeBtn.setOnAction(e -> buttonClicked(e));

    // scroll
    anchor.setOnKeyPressed(
        e -> {
          mapInsertController.scrollOnPress(e);
          Rectangle viewWindow =
              new Rectangle(
                  0, 0, stackPane.getWidth(), mapInsertController.containerStackPane.getHeight());
          mapInsertController.containerStackPane.setClip(viewWindow);
        });
    anchor.setOnKeyReleased(e -> mapInsertController.scrollOnRelease(e));
    mapInsertController.containerStackPane.setOnScroll(e -> mapInsertController.zoom(e));

    // Reset view button
    resetView.setOnAction(e -> mapInsertController.resetMapView());
    resetView.toFront();

    // Set the starting image early because otherwise it will flash default
    mapInsertController.changeMapImage(MapController.MAP_PAGE.PARKING);

    // Tooltip box
    JFXDialog dialog = new JFXDialog();
    dialog.setContent(
        new Label(
            " Scroll to Zoom"
                + "\n Hold CTRL + Scroll to Pan Up and down"
                + "\n Hold SHIFT + Scroll to Pan left and right"
                + "\n Reset brings back the original framing"));
    toolTip.setOnAction((action) -> dialog.show(stackPane));
    toolTip.toFront();

    // Node selection menus Keys
    startLocationBox.setOnKeyPressed(
        e -> {
          if (e.getCode() == KeyCode.ENTER) {
            calculatePath();
          }
        });

    startLocationBox
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (options, oldValue, newValue) -> {
              if ((oldValue == null && newValue != null) || (!oldValue.equals(newValue))) {
                calculatePath();
              } else {
                pathActive = false;
              }
            });

    endLocationBox.setOnKeyPressed(
        e -> {
          if (e.getCode() == KeyCode.ENTER) {
            calculatePath();
          }
        });

    endLocationBox
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (options, oldValue, newValue) -> {
              if ((oldValue == null && newValue != null) || (!oldValue.equals(newValue))) {
                calculatePath();
              } else {
                pathActive = false;
              }
            });

    bathroomCheck
        .selectedProperty()
        .addListener(
            (options, oldValue, newValue) -> {
              calculatePath();
            });

    cafeCheck
        .selectedProperty()
        .addListener(
            (options, oldValue, newValue) -> {
              calculatePath();
            });

    kioskCheck
        .selectedProperty()
        .addListener(
            (options, oldValue, newValue) -> {
              calculatePath();
            });

    noStairsCheckBox
        .selectedProperty()
        .addListener(
            (options, oldValue, newValue) -> {
              if (oldValue != newValue) {

                String start = (String) startLocationBox.getValue();
                String end = (String) endLocationBox.getValue();

                if (!newValue) {
                  nodes = ActiveGraph.getNodes();
                  edges = ActiveGraph.getEdges();
                  graph = ActiveGraph.getActiveGraph();

                  startLocationBox.getItems().remove(0, startLocationBox.getItems().size());
                  endLocationBox.getItems().remove(0, endLocationBox.getItems().size());

                  for (Node node : nodes) {
                    startLocationBox.getItems().add(node.nodeID);
                  }

                  for (Node node : nodes) {
                    endLocationBox.getItems().add(node.nodeID);
                  }
                } else {
                  nodes = ActiveGraphNoStairs.getNodes();
                  edges = ActiveGraphNoStairs.getEdges();
                  graph = ActiveGraphNoStairs.getActiveGraph();

                  startLocationBox.getItems().remove(0, startLocationBox.getItems().size());
                  endLocationBox.getItems().remove(0, endLocationBox.getItems().size());

                  for (Node node : nodes) {
                    startLocationBox.getItems().add(node.nodeID);
                  }

                  for (Node node : nodes) {
                    endLocationBox.getItems().add(node.nodeID);
                  }
                }

                mapInsertController.removeAllAdornerElements();
                mapInsertController.addAdornerElements(
                    nodes, edges, mapInsertController.floorNumber);

                resetMouseHandlingForAdorners();

                startLocationBox.setValue(start);
                endLocationBox.setValue(end);
                calculatePath();
              }
            });

    // Floor selection menu population
    int i = 0;
    for (MenuItem menuItem : mapInsertController.getFloorMenu().getItems()) {
      int index = i;
      menuItem.setOnAction(
          e -> {
            mapInsertController.removeAllAdornerElements();
            mapInsertController.changeMapImage(mapInsertController.getMapOrder().get(index));
            mapInsertController.addAdornerElements(nodes, edges, mapInsertController.floorNumber);

            resetMouseHandlingForAdorners();
            // mapInsertController.updateMenuPreview(e, mapInsertController.getFloorMenu());

            calculatePath();
          });
      i++;
    }

    upButton.setOnAction(e -> mapInsertController.panOnButtons("up"));
    downButton.setOnAction(e -> mapInsertController.panOnButtons("down"));
    leftButton.setOnAction(e -> mapInsertController.panOnButtons("left"));
    rightButton.setOnAction(e -> mapInsertController.panOnButtons("right"));
    zoomInButton.setOnAction(e -> mapInsertController.zoomOnButtons("in"));
    zoomOutButton.setOnAction(e -> mapInsertController.zoomOnButtons("out"));

    zoomSlider.setDisable(true);

    zoomLabel.setText("Zoom");

    // Populate local graph and selection menus
    // nodes = mapInsertController.loadNodesFromCSV();
    // edges = mapInsertController.loadEdgesFromCSV();
    nodes = ActiveGraph.getNodes();
    edges = ActiveGraph.getEdges();
    graph = ActiveGraph.getActiveGraph();

    for (Node node : nodes) {
      startLocationBox.getItems().add(node.nodeID);
    }

    for (Node node : nodes) {
      endLocationBox.getItems().add(node.nodeID);
    }

    // Select startNodeBox
    startLocationBox.requestFocus();

    // Init Map
    Platform.runLater(
        () -> {
          mapInsertController.getFloorMenu().setText("Parking");
          mapInsertController.changeMapImage(MapController.MAP_PAGE.PARKING);

          mapInsertController.addAdornerElements(nodes, edges, mapInsertController.floorNumber);

          resetMouseHandlingForAdorners();
        });

    Platform.runLater(() -> startLocationBox.requestFocus());
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

  // Set selection click handlers
  protected void resetMouseHandlingForAdorners() {
    for (javafx.scene.Node p : mapInsertController.getAdornerPane().getChildren()) {
      try {

        if (p instanceof MapController.CircleEx) {
          setNodeOnClick((MapController.CircleEx) p);
        }

      } catch (Exception exp) {
        // System.out.println("no point selected");
      }
    }
  }

  private void setNodeOnClick(MapController.CircleEx node) {
    node.setOnMouseClicked(
        w -> {
          if (!node.hasFocus || (node.hasFocus && pathActive)) {
            if (startLocationBox.isFocused()) {
              if (startLocationBox.getValue() != null) {
                MapController.CircleEx n =
                    (MapController.CircleEx)
                        mapInsertController
                            .getAdornerPane()
                            .getScene()
                            .lookup("#" + startLocationBox.getValue());
                if (n != null) {
                  mapInsertController.deSelectCircle(n);
                }
              }

              mapInsertController.selectCircle((MapController.CircleEx) node);
              startLocationBox.setValue(node.getId());

            } else if (endLocationBox.isFocused()) {

              if (endLocationBox.getValue() != null) {
                MapController.CircleEx n =
                    (MapController.CircleEx)
                        mapInsertController
                            .getAdornerPane()
                            .getScene()
                            .lookup("#" + endLocationBox.getValue());
                if (n != null) {
                  mapInsertController.deSelectCircle(n);
                }
              }

              mapInsertController.selectCircle((MapController.CircleEx) node);
              endLocationBox.setValue(node.getId());
            }
          } else {
            if (startLocationBox.isFocused()) {
              mapInsertController.selectCircle((MapController.CircleEx) node);
              startLocationBox.setValue("");

            } else if (endLocationBox.isFocused()) {
              mapInsertController.selectCircle((MapController.CircleEx) node);
              endLocationBox.setValue("");
            }
          }
        });
  }

  /* Dont need it
  private void setEdgeOnClick(MapController.LineEx edge) {
    edge.setOnMouseClicked(
            w -> {
              if (!shiftPressed) {
                mapInsertController.clearSelection();
              }
              mapInsertController.selectLine((MapController.LineEx) edge);
            });
  }*/

  public void calculatePath() {
    if (startLocationBox.getValue() != null && endLocationBox.getValue() != null) {

      ArrayList<String> endLocations = new ArrayList<>();
      endLocations.add((String) endLocationBox.getValue());
      if (bathroomCheck.isSelected()) {
        endLocations.add(
            0,
            DijkstrasAlgorithm.dijkstraDetour(
                graph, (String) startLocationBox.getValue(), endLocations, "REST"));
      }
      if (cafeCheck.isSelected()) {
        endLocations.add(
            0,
            DijkstrasAlgorithm.dijkstraDetour(
                graph, (String) startLocationBox.getValue(), endLocations, "FOOD"));
      }
      if (kioskCheck.isSelected()) {
        endLocations.add(
            0,
            DijkstrasAlgorithm.dijkstraDetour(
                graph, (String) startLocationBox.getValue(), endLocations, "KIOS"));
      }

      pathActive = true;

      mapInsertController.clearSelection();
      ArrayList<Node> nodes =
          AStarAlgorithm.aStar(graph, (String) startLocationBox.getValue(), endLocations);

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
