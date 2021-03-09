package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import edu.wpi.cs3733.c21.teamY.algorithms.AlgorithmCalls;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.FuzzySearchComboBoxListener;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import edu.wpi.cs3733.c21.teamY.entity.*;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class PathfindingPageController extends SubPage {

  // connects the scenebuilder button to a code button
  // add buttons to other scenes here
  @FXML private AnchorPane anchor;

  private MapController mapInsertController;
  //  @FXML private JFXButton resetView;
  @FXML private ComboBox startLocationBox;
  @FXML private ComboBox endLocationBox;

  FuzzySearchComboBoxListener startLocationFuzzy;
  FuzzySearchComboBoxListener endLocationFuzzy;

  @FXML private JFXButton bathroomBtn;
  @FXML private JFXButton cafeBtn;
  @FXML private JFXButton kioskBtn;
  @FXML private JFXButton parkingBtn;
  @FXML private JFXButton noStairsBtn;
  @FXML private GridPane overlayGridPane;
  @FXML private JFXButton multDestinationBtn;

  //  @FXML private Slider zoomSlider;
  //  @FXML private Button upButton;
  //  @FXML private Button downButton;
  //  @FXML private Button leftButton;
  //  @FXML private Button rightButton;
  //  @FXML private JFXButton zoomInButton;
  //  @FXML private JFXButton zoomOutButton;
  @FXML private VBox textDirectionsBox;
  @FXML private VBox textDirectionViewer;
  @FXML public VBox navigationHeaderVBox;

  @FXML private JFXButton exitDirectionBtn;
  //  @FXML private VBox sideMenuVBox;
  @FXML private RowConstraints row1;
  @FXML private JFXButton swapLocationsBox;
  // @FXML private Label zoomLabel;

  private ArrayList<Node> nodes = new ArrayList<Node>();
  private ArrayList<Edge> edges = new ArrayList<Edge>();

  private ArrayList<Node> pathNodes = new ArrayList<Node>(); // Used to store path between floors
  private ComboBox lastSelectedComboBox = null;
  ArrayList<String> endLocations = new ArrayList<>();
  Boolean addDest = false; // Toggle state of the add destinations button
  // Used to save start/end node on a floor
  MapController.CircleEx startNode;
  MapController.CircleEx endNode;

  private Graph graph;

  private boolean pathActive = false;
  private String noType = "";
  private JFXComboBox<String> cmb = new JFXComboBox<>();

  private boolean bathroom = false;
  private boolean restaurant = false;
  private boolean noStairs = false;
  private boolean kiosk = false;
  private int nearestNodeRadius = 500;

  private boolean textExpanded = false;

  // TooltipInstantiations
  Tooltip bathroomTooltip = new Tooltip("Add/Remove Bathroom Detour");
  Tooltip cafeTooltip = new Tooltip("Add/Remove Cafe Detour");
  Tooltip kioskTooltip = new Tooltip("Add/Remove Kiosk Detour");
  Tooltip parkingTooltip = new Tooltip("Return to parking lot");
  Tooltip noStairsTooltip = new Tooltip("Toggle handicap accessible route on/off");

  /** Do not use it. It does nothing. */
  public PathfindingPageController() {}

  //  private void loadMap() {
  //    FXMLLoader fxmlLoader = new FXMLLoader();
  //    try {
  //      javafx.scene.Node node =
  //          fxmlLoader.load(getClass().getResource("MapUserControl.fxml").openStream());
  //      mapInsertController = (MapController) fxmlLoader.getController();
  //      mapInsertController.setParent(parent);
  //      anchor.getChildren().add(node);
  //      node.toBack();
  //      // node.setOpacity(0);
  //    } catch (IOException e) {
  //      e.printStackTrace();
  //    }
  //  }

  /** FXML initialise runs after loading FXML and sets important stuff */
  @FXML
  private void initialize() {
    //    loadMap();
    textDirectionsBox.setPickOnBounds(false);
    textDirectionViewer.setPickOnBounds(false);
    textDirectionViewer.setVisible(false);
    //    textDirectionsBox.setVisible(false);
    //    exitDirectionBtn.setVisible(false);
    overlayGridPane.setPickOnBounds(false);
    overlayGridPane.toFront();

    //    sideMenuVBox.setPickOnBounds(false);
    exitDirectionBtn.setOnAction(e -> updateTextDirectionBox());
    //         attaches a handler to the button with a lambda expression

    // Reset view button
    //    resetView.setOnAction(
    //        e -> {
    //          mapInsertController.resetMapView();
    //        });
    //    resetView.toFront();
    //    zoomInButton.toFront();
    //    zoomOutButton.toFront();

    // Set the starting image early because otherwise it will flash default

    // tooltips

    Tooltip.install(bathroomBtn, bathroomTooltip);
    Tooltip.install(kioskBtn, kioskTooltip);
    Tooltip.install(cafeBtn, cafeTooltip);
    Tooltip.install(parkingBtn, parkingTooltip);
    Tooltip.install(noStairsBtn, noStairsTooltip);

    JFXDialog dialog = new JFXDialog();
    dialog.setContent(
        new Label(
            " Scroll to Zoom"
                + "\n Hold CTRL + Scroll to Pan Up and down"
                + "\n Hold SHIFT + Scroll to Pan left and right"
                + "\n Reset brings back the original framing"));

    // Node selection menus Keys
    startLocationBox.setOnKeyPressed(
        e -> {
          if (e.getCode() == KeyCode.ENTER) {
            calculatePath();
          }
        });

    // Start and End location box events
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

    swapLocationsBox.setOnAction(
        e -> {
          String startLoc = (String) startLocationBox.getValue();
          startLocationBox.setValue(endLocationBox.getValue());
          endLocationBox.setValue(startLoc);
          calculatePath();
        });
    multDestinationBtn.setOnAction(
        e -> {
          addDest = true;
        });

    startLocationBox.setOnAction(e -> lastSelectedComboBox = startLocationBox);
    endLocationBox.setOnAction(e -> lastSelectedComboBox = endLocationBox);

    bathroomBtn.setOnAction(e -> detourBtnPressed(e));
    cafeBtn.setOnAction(e -> detourBtnPressed(e));
    kioskBtn.setOnAction(e -> detourBtnPressed(e));
    parkingBtn.setOnAction(e -> detourBtnPressed(e));
    noStairsBtn.setOnAction(e -> detourBtnPressed(e));

    // Floor selection menu population

    //    upButton.setOnAction(e -> mapInsertController.panOnButtons("up"));
    //    downButton.setOnAction(e -> mapInsertController.panOnButtons("down"));
    //    leftButton.setOnAction(e -> mapInsertController.panOnButtons("left"));
    //    rightButton.setOnAction(e -> mapInsertController.panOnButtons("right"));
    //    zoomInButton.setOnAction(e -> mapInsertController.zoomOnButtons(0.1));
    //    zoomOutButton.setOnAction(e -> mapInsertController.zoomOnButtons(-0.1));

    // zoomSlider.setDisable(true);

    // zoomLabel.setText("Zoom");

    // Select startNodeBox
    startLocationBox.requestFocus();

    // Init Graph
    resetGraphNodesEdges();
    resetComboBoxes();
    System.out.println("Made it one!");
    // this handles auto route calculation after covid survey determination

    String userId = Settings.getSettings().getCurrentUsername();
    System.out.println("Made it!");
    if (DataOperations.checkForCompletedCovidSurvey(userId)) {
      System.out.println("Check complete!");
      int status = DataOperations.checkSurveyStatus(userId);
      if (status == 1) {
        endLocationBox.setValue("Atrium Main Entrance");
      } else if (status == 0) {
        endLocationBox.setValue("Emergency Entrance");
      }
    }

    // Init Map
    Platform.runLater(
        () -> {
          if (parent.isDesktop) {
            mapInsertController =
                ((NavigationMapController) parent.rightPageController).getMapInsertController();
          } else {

            mapInsertController =
                ((NavigationMapController) parent.centerPageController).getMapInsertController();

            JFXButton resetBtn = mapInsertController.getReset();
            resetBtn.setText("Reset");
            resetBtn.setMaxWidth(36);
            resetBtn.setMaxHeight(36);
            resetBtn.setMinWidth(36);
            resetBtn.setMinHeight(36);
            // ((JFXButton) menuItem).setStyle("-fx-font: 20");
            resetBtn.setStyle(
                "-fx-font-size: 10; -fx-background-color: #5a5c94; -fx-text-fill:#efeff9; -fx-background-radius: 18; -fx-font-size: 8");
          }

          int i = -1;
          for (javafx.scene.Node menuItem : mapInsertController.getFloorList().getChildren()) {
            if (i != -1) {
              int index = i;
              ((JFXButton) menuItem).setOnAction(e -> handleFloorChanged(e, index));
              i++;
            } else {
              i++;
            }
            if (!parent.isDesktop) {
              ((JFXButton) menuItem).setMaxWidth(36);
              ((JFXButton) menuItem).setMaxHeight(36);
              ((JFXButton) menuItem).setMinWidth(36);
              ((JFXButton) menuItem).setMinHeight(36);
              // ((JFXButton) menuItem).setStyle("-fx-font: 20");
              ((JFXButton) menuItem)
                  .setStyle(
                      "-fx-font-size: 17; -fx-background-color: #5a5c94; -fx-text-fill: #efeff9; -fx-background-radius: 18");
              // HBox.setMargin(menuItem, new Insets(0, 50, 0, 0));
            }
          }

          // Set handler for Mouse Click Anywhere on Map
          mapInsertController
              .getAdornerPane()
              .setOnMouseReleased(
                  e -> {
                    handleClickOnMap(e);
                  });
          mapInsertController.changeMapImage(MapController.MAP_PAGE.PARKING);
          mapInsertController.removeAllAdornerElements();
          // mapInsertController.getFloorMenu().setText("Parking");
          mapInsertController.changeMapImage(MapController.MAP_PAGE.PARKING);
          mapInsertController.addAdornerElements(nodes, edges, mapInsertController.floorNumber);

          mapInsertController.setDisplayUnselectedAdorners(false);

          //          SubPage subPage = parent.rightPageController;
          startLocationBox.requestFocus();

          // overlayGridPane.maxWidthProperty().bind(getWidth());

          // overlayGridPane.prefWidthProperty().bind(overlayGridPane.getScene().widthProperty());
          overlayGridPane.prefHeightProperty().bind(overlayGridPane.getScene().heightProperty());
          overlayGridPane
              .getScene()
              .widthProperty()
              .addListener(
                  new ChangeListener<Number>() {
                    @Override
                    public void changed(
                        ObservableValue<? extends Number> observable,
                        Number oldValue,
                        Number newValue) {
                      overlayGridPane.setPrefWidth(((double) newValue) - 130);
                    }
                  });
          row1.maxHeightProperty().bind(anchor.getScene().heightProperty());
          try {
            startLocationBox.setValue(
                graph.nodeFromID(
                        DataOperations.findCarLocation(Settings.getSettings().getCurrentUsername()))
                    .longName);
          } catch (SQLException e) {
            e.printStackTrace();
          }
          calculatePath();
        });
  }

  /* private ReadOnlyDoubleProperty getWidth() {
    return new ReadOnlyDoubleProperty(overlayGridPane.getScene().widthProperty().getValue() - 130) {
    };
  }*/
  private ArrayList<Node> runAlgo(
      Graph g, String startID, ArrayList<String> goalIDs, String accessType) {
    return Settings.getSettings().getAlgorithmSelection().run(g, startID, goalIDs, accessType);
  }

  private void detourBtnPressed(ActionEvent e) {
    if (e.getSource() == bathroomBtn) {
      bathroom = !bathroom;
      if (bathroom) bathroomBtn.setStyle("-fx-background-color: #efeff9");
      else bathroomBtn.setStyle("-fx-background-color: transparent");
    } else if (e.getSource() == cafeBtn) {
      restaurant = !restaurant;
      if (restaurant) cafeBtn.setStyle("-fx-background-color: #efeff9");
      else cafeBtn.setStyle("-fx-background-color: transparent");
    } else if (e.getSource() == kioskBtn) {
      kiosk = !kiosk;
      if (kiosk) kioskBtn.setStyle("-fx-background-color: #efeff9");
      else kioskBtn.setStyle("-fx-background-color: transparent");
    } else if (e.getSource() == parkingBtn) {
      try {
        endLocationBox.setValue(
            graph.nodeFromID(
                    DataOperations.findCarLocation(Settings.getSettings().getCurrentUsername()))
                .longName);
      } catch (Exception exception) {
        System.out.println("Find Car Location Failed in pathfinding page");
      }

    } else if (e.getSource() == noStairsBtn) {
      noStairs = !noStairs;
      String start = (String) startLocationBox.getValue();
      String end = (String) endLocationBox.getValue();

      if (!noStairs) {
        noType = "";
        noStairsBtn.setStyle("-fx-background-color: transparent");
      } else {
        noType = "STAI";
        noStairsBtn.setStyle("-fx-background-color: #efeff9");
      }

      mapInsertController.removeAllAdornerElements();
      mapInsertController.addAdornerElements(nodes, edges, mapInsertController.floorNumber);

      startLocationBox.setValue(start);
      endLocationBox.setValue(end);
    }
    // Detour handling for multiple destinations
    System.out.println("End size " + endLocations.size());
    endLocations.remove(endLocations.size() - 1);
    int size = endLocations.size();
    for (int k = 0; k < size; k++) {
      System.out.println("Inside loop");
      if ((graph.nodeFromID(endLocations.get(k)).nodeType.equals("BATH"))
          || (graph.nodeFromID(endLocations.get(k)).nodeType.equals("KIOS"))
          || (graph.nodeFromID(endLocations.get(k)).nodeType.equals("FOOD"))) {
        System.out.println("Removing " + graph.nodeFromID(endLocations.get(k)).longName);
        endLocations.remove(endLocations.get(k));
        size--;
      }
    }
    addDest = true;

    calculatePath();
  }
  // NEAREST NODE ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

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
    circle.setRadius(nearestNodeRadius); // Change to adjust selection range
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

    // use circle to find nearest node
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

  // EVENT HANDLING ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /**
   * handleClickOnMap handles all clicking on map, whether that be on a node or not
   *
   * @param e MouseEvent
   */
  private void handleClickOnMap(MouseEvent e) {
    // Click intersects with node
    if (e.getPickResult().getIntersectedNode() instanceof MapController.CircleEx) {
      handleClickOnNode((MapController.CircleEx) e.getPickResult().getIntersectedNode());
    } else {
      // Clicked on blank map
      mapInsertController.defaultOnMouseReleased(e);

      if (!mapInsertController.wasLastClickDrag()) {
        // If wasnt a drag, but clicked on blank map, select nearest node within reason
        MapController.CircleEx p = getNearestNode(e.getX(), e.getY());
        if (p != null) {
          handleClickOnNode(p);
        }
      }
    }
  }

  /**
   * handleClickOnNode Functionality for clicking on a node
   *
   * @param node CircleEx that is passed in
   */
  private void handleClickOnNode(MapController.CircleEx node) {
    if (!node.hasFocus || (node.hasFocus && isPathActive())) {

      // Start node box is selected -> deselect old start node, use new one
      if (startLocationBox.isFocused()) {
        if (startLocationBox.getValue() != null && startNode != null) {
          mapInsertController.deSelectCircle(startNode);
        }
        startLocationBox.setValue(
            graph.nodeFromID(node.getId()).longName); // startLocationBox.setValue(node.getId())
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
        endLocationBox.setValue(
            graph.nodeFromID(node.getId()).longName); // endLocationBox.setValue(node.getId());
      }

    }
    // Deselect start or end node
    else {
      if (startLocationBox.isFocused() && startLocationBox.getValue() != null) {
        mapInsertController.deSelectCircle(node);
        startLocationBox.setValue(null);
        startNode = null;
        clearPath();
        if (endNode != null) {
          mapInsertController.selectCircle(endNode);
        }

      } else if (endLocationBox.isFocused() && endLocationBox.getValue() != null) {
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

  /**
   * Handles changing of floors in floor menu
   *
   * @param e
   * @param menuItemIndex
   */
  //  private void handleFloorChanged(ActionEvent e, int menuItemIndex) {
  //    // This should be optimised to only switch if the floor actually changed, but its very fast,
  // so
  //    // I cant be bothered
  //    System.out.println("<3 Geg");
  //    mapInsertController.removeAllAdornerElements();
  //    mapInsertController.changeMapImage(mapInsertController.getMapOrder().get(menuItemIndex));
  //    // System.out.println(nodes.toString());
  //    mapInsertController.addAdornerElements(nodes, edges, "2");
  //    // mapInsertController.getAdornerPane().toFront();
  //    System.out.println("new line");
  //    // drawPath(pathNodes);
  //    //    if (lastSelectedComboBox != null) {
  //    //      lastSelectedComboBox.requestFocus();
  //    //    }
  //    // mapInsertController.updateMenuPreview(e, mapInsertController.getFloorMenu());
  //  }
  private void handleFloorChanged(ActionEvent e, int menuItemIndex) {
    // This should be optimised to only switch if the floor actually changed, but its very fast, so
    // I cant be bothered
    mapInsertController.removeAllAdornerElements();
    mapInsertController.changeMapImage(mapInsertController.getMapOrder().get(menuItemIndex));
    mapInsertController.addAdornerElements(nodes, edges, mapInsertController.floorNumber);
    drawPath(pathNodes);
    if (lastSelectedComboBox != null) {
      lastSelectedComboBox.requestFocus();
    }
    // mapInsertController.updateMenuPreview(e, mapInsertController.getFloorMenu());
  }

  // button event handler
  @FXML
  private void buttonClicked(ActionEvent e) {
    // error handling for FXMLLoader.load
    try {
      // initializing stage
      Stage stage = null;

      // display new stage
      stage.show();
    } catch (Exception exp) {
    }
  }

  @Override
  public void loadNavigationBar() {
    if (!parent.isDesktop) parent.animateCenterColumnWidth(380);
  };

  private void updateTextDirectionBox() {
    if (!parent.isDesktop) {
      if (textExpanded) {
        textDirectionsBox.setVisible(false);
        textDirectionViewer.setVisible(false);
        exitDirectionBtn.setText("Show Steps");

        parent.animateCenterColumnWidth(380);
      } else {
        textDirectionsBox.setVisible(true);
        textDirectionViewer.setVisible(true);
        exitDirectionBtn.setText("Exit Steps");

        parent.animateCenterColumnWidth(0);
      }
      textExpanded = !textExpanded;
    } else {
      textDirectionsBox.setVisible(false);
    }
    exitDirectionBtn.toFront();
  }

  private void generateTextDirections(ArrayList<Node> pathNodes) {
    textDirectionViewer.getChildren().clear();

    if (parent.isDesktop) {
      textDirectionsBox.setVisible(true);
      textDirectionViewer.setVisible(true);
    }

    ArrayList<String> directionList = AlgorithmCalls.textDirections(pathNodes, endLocations);
    System.out.println(directionList.size());
    for (String direction : directionList) {

      Label newLabel = new Label(direction);
      textDirectionViewer.getChildren().add(newLabel);
      newLabel.toFront();
      for (String endLocation : endLocations) {
        if (direction.contains(graph.nodeFromID(endLocation).longName)
            && direction.contains("reached")) {
          newLabel.setStyle("-fx-font-weight:BOLD;");
        }
      }
    }
  }

  // PATHFINDING ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** resetGraphNodesEdges sets graph, nodes, stairs, to updated values in ActiveGraph */
  private void resetGraphNodesEdges() {
    try {
      ActiveGraph.initialize();
    } catch (Exception exception) {
      System.out.println("no work");
      // IT NO WORK
    }
    nodes = ActiveGraph.getNodes();
    edges = ActiveGraph.getEdges();
    graph = ActiveGraph.getActiveGraph();
  }

  /** resetComboBoxes Resets node comboboxes with values from nodes and edges */
  private void resetComboBoxes() {
    startLocationBox.getItems().remove(0, startLocationBox.getItems().size());
    endLocationBox.getItems().remove(0, endLocationBox.getItems().size());

    for (Node node : nodes) {
      String name = node.longName;
      String type = node.nodeType;
      // Filtering out the unwanted midway points
      if (!type.equals("WALK")
          && !type.equals("ELEV")
          && !type.equals("HALL")
          && !type.equals("STAI")) {
        startLocationBox.getItems().add(name);
        endLocationBox.getItems().add(name);
      }
    }
    startLocationFuzzy = new FuzzySearchComboBoxListener(startLocationBox);
    endLocationFuzzy = new FuzzySearchComboBoxListener(endLocationBox);
  }

  /**
   * isPathActive
   *
   * @return returns if there is a path that should be on screen (or if the saved path has > 2
   *     nodes)
   */
  private boolean isPathActive() {
    return pathNodes.size() > 2;
  }

  /** calculatePath Calculates the path between two nodes in the comboboxes and saves it to path */
  public void calculatePath() {
    clearPath();
    if (startLocationBox.getValue() != null && endLocationBox.getValue() != null) {
      String endID =
          graph.longNodes.get((String) endLocationBox.getValue())
              .nodeID; // (String) endLocationBox.getValue();
      endLocations.add(endID);
      String startID =
          graph.longNodes.get((String) startLocationBox.getValue())
              .nodeID; // (String) startLocationBox.getValue();

      if (graph.longNodes.get((String) startLocationBox.getValue()).nodeType.equals("PARK")) {
        try {
          if (DataOperations.findCarLocation(Settings.getSettings().getCurrentUsername())
              .equals("")) {
            DataOperations.saveParkingSpot(startID, Settings.getSettings().getCurrentUsername());
          } else {
            DataOperations.updateParkingSpot(startID, Settings.getSettings().getCurrentUsername());
          }

        } catch (Exception exception) {
          System.out.println("Save Parking Spot failed");
        }
      }

      mapInsertController.clearSelection();

      ArrayList<Node> algoNodes = runAlgo(graph, startID, endLocations, noType);

      boolean detour = false;
      if (bathroom) {
        endLocations = AlgorithmCalls.dijkstraDetour(graph, algoNodes, endLocations, "REST");
        detour = true;
      }
      if (restaurant) {
        endLocations = AlgorithmCalls.dijkstraDetour(graph, algoNodes, endLocations, "FOOD");
        detour = true;
      }
      if (kiosk) {
        endLocations = AlgorithmCalls.dijkstraDetour(graph, algoNodes, endLocations, "KIOS");
        detour = true;
      }
      // If we've taken a detour, regenerate path
      if (detour) {
        algoNodes = runAlgo(graph, startID, endLocations, noType);
      }

      pathNodes = algoNodes;
      drawPath(pathNodes);

      generateTextDirections(pathNodes);
      addDest = false;
    }
  }

  /**
   * drawPath Selects all nodes and edges in passed in path
   *
   * @param nodes is the path passed in
   */
  private void drawPath(ArrayList<Node> nodes) {
    mapInsertController.clearSelection();
    if (nodes != null) {
      for (int i = 0; i < nodes.size() - 1; i++) {
        MapController.CircleEx n =
            (MapController.CircleEx)
                mapInsertController.getAdornerPane().lookup("#" + nodes.get(i).nodeID);
        MapController.CircleEx m =
            (MapController.CircleEx)
                mapInsertController.getAdornerPane().lookup("#" + nodes.get(i + 1).nodeID);
        if (n != null
            && (i == 0
                || endLocations.contains(
                    n.getId()))) { // selects first circle in path and any destinations
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
                      .lookup("#" + nodes.get(i).nodeID + "_" + nodes.get(i + 1).nodeID);

          if (l == null) {
            l =
                (MapController.LineEx)
                    mapInsertController
                        .getAdornerPane()
                        .lookup("#" + nodes.get(i + 1).nodeID + "_" + nodes.get(i).nodeID);
          }

          if (l != null) {
            if (l.direcVisualsEnabled) {
              l.biDirectional = true;
              l.updateVisuals();
            } else {
              l.direcVisualsEnabled = true;
              l.setDirection(n);
            }

            // Selecting updates visuals automatically
            mapInsertController.selectLine(l);
          } else {
            System.out.println("Could not identify line");
          }
        }
      }
    }
  }

  /** clearPath deselects all and clears saved path */
  private void clearPath() {
    mapInsertController.clearSelection();
    pathNodes = new ArrayList<Node>();
    if (!addDest) {
      endLocations = new ArrayList<String>();
    }
  }

  public JFXButton getBathroomBtn() {
    return bathroomBtn;
  }
}
