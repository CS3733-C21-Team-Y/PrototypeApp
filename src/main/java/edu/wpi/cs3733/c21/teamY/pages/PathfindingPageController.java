package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import edu.wpi.cs3733.c21.teamY.algorithms.AlgorithmCalls;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import edu.wpi.cs3733.c21.teamY.entity.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PathfindingPageController extends SubPage {

  // connects the scenebuilder button to a code button
  // add buttons to other scenes here
  @FXML private AnchorPane anchor;

  private MapController mapInsertController;
  //  @FXML private JFXButton resetView;
  @FXML private ComboBox destinationCB1;
  @FXML private ComboBox destinationCB2;
  @FXML private VBox destinationsVBox;
  @FXML private ScrollPane destinationScrollPane;
  @FXML private SplitPane rightMenuSplitPane;

  @FXML private JFXButton bathroomBtn;
  @FXML private JFXButton cafeBtn;
  @FXML private JFXButton kioskBtn;
  @FXML private JFXButton parkingBtn;
  @FXML private JFXButton noStairsBtn;
  @FXML private GridPane overlayGridPane;
  @FXML private JFXButton multDestinationBtn;
  @FXML private JFXButton optimizeButton;

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
  @FXML private VBox sideMenuVBox;
  @FXML private RowConstraints row1;
  @FXML private JFXButton swapLocationsBox;
  @FXML private GridPane textDirectionGrid;
  @FXML private ScrollPane textDirectionScroll;
  // @FXML private Label zoomLabel;

  private ArrayList<Edge> edges = new ArrayList<Edge>();
  private ArrayList<Node> nodes = new ArrayList<Node>();

  private ArrayList<Node> pathNodes = new ArrayList<Node>(); // Used to store path between floors
  private ComboBox lastSelectedComboBox = null;
  ArrayList<String> endLocations = new ArrayList<>();
  // Boolean addDest = false; // Toggle state of the add destinations button
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

  private int intendedHeight = 90;

  private boolean textExpanded = false;

  // TooltipInstantiations
  Tooltip bathroomTooltip = new Tooltip("Add/Remove Bathroom Detour");
  Tooltip cafeTooltip = new Tooltip("Add/Remove Cafe Detour");
  Tooltip kioskTooltip = new Tooltip("Add/Remove Kiosk Detour");
  Tooltip parkingTooltip = new Tooltip("Return to parking lot");
  Tooltip noStairsTooltip = new Tooltip("Toggle handicap accessible route on/off");
  Tooltip swapTooltip = new Tooltip("Click to swap your start and end destination");
  Tooltip multiDestTooltip =
      new Tooltip("Click to save the current destination to allow for additional destinations");
  Tooltip optimizeTooltip = new Tooltip("Optimizes the current path");

  private ArrayList<DestinationItemController> destinations =
      new ArrayList<DestinationItemController>();

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
    anchor.setPickOnBounds(false);
    overlayGridPane.setPickOnBounds(false);
    overlayGridPane.setPickOnBounds(false);
    sideMenuVBox.setPickOnBounds(false);
    textDirectionGrid.setPickOnBounds(false);
    textDirectionScroll.setPickOnBounds(false);
    overlayGridPane.toFront();

    //    sideMenuVBox.setPickOnBounds(false);
    exitDirectionBtn.setOnAction(
        e -> {
          updateTextDirectionBox();
          exitDirectionBtn.toFront();
        });
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

    // Init Graph
    resetGraphNodesEdges();

    // tooltips

    Tooltip.install(bathroomBtn, bathroomTooltip);
    Tooltip.install(kioskBtn, kioskTooltip);
    Tooltip.install(cafeBtn, cafeTooltip);
    Tooltip.install(parkingBtn, parkingTooltip);
    Tooltip.install(noStairsBtn, noStairsTooltip);
    Tooltip.install(multDestinationBtn, multiDestTooltip);
    Tooltip.install(optimizeButton, optimizeTooltip);
    Tooltip.install(swapLocationsBox, swapTooltip);

    JFXDialog dialog = new JFXDialog();
    dialog.setContent(
        new Label(
            " Scroll to Zoom"
                + "\n Hold CTRL + Scroll to Pan Up and down"
                + "\n Hold SHIFT + Scroll to Pan left and right"
                + "\n Reset brings back the original framing"));

    swapLocationsBox.setOnAction(
        e -> {
          flipPath();
        });
    multDestinationBtn.setOnAction(
        e -> {
          initializeNewDestination();
        });

    optimizeButton.setOnAction(
        e -> {
          optimizePath();
        });

    bathroomBtn.setOnAction(e -> detourBtnPressed(e));
    bathroomBtn.setCursor(Cursor.HAND);
    cafeBtn.setOnAction(e -> detourBtnPressed(e));
    cafeBtn.setCursor(Cursor.HAND);
    kioskBtn.setOnAction(e -> detourBtnPressed(e));
    kioskBtn.setCursor(Cursor.HAND);
    parkingBtn.setOnAction(e -> detourBtnPressed(e));
    parkingBtn.setCursor(Cursor.HAND);
    noStairsBtn.setOnAction(e -> detourBtnPressed(e));
    noStairsBtn.setCursor(Cursor.HAND);

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

    // resetComboBoxes();
    System.out.println("Made it one!");
    // this handles auto route calculation after covid survey determination

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
                "-fx-background-color: #5a5c94; -fx-text-fill:#efeff9; -fx-background-radius: 18; -fx-font-size: 8");
          }

          destinationCB1 = initializeNewDestination();
          destinationCB2 = initializeNewDestination();
          destinationCB1.requestFocus();
          destinationCB2.setOnAction(e -> lastSelectedComboBox = destinationCB2);

          String userId = Settings.getSettings().getCurrentUsername();
          System.out.println("Made it!");
          if (DataOperations.checkForCompletedCovidSurvey(userId)) {
            System.out.println("Check complete!");
            int status = DataOperations.checkSurveyStatus(userId);
            if (status == 1) {
              destinationCB2.setValue("Atrium Main Entrance");
            } else if (status == 0) {
              destinationCB2.setValue("Emergency Entrance");
            }
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
              javafx.scene.Node currentFloor =
                  mapInsertController.getFloorList().getChildren().get(0);
              ((JFXButton) currentFloor)
                  .setStyle(
                      "-fx-font-size: 8; -fx-background-color: #5a5c94; -fx-text-fill: #efeff9; -fx-background-radius: 18");
            }
          }

          mapInsertController
              .getAdornerPane()
              .setOnMousePressed(
                  e -> {
                    if (e.isSecondaryButtonDown()) {
                      System.out.println("Right button clicked");
                      rightClicked = true;
                    } else {
                      resetContextMenu();
                    }
                    mapInsertController.defaultOnMousePressed(e);
                  });

          // Set handler for Mouse Click Anywhere on Map
          mapInsertController
              .getAdornerPane()
              .setOnMouseReleased(
                  e -> {
                    if (rightClicked) {
                      System.out.println("Right button released");
                      handleRightClick(e);
                      rightClicked = false;
                    } else {
                      handleClickOnMap(e);
                    }
                  });

          mapInsertController.changeMapImage(MapController.MAP_PAGE.PARKING);
          mapInsertController.removeAllAdornerElements();
          // mapInsertController.getFloorMenu().setText("Parking");
          mapInsertController.changeMapImage(MapController.MAP_PAGE.PARKING);
          mapInsertController.addAdornerElements(nodes, edges, mapInsertController.floorNumber);

          mapInsertController.setDisplayUnselectedAdorners(false);

          //          SubPage subPage = parent.rightPageController;
          destinationCB1.requestFocus();

          setContextMenuActions();
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
            destinationCB1.setValue(
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
        destinationCB2.setValue(
            graph.nodeFromID(
                    DataOperations.findCarLocation(Settings.getSettings().getCurrentUsername()))
                .longName);
      } catch (Exception exception) {
        System.out.println("Find Car Location Failed in pathfinding page");
      }

    } else if (e.getSource() == noStairsBtn) {
      noStairs = !noStairs;
      ArrayList<String> cbvalues = new ArrayList<>();

      for (DestinationItemController dest : destinations) {
        cbvalues.add((String) dest.getDestinationCB().getValue());
      }

      if (!noStairs) {
        noType = "";
        noStairsBtn.setStyle("-fx-background-color: transparent");
      } else {
        noType = "STAI";
        noStairsBtn.setStyle("-fx-background-color: #efeff9");
      }

      mapInsertController.removeAllAdornerElements();
      mapInsertController.addAdornerElements(nodes, edges, mapInsertController.floorNumber);

      for (int i = 0; i < cbvalues.size(); i++) {
        String newValue = cbvalues.get(i);
        Node node = graph.nodeFromLongName(newValue);
        if (noStairs && (node == null || node.nodeType.equals("STAI"))) {
          newValue = "";
        }

        destinations.get(i).getDestinationCB().setValue(newValue);
      }
    }
    // Detour handling for multiple destinations
    System.out.println("End size " + endLocations.size());
    endLocations.remove(endLocations.size() - 1);
    int size = endLocations.size();
    for (int k = 0; k < size; k++) {
      System.out.println("Inside loop");
      if ((graph.nodeFromID(endLocations.get(k)).nodeType.equals("REST"))
          || (graph.nodeFromID(endLocations.get(k)).nodeType.equals("KIOS"))
          || (graph.nodeFromID(endLocations.get(k)).nodeType.equals("FOOD"))) {
        System.out.println("Removing " + graph.nodeFromID(endLocations.get(k)).longName);
        endLocations.remove(endLocations.get(k));
        size--;
      }
    }
    // addDest = true;

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
      if (isStairViolation(n)) {
        continue;
      }
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
    if (e.getPickResult().getIntersectedNode() instanceof MapController.CircleEx
        && !isStairViolation((MapController.CircleEx) e.getPickResult().getIntersectedNode())) {
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

  private boolean isStairNode(MapController.CircleEx node) {
    Node nodee = graph.nodeFromID(node.getId());
    if (nodee != null) {
      return nodee.nodeType.equals("STAI");
    }
    return false;
  }

  private boolean isStairViolation(MapController.CircleEx node) {
    return isStairNode(node) && noStairs;
  }

  private void clearComboBoxValue(int cbIndex) {
    ComboBox selectedBox = destinations.get(cbIndex).getDestinationCB();

    if (selectedBox.getValue() != null) {

      MapController.CircleEx oldNodeCircle = null;
      Node oldNode = graph.longNodes.get((String) selectedBox.getValue());
      if (oldNode == null) {
        return;
      }
      String nodeId = oldNode.nodeID;
      if (nodeId != null) {
        oldNodeCircle =
            (MapController.CircleEx) mapInsertController.getAdornerPane().lookup("#" + nodeId);
      }

      if (oldNodeCircle != null) {
        mapInsertController.deSelectCircle(oldNodeCircle);
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

      ComboBox selectedBox = null;
      int index = -1;
      for (DestinationItemController dest : destinations) {
        index++;
        if (dest.getDestinationCB().isFocused()) {
          selectedBox = dest.getDestinationCB();
        }
      }

      if (selectedBox == null) {
        return;
      }

      // Start node box is selected -> deselect old node, use new one
      clearComboBoxValue(index);
      selectedBox.setValue(
          graph.nodeFromID(node.getId()).longName); // startLocationBox.setValue(node.getId())
      startNode = node;

      mapInsertController.selectCircle(node);
    }
    // Deselect start or end node
    /*
    else {
      if (destinationCB1.isFocused() && destinationCB1.getValue() != null) {
        mapInsertController.deSelectCircle(node);
        destinationCB1.setValue(null);
        startNode = null;
        clearPath();
        if (endNode != null) {
          mapInsertController.selectCircle(endNode);
        }

      } else if (destinationCB2.isFocused() && destinationCB2.getValue() != null) {
        mapInsertController.deSelectCircle(node);
        destinationCB2.setValue(null);
        endNode = null;
        clearPath();
        if (startNode != null) {
          mapInsertController.selectCircle(startNode);
        }
      }
    }*/
  }

  private boolean rightClicked;
  private MapController.CircleEx rightClickedNode = null;

  private void handleRightClick(MouseEvent e) {
    resetContextMenu();

    Point2D pt = new Point2D(e.getSceneX(), e.getSceneY());
    pt = mapInsertController.getAdornerPane().sceneToLocal(pt);
    rightClickedNode = getNearestNode(pt.getX(), pt.getY());

    adornerTitleLabel.setDisable(true);

    if (rightClickedNode != null) {
      Node node = graph.nodeFromID(rightClickedNode.getId());
      if (node != null) {
        adornerTitleLabel.setText(node.longName);
      }

      mapContextMenu.getItems().addAll(adornerTitleLabel, separator1, addDestination);

      // if theres a selected thisng
      ComboBox selectedBox = null;
      int index = -1;
      for (DestinationItemController dest : destinations) {
        index++;
        if (dest.getDestinationCB().isFocused()) {
          selectedBox = dest.getDestinationCB();
        }
      }
      if (selectedBox != null) {
        mapContextMenu.getItems().add(selectDestination);
        mapContextMenu.getItems().add(clearDestination);
      }

      if (rightClickedNode.hasFocus) {
        mapContextMenu.getItems().add(removeDestination);
        removeDestination.setDisable(destinations.size() <= 2);
      }

      mapContextMenu.getItems().addAll(separator1_5, selectStartNode, selectEndNode);

      if (pathActive) {
        mapContextMenu.getItems().addAll(separator2, flipPath, optimizePath, clearPath);
      }

      if (rightClickedNode.hasFocus
          && !pathNodes.get(0).equals(node)
          && !pathNodes.get(pathNodes.size() - 1).equals(node)) {
        List<Integer> nodeIndexs = indexOfAll(node, pathNodes);
        boolean next = false, prev = false;
        for (Integer nodeIndex : nodeIndexs) {
          int nextFloorChange =
              Integer.valueOf(pathNodes.get(nodeIndex + 1).floor)
                  - Integer.valueOf(mapInsertController.floorNumber);
          int prevFloorChange =
              Integer.valueOf(pathNodes.get(nodeIndex - 1).floor)
                  - Integer.valueOf(mapInsertController.floorNumber);
          if (nextFloorChange != 0) {
            next = true;
          } else if (prevFloorChange != 0) {
            prev = true;
          }
        }
        if (next && prev) {
          mapContextMenu.getItems().addAll(separator3, prevPath, nextPath);
        } else if (next) {
          mapContextMenu.getItems().addAll(separator3, nextPath);
        } else if (prev) {
          mapContextMenu.getItems().addAll(separator3, prevPath);
        }
      }

      mapContextMenu.show(
          mapInsertController.getContainerStackPane(), e.getScreenX(), e.getScreenY());

    } else if (pathActive) {
      mapContextMenu.getItems().addAll(flipPath, clearPath);
      mapContextMenu.show(
          mapInsertController.getContainerStackPane(), e.getScreenX(), e.getScreenY());
    }
  }

  ContextMenu mapContextMenu = new ContextMenu();

  MenuItem selectStartNode = new MenuItem("Select Start Location");
  MenuItem selectEndNode = new MenuItem("Select End Location");

  MenuItem selectDestination = new MenuItem("Select Destination");
  MenuItem removeDestination = new MenuItem("Remove Location");
  MenuItem addDestination = new MenuItem("Add Destination");
  MenuItem clearDestination = new MenuItem("Clear Destination");

  MenuItem clearPath = new MenuItem("Clear path");
  MenuItem flipPath = new MenuItem("Flip path");
  MenuItem optimizePath = new MenuItem("Optimize path");

  MenuItem nextPath = new MenuItem("Continue on Path");
  MenuItem prevPath = new MenuItem("Return to Previous Path");

  SeparatorMenuItem separator1 = new SeparatorMenuItem();
  SeparatorMenuItem separator1_5 = new SeparatorMenuItem();
  SeparatorMenuItem separator2 = new SeparatorMenuItem();
  SeparatorMenuItem separator3 = new SeparatorMenuItem();
  MenuItem adornerTitleLabel = new MenuItem();

  private void setContextMenuActions() {
    adornerTitleLabel.getStyleClass().add("context-menu-title");
    separator1.setDisable(true);

    selectStartNode.setOnAction(
        e -> {
          if (destinations.get(0).getDestinationCB().getValue() != null && startNode != null) {
            clearComboBoxValue(0);
          }

          if (rightClickedNode != null) {
            Node node = graph.nodeFromID(rightClickedNode.getId());
            if (node != null) {
              destinations.get(0).getDestinationCB().setValue(node.longName);
              mapInsertController.selectCircle(rightClickedNode);
            }
          }
        });

    selectEndNode.setOnAction(
        e -> {
          if (destinations.get(destinations.size() - 1).getDestinationCB().getValue() != null
              && endNode != null) {
            mapInsertController.deSelectCircle(endNode);
          }

          if (rightClickedNode != null) {
            Node node = graph.nodeFromID(rightClickedNode.getId());
            if (node != null) {
              destinations.get(destinations.size() - 1).getDestinationCB().setValue(node.longName);
              mapInsertController.selectCircle(rightClickedNode);
            }
          }
        });

    clearPath.setOnAction(
        e -> {
          clearDestinations();
        });

    flipPath.setOnAction(
        e -> {
          flipPath();
        });

    prevPath.setOnAction(
        e -> {
          if (rightClickedNode != null) {
            Node node = graph.nodeFromID(rightClickedNode.getId());
            List<Integer> nodeIndexs = indexOfAll(node, pathNodes);
            int floorChange = Integer.valueOf(mapInsertController.floorNumber);
            for (Integer nodeIndex : nodeIndexs) {
              floorChange = Integer.valueOf(pathNodes.get(nodeIndex - 1).floor);
              System.out.println(floorChange);
              if (floorChange - Integer.valueOf(mapInsertController.floorNumber) != 0) {
                break;
              }
            }
            handleFloorChanged(e, floorChange, false);
          }
        });

    nextPath.setOnAction(
        e -> {
          if (rightClickedNode != null) {
            Node node = graph.nodeFromID(rightClickedNode.getId());
            List<Integer> nodeIndexs = indexOfAll(node, pathNodes);
            int floorChange = Integer.valueOf(mapInsertController.floorNumber);
            for (Integer nodeIndex : nodeIndexs) {
              floorChange = Integer.valueOf(pathNodes.get(nodeIndex + 1).floor);
              System.out.println(floorChange);
              if (floorChange - Integer.valueOf(mapInsertController.floorNumber) != 0) {
                break;
              }
            }
            handleFloorChanged(e, floorChange, false);
          }
        });

    removeDestination.setOnAction(
        e -> {
          int index = 0;
          String name = graph.nodeFromID(rightClickedNode.getId()).getLongName();
          for (DestinationItemController dest : destinations) {
            if (((String) dest.getDestinationCB().getValue()).equals(name)) {
              break;
            }
            index++;
          }
          removeDestinations(index);
        });

    selectDestination.setOnAction(
        e -> {
          handleClickOnNode(rightClickedNode);
        });

    addDestination.setOnAction(
        e -> {
          initializeNewDestination();
          handleClickOnNode(rightClickedNode);
        });

    clearDestination.setOnAction(
        e -> {
          ComboBox selectedBox = null;
          int index = -1;
          for (DestinationItemController dest : destinations) {
            index++;
            if (dest.getDestinationCB().isFocused()) {
              selectedBox = dest.getDestinationCB();
            }
          }
          if (selectedBox != null) {
            selectedBox.setValue("");
          }
        });

    optimizePath.setOnAction(
        e -> {
          optimizePath();
        });
  }

  private void resetContextMenu() {
    if (mapContextMenu != null) {
      mapContextMenu.getItems().remove(0, mapContextMenu.getItems().size());
      mapContextMenu.hide();
    }
  }

  private void flipPath() {
    ArrayList<String> cbvalues = new ArrayList<>();
    for (DestinationItemController dest : destinations) {
      cbvalues.add((String) dest.getDestinationCB().getValue());
    }

    for (int i = 0; i < cbvalues.size(); i++) {
      destinations.get(i).getDestinationCB().setValue(cbvalues.get(cbvalues.size() - 1 - i));
    }
  }

  private void clearDestinations() {
    mapInsertController.clearSelection();
    int i = -1;
    for (DestinationItemController dest : destinations) {
      destinationsVBox.getChildren().remove(dest.getDestinationRootHBox());
    }
    intendedHeight = 90;
    destinations = new ArrayList<>();
    initializeNewDestination();
    initializeNewDestination();
    clearPath();
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
    handleFloorChanged(e, menuItemIndex, true);
  }

  private void handleFloorChanged(ActionEvent e, int menuItemIndex, boolean animate) {
    // This should be optimised to only switch if the floor actually changed, but its very fast, so
    // I cant be bothered
    mapInsertController.removeAllAdornerElements();
    mapInsertController.changeMapImage(
        mapInsertController.getMapOrder().get(menuItemIndex), animate);
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
        exitDirectionBtn.toFront();
      } else {
        textDirectionsBox.setVisible(true);
        textDirectionViewer.setVisible(true);
        exitDirectionBtn.setText("Exit Steps");
        parent.animateCenterColumnWidth(0);
        exitDirectionBtn.toFront();
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

    ArrayList<Label> labelList = new ArrayList<>();

    ArrayList<String> directionList = AlgorithmCalls.textDirections(pathNodes, endLocations);

    for (String direction : directionList) {

      Label newLabel = new Label(direction);
      newLabel.setMaxWidth(300);
      newLabel.setWrapText(true);

      for (String endLocation : endLocations) {
        if (direction.contains(graph.nodeFromID(endLocation).longName)
            && direction.contains("reached")) {
          newLabel.setStyle("-fx-font-weight:BOLD;");
          Image img = new Image("edu/wpi/cs3733/c21/teamY/images/directions/destinationIcon.png");
          ImageView view = new ImageView(img);
          view.setFitHeight(20);
          view.setPreserveRatio(true);
          newLabel.setGraphic(view);
          newLabel.setWrapText(true);
        }
        if (direction.contains("You have reached Floor")) {
          newLabel.setStyle("-fx-text-fill: GREEN; -fx-font-weight: BOLD;");
          newLabel.setOnMouseClicked(
              event -> {
                ActionEvent ae = new ActionEvent();
                handleFloorChanged(
                    ae,
                    Integer.valueOf(
                        direction
                            .substring(
                                direction.indexOf("reached Floor") + 13,
                                direction.indexOf("reached Floor") + 15)
                            .trim()),
                    false);
              });
        } else if (direction.contains("You have reached the Parking")) {
          newLabel.setStyle("-fx-text-fill: GREEN; -fx-font-weight: BOLD;");
          newLabel.setOnMouseClicked(
              event -> {
                ActionEvent ae = new ActionEvent();
                handleFloorChanged(ae, 0, false);
              });
        } else if (direction.contains("You begin")) {
          newLabel.setStyle("-fx-text-fill: GREEN; -fx-font-weight: BOLD;");
          newLabel.setOnMouseClicked(
              event -> {
                ActionEvent ae = new ActionEvent();
                handleFloorChanged(
                    ae,
                    Integer.valueOf(direction.substring(direction.indexOf("on Floor") + 8).trim()),
                    false);
              });
        }
      }

      if (direction.contains("turn around")) {
        Image img = new Image("edu/wpi/cs3733/c21/teamY/images/directions/leftTurnAround.png");
        ImageView view = new ImageView(img);
        view.setFitHeight(20);
        view.setPreserveRatio(true);
        newLabel.setWrapText(true);
        newLabel.setGraphic(view);
      }
      if (direction.contains("Turn left")) {
        Image img = new Image("edu/wpi/cs3733/c21/teamY/images/directions/arrow_turn_left.png");
        ImageView view = new ImageView(img);
        view.setFitHeight(20);
        view.setPreserveRatio(true);
        newLabel.setWrapText(true);
        newLabel.setGraphic(view);
      }
      if (direction.contains("Bear left")) {
        Image img = new Image("edu/wpi/cs3733/c21/teamY/images/directions/bearLeft.png");
        ImageView view = new ImageView(img);
        view.setFitHeight(20);
        view.setPreserveRatio(true);
        newLabel.setGraphic(view);
        newLabel.setWrapText(true);
      }
      if (direction.contains("Bear right")) {
        Image img = new Image("edu/wpi/cs3733/c21/teamY/images/directions/bearRight.png");
        ImageView view = new ImageView(img);
        view.setFitHeight(20);
        view.setPreserveRatio(true);
        newLabel.setGraphic(view);
        newLabel.setWrapText(true);
      }
      if (direction.contains("Turn right")) {
        Image img = new Image("edu/wpi/cs3733/c21/teamY/images/directions/turnRight.png");
        ImageView view = new ImageView(img);
        view.setFitHeight(20);
        view.setPreserveRatio(true);
        newLabel.setGraphic(view);
        newLabel.setWrapText(true);
      }
      if (direction.contains("Continue Straight")) {
        Image img = new Image("edu/wpi/cs3733/c21/teamY/images/directions/straight.png");
        ImageView view = new ImageView(img);
        view.setFitHeight(20);
        view.setPreserveRatio(true);
        newLabel.setGraphic(view);
        // newLabel.setWrapText(true);
      }
      if (direction.contains("use Staircase")) {
        Image img = new Image("edu/wpi/cs3733/c21/teamY/images/directions/stairs.png");
        ImageView view = new ImageView(img);
        view.setFitHeight(20);
        view.setPreserveRatio(true);
        newLabel.setGraphic(view);
        newLabel.setWrapText(true);
      } else if (direction.contains("use Elevator")) {
        Image img = new Image("edu/wpi/cs3733/c21/teamY/images/directions/elevator.png");
        ImageView view = new ImageView(img);
        view.setFitHeight(20);
        view.setPreserveRatio(true);
        newLabel.setGraphic(view);
        newLabel.setWrapText(true);
      }

      labelList.add(newLabel);
    }
    System.out.println(labelList.size());
    ArrayList<Integer> intList = new ArrayList<>();
    ArrayList<List<Label>> arrOfArr = new ArrayList<>();
    ArrayList<String> paneList = new ArrayList<>();

    for (int i = 0; i < labelList.size(); i++) {
      if (labelList
          .get(i)
          .getText()
          .contains("reached")) { // labelList.get(i).getText().contains("use Staircase")
        intList.add(i);
        paneList.add(labelList.get(i).getText());
      }
    }

    textDirectionViewer.getChildren().add(labelList.get(0));
    paneList.add(0, labelList.get(1).getText());

    Accordion accordion = new Accordion();
    TitledPane gridTitlePane;
    GridPane grid;

    System.out.println(intList.size());

    arrOfArr.add(labelList.subList(1, intList.get(0)));
    if (intList.size() == 1) {
      for (int i = 1; i < labelList.size() - 1; i++) {
        textDirectionViewer.getChildren().add(labelList.get(i));
      }

    } else {
      for (int i = 0; i < intList.size() - 1; i++) {
        arrOfArr.add(labelList.subList(intList.get(i), intList.get(i + 1)));
      }
      arrOfArr.add(labelList.subList(intList.get(intList.size() - 1), labelList.size() - 1));
    }

    System.out.println(arrOfArr.size());
    System.out.println(paneList.size());

    if (intList.size() != 1) {
      for (int i = 0; i < arrOfArr.size() - 1; i++) {
        gridTitlePane = new TitledPane();
        grid = new GridPane();
        grid.setVgap(4);

        // iterate through each arraylist and add it to a grid
        for (int j = 0; j < arrOfArr.get(i).size(); j++) {
          grid.add(arrOfArr.get(i).get(j), 0, j);
          arrOfArr.get(i).get(j).setWrapText(true);
        }

        gridTitlePane.setContent(grid);
        // replace text from arraylist of text
        gridTitlePane.setText(paneList.get(i));

        gridTitlePane.setWrapText(true);
        gridTitlePane.setExpanded(true);
        accordion.getPanes().add(gridTitlePane);
      }
    }

    textDirectionViewer.getChildren().add(accordion);
    textDirectionViewer.getChildren().add(labelList.get(labelList.size() - 1));
  }

  // PATHFINDING ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  private void optimizePath() {
    ArrayList<String> ends = new ArrayList<>();
    for (DestinationItemController dest : destinations) {
      if (dest.getDestinationCB().getValue() != null && dest.getDestinationCB().getValue() != "") {
        ends.add(graph.longNodes.get((String) dest.getDestinationCB().getValue()).nodeID);
        System.out.println("Making of ends: " + ends);
      }
    }
    clearDestinations();
    for (int k = 0; k < ends.size() - 2; k++) {
      initializeNewDestination();
    }
    String start = ends.get(0);
    ends.remove(0);
    endLocations = AlgorithmCalls.nearestNeighbor(graph, start, ends);

    mapInsertController.clearSelection();

    ArrayList<Node> algoNodes = runAlgo(graph, start, endLocations, noType);
    ArrayList<String> endLocationsNoDetours = endLocations;

    int i = 0;
    for (String endLocation : endLocationsNoDetours) {
      DestinationItemController dest = destinations.get(i + 1);
      if (i == 0) {
        destinations.get(i).getDestinationCB().setValue(graph.nodeFromID(start).longName);
      }
      dest.getDestinationCB().setValue(graph.nodeFromID(endLocation).longName);
      i++;
    }

    if (bathroom) {
      endLocations = AlgorithmCalls.dijkstraDetour(graph, algoNodes, endLocations, "REST");
      algoNodes = runAlgo(graph, start, endLocations, noType);
    }
    if (restaurant) {
      endLocations = AlgorithmCalls.dijkstraDetour(graph, algoNodes, endLocations, "FOOD");
      algoNodes = runAlgo(graph, start, endLocations, noType);
    }
    if (kiosk) {
      endLocations = AlgorithmCalls.dijkstraDetour(graph, algoNodes, endLocations, "KIOS");
      algoNodes = runAlgo(graph, start, endLocations, noType);
    }

    pathNodes = algoNodes;
    drawPath(pathNodes);

    generateTextDirections(pathNodes);
  }

  public void centerNodeInScrollPane(ScrollPane scrollPane, int index) {
    double lengthdown = scrollPane.getHeight() * (index) / (destinations.size() - 1);
    scrollPane.layout();
    scrollPane.setVvalue(lengthdown);
    /*
    Timeline timeline = new Timeline();
    timeline.getKeyFrames().clear();
    timeline
        .getKeyFrames()
        .add(
            new KeyFrame(
                Duration.millis(500), new KeyValue(scrollPane.vvalueProperty(), lengthdown)));
    timeline.play();*/
  }

  private ComboBox initializeNewDestination() {
    if (parent.isDesktop) {
      intendedHeight += 40;
      double newHeight = (intendedHeight >= 370) ? 370 : intendedHeight;
      Timeline timeline = new Timeline();
      KeyValue kv2 =
          new KeyValue(navigationHeaderVBox.minHeightProperty(), newHeight, Interpolator.EASE_IN);
      KeyFrame kf = new KeyFrame(Duration.seconds(0.25), kv2);
      timeline.getKeyFrames().add(kf);
      timeline.play();
    }

    FXMLLoader fxmlLoader = new FXMLLoader();
    DestinationItemController controller = null;
    ComboBox output = null;
    try {

      javafx.scene.Node node =
          fxmlLoader.load(getClass().getResource("DestinationItem.fxml").openStream());
      controller = (DestinationItemController) fxmlLoader.getController();
      controller.populateComboBox(nodes);
      controller.index = destinations.size();
      destinations.add(controller);
      destinationsVBox.getChildren().add(node);
      output = controller.getDestinationCB();

      ComboBox destCB = controller.getDestinationCB();

      controller.getIndexLabel().setText("" + (controller.index + 1) + ".");
      controller
          .getIndexLabel()
          .setMinWidth(
              controller.getIndexLabel().getText().length()
                      * controller.getIndexLabel().getFont().getSize()
                  + 2);
      // Enter Pressed Event
      destCB.setOnKeyPressed(
          e -> {
            if (e.getCode() == KeyCode.ENTER) {
              calculatePath();
            }
          });

      // Value changed event
      destCB
          .getSelectionModel()
          .selectedItemProperty()
          .addListener(
              (options, oldValue, newValue) -> {
                if ((oldValue == null && newValue != null) || (!oldValue.equals(newValue))) {
                  calculatePath();
                }
              });

      // Last selected ComboBox setting
      destCB.setOnAction(e -> lastSelectedComboBox = destCB);

      DestinationItemController finalController = controller;

      controller
          .getUpBtn()
          .setOnAction(
              e -> {
                swapDestinations(finalController.index, finalController.index - 1);
                updateDestinationIndeces();
              });
      controller
          .getDownBtn()
          .setOnAction(
              e -> {
                swapDestinations(finalController.index, finalController.index + 1);
                updateDestinationIndeces();
              });

      controller
          .getRemoveBtn()
          .setOnAction(
              e -> {
                removeDestinations(finalController.index);
                updateDestinationIndeces();
              });

      refreshEnabledButtons();
      controller.getDestinationCB().requestFocus();
      lastSelectedComboBox = controller.getDestinationCB();
      centerNodeInScrollPane(
          destinationScrollPane, controller.index); // controller.getDestinationRootHBox());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return output;
  }

  // swaps text in comboboxes at ints a and b
  private void swapDestinations(int a, int b) {
    if (a < destinations.size() && b < destinations.size() && a >= 0 && b >= 0) {
      String dest1 = (String) destinations.get(b).getDestinationCB().getValue();
      destinations
          .get(b)
          .getDestinationCB()
          .setValue(destinations.get(a).getDestinationCB().getValue());
      destinations.get(a).getDestinationCB().setValue(dest1);
      calculatePath();
    }
  }

  private void removeDestinations(int index) {
    if (parent.isDesktop) {
      intendedHeight -= 40;
      double newHeight =
          (intendedHeight <= 170) ? 170 : (intendedHeight >= 370) ? 370 : intendedHeight;
      Timeline timeline = new Timeline();
      KeyValue kv2 =
          new KeyValue(navigationHeaderVBox.minHeightProperty(), newHeight, Interpolator.EASE_IN);
      KeyFrame kf = new KeyFrame(Duration.seconds(0.25), kv2);
      timeline.getKeyFrames().add(kf);
      timeline.play();
    }
    if (index < destinations.size() && index >= 0) {
      clearComboBoxValue(index);

      destinationsVBox.getChildren().remove(destinations.get(index).getDestinationRootHBox());
      destinations.remove(index);
      refreshEnabledButtons();
      updateDestinationIndeces();
      calculatePath();
    }
  }

  private void updateDestinationIndeces() {
    for (int i = 0; i < destinations.size(); i++) {
      destinations.get(i).index = i;
      destinations.get(i).getIndexLabel().setText("" + (i + 1) + ".");
      destinations
          .get(i)
          .getIndexLabel()
          .setMinWidth(
              destinations.get(i).getIndexLabel().getText().length()
                      * destinations.get(i).getIndexLabel().getFont().getSize()
                  + 2);
    }
  }

  private void refreshEnabledButtons() {
    boolean closeButtonsEnabled = destinations.size() > 2;
    for (DestinationItemController dest : destinations) {
      dest.getRemoveBtn().setDisable(!closeButtonsEnabled);
      dest.getUpBtn().setDisable(false);
      dest.getDownBtn().setDisable(false);
    }
    destinations.get(0).getUpBtn().setDisable(true);
    destinations.get(destinations.size() - 1).getDownBtn().setDisable(true);
  }

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
    for (DestinationItemController dest : destinations) {
      dest.populateComboBox(nodes);
    }
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

    ArrayList<DestinationItemController> effectiveDests = new ArrayList<>();
    int numLocations = 0;
    for (DestinationItemController dest : destinations) {
      if (dest.getDestinationCB().getValue() != null
          && !dest.getDestinationCB().getValue().equals("")) {
        numLocations++;
        effectiveDests.add(dest);
      }
    }

    if (numLocations > 1
        && destinations.get(0).getDestinationCB().getValue() != null
        && !destinations.get(0).getDestinationCB().getValue().equals("")) {

      String startID =
          graph.longNodes.get((String) effectiveDests.get(0).getDestinationCB().getValue()).nodeID;

      for (int i = 1; i < effectiveDests.size(); i++) {
        DestinationItemController dest = effectiveDests.get(i);
        if (dest.getDestinationCB().getValue() != null) {
          String endID =
              graph.longNodes.get((String) dest.getDestinationCB().getValue())
                  .nodeID; // (String) endLocationBox.getValue();
          endLocations.add(endID);
        }
      }

      if (graph
          .longNodes
          .get((String) effectiveDests.get(0).getDestinationCB().getValue())
          .nodeType
          .equals("PARK")) {
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

      if (bathroom) {
        endLocations = AlgorithmCalls.dijkstraDetour(graph, algoNodes, endLocations, "REST");
        algoNodes = runAlgo(graph, startID, endLocations, noType);
      }
      if (restaurant) {
        endLocations = AlgorithmCalls.dijkstraDetour(graph, algoNodes, endLocations, "FOOD");
        algoNodes = runAlgo(graph, startID, endLocations, noType);
      }
      if (kiosk) {
        endLocations = AlgorithmCalls.dijkstraDetour(graph, algoNodes, endLocations, "KIOS");
        algoNodes = runAlgo(graph, startID, endLocations, noType);
      }

      pathNodes = algoNodes;
      drawPath(pathNodes);

      generateTextDirections(pathNodes);
    }
  }

  /**
   * drawPath Selects all nodes and edges in passed in path
   *
   * @param nodes is the path passed in
   */
  private void drawPath(ArrayList<Node> nodes) {
    if (nodes != null) {
      mapInsertController.clearSelection();
      pathActive = true;
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
        ArrayList<Node> neighbors = nodes.get(i).getNeighbors();

        for (Node neighbor : neighbors) {
          if (n != null && !neighbor.floor.equals(nodes.get(i).floor)) {
            mapInsertController.selectCircle(n);
            break;
          }
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
    pathActive = false;
    mapInsertController.clearSelection();
    pathNodes = new ArrayList<Node>();
    endLocations = new ArrayList<String>();
    textDirectionViewer.getChildren().clear();
  }

  public JFXButton getBathroomBtn() {
    return bathroomBtn;
  }

  public <T> List<Integer> indexOfAll(T obj, List<T> list) {
    final List<Integer> indexList = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      if (obj.equals(list.get(i))) {
        indexList.add(i);
      }
    }
    return indexList;
  }
}
