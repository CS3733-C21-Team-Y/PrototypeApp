package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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

  private ArrayList<Node> pathNodes = new ArrayList<Node>(); // Used to store path between floors

  // Used to save start/end node on a floor
  MapController.CircleEx startNode;
  MapController.CircleEx endNode;

  private Graph graph;

  private JFXComboBox<String> cmb = new JFXComboBox<>();

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
            drawPath(pathNodes);
            mapInsertController.updateMenuPreview(e, mapInsertController.getFloorMenu());
          });
      i++;
    }

    // Movement and zoom buttons
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

    /*
    // Mouse Click on Map not Node
    mapInsertController
        .getAdornerPane()
        .setOnMouseClicked(
            e -> {

              MapController.CircleEx p = getNearestNode(e.getX(), e.getY());
              if (p != null) {
                handleClickOnNode(p);
              }
            });*/

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

  /**
   * Gets the nearest to node to an x and y coord double
   *
   * @param xCoord
   * @param yCoord
   * @return MapController.CircleEx node
   */
  private MapController.CircleEx getNearestNode(double xCoord, double yCoord) {
    ArrayList<MapController.CircleEx> nodesWithinRange = new ArrayList<>();

    // Draw circle
    Circle circle = new Circle();
    circle.setCenterX(xCoord);
    circle.setCenterY(yCoord);
    circle.setRadius(25); // Change to adjust selection range
    circle.setFill(Color.TRANSPARENT);

    // get nodes within circle
    mapInsertController.getAdornerPane().getChildren().add(circle);
    for (javafx.scene.Node node : mapInsertController.getAdornerPane().getChildren()) {
      if (node instanceof MapController.CircleEx) {
        if (circle.intersects(node.getBoundsInLocal())) {
          nodesWithinRange.add((MapController.CircleEx) node);
        }
      }
    }

    // use nearest neighbor to find nearest node
    double minDistance = circle.getRadius();
    MapController.CircleEx minNode = null;
    for (MapController.CircleEx n : nodesWithinRange) {
      double distX = n.getCenterX() - circle.getCenterX();
      double distY = n.getCenterY() - circle.getCenterY();
      double pythagoras = Math.sqrt(distX * distX + distY * distY);
      if (pythagoras < minDistance) {
        minNode = n;
        minDistance = pythagoras;
      }
    }
    mapInsertController.getAdornerPane().getChildren().remove(circle);
    circle = null;
    return minNode;
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

  private boolean isPathActive() {
    return pathNodes.size() > 2;
  }

  private void setNodeOnClick(MapController.CircleEx node) {
    node.setOnMouseClicked(
        w -> {
          handleClickOnNode(node);
        });
  }

  // Select Node
  private void handleClickOnNode(MapController.CircleEx node) {
    if (!node.hasFocus || (node.hasFocus && isPathActive())) {

      // Start node box is selected -> deselect old start node, use new one
      if (startLocationBox.isFocused()) {
        if (startLocationBox.getValue() != null && startNode != null) {
          mapInsertController.deSelectCircle(startNode);
        }
        startLocationBox.setValue(node.getId());
        startNode = node;

        mapInsertController.selectCircle(node);
      }

      // End node box is selected -> deselect old end node, use new one
      else if (endLocationBox.isFocused()) {
        if (endLocationBox.getValue() != null) {
          if (endLocationBox.getValue() != null && endNode != null) {
            mapInsertController.deSelectCircle(endNode);
          }
        }
        mapInsertController.selectCircle(node);
        endLocationBox.setValue(node.getId());
      }

    }
    // Deselect start or end node
    else {
      if (startLocationBox.isFocused()) {
        mapInsertController.deSelectCircle(node);
        startLocationBox.setValue(null);
        startNode = null;
        clearPath();
        if (endNode != null) {
          mapInsertController.selectCircle(endNode);
        }

      } else if (endLocationBox.isFocused()) {
        mapInsertController.deSelectCircle(node);
        endLocationBox.setValue(null);
        endNode = null;
        clearPath();
        if (startNode != null) {
          mapInsertController.selectCircle(startNode);
        }
      }
    }
  }

  // Calculates and draws the path
  public void calculatePath() {
    clearPath();
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

      mapInsertController.clearSelection();
      ArrayList<Node> nodes =
          AStarAlgorithm.aStar(graph, (String) startLocationBox.getValue(), endLocations);

      pathNodes = nodes;
      drawPath(pathNodes);
    }
  }

  public void drawPath(ArrayList<Node> nodes) {
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
        if (m != null && i == nodes.size() - 2) { // Selects last node in path
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

          if (l != null) {
            mapInsertController.selectLine(l);
          } else {
            System.out.println("Could not identify line");
          }
        }
      }
    }
  }

  public void clearPath() {
    mapInsertController.clearSelection();
    pathNodes = new ArrayList<Node>();
  }
}
