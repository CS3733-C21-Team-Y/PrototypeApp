package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import edu.wpi.cs3733.c21.teamY.algorithms.AStarI;
import edu.wpi.cs3733.c21.teamY.algorithms.AlgoContext;
import edu.wpi.cs3733.c21.teamY.algorithms.BFSI;
import edu.wpi.cs3733.c21.teamY.algorithms.DFSI;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.JDBCUtils;
import edu.wpi.cs3733.c21.teamY.entity.Edge;
import edu.wpi.cs3733.c21.teamY.entity.Node;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GraphEditPageController extends SubPage {

  @FXML private Pane anchor;
  @FXML private HBox header;
  @FXML private VBox mapBox;
  @FXML private HBox hBox;

  //  @FXML private Button toHomeBtn;
  @FXML private Button addNode;
  @FXML private Button loadNodesButton;

  @FXML private CheckBox addNodecb;
  @FXML private CheckBox addEdgecb;

  @FXML private Button deleteNode;
  @FXML private Button export;

  @FXML private Button addEdge;

  @FXML private Text nodeDisplay = new Text("Selected Node");
  @FXML private Text edgeDisplay = new Text("Selected Edge");

  // variables for selecting points and locating edges
  private boolean startEdgeFlag = true;
  private double startx, starty, endx, endy;
  private String startNodeID, endNodeID;

  @FXML private TextField newX;
  @FXML private TextField newY;

  @FXML private StackPane stackPane;

  @FXML private Button toolTip;
  @FXML private Button resetView;

  @FXML private SplitMenuButton selectNewAlgo;
  @FXML private MenuItem depthFirst;
  @FXML private MenuItem breadthFirst;
  @FXML private MenuItem aStar;

  //  @FXML private MenuItem setFloorThreePage;
  //  @FXML private MenuItem setFloorFourPage;
  //  @FXML private MenuItem setFloorFivePage;

  private ArrayList<Edge> edges = new ArrayList<Edge>();
  private ArrayList<Node> nodes = new ArrayList<Node>();

  private int nodeIDCounter;
  private boolean shiftPressed = false;

  private double minimumSelectionMove = 10;

  @FXML private JFXButton panUpButton;
  @FXML private JFXButton panDownButton;
  @FXML private JFXButton panRightButton;
  @FXML private JFXButton panLeftButton;
  @FXML private JFXButton zoomInButton;
  @FXML private JFXButton zoomOutButton;

  @FXML private JFXButton moveNodeUpButton;
  @FXML private JFXButton moveNodeDownButton;
  @FXML private JFXButton moveNodeLeftButton;
  @FXML private JFXButton moveNodeRightButton;
  @FXML private ComboBox startLocationBox;
  @FXML private ComboBox endLocationBox;

  @FXML private EditNodeTableController editNodeTableController;
  @FXML private MapController mapInsertController;

  private double dragStartXRelativeEdge;
  private double dragStartYRelativeEdge;

  private double dragStartX;
  private double dragStartY;

  private boolean isDraggingAdorner = false;
  private ArrayList<MapController.CircleEx> nodesAffectedByDrag =
      new ArrayList<MapController.CircleEx>();
  private ArrayList<MapController.LineEx> edgesAffectedByDrag =
      new ArrayList<MapController.LineEx>();

  private javafx.scene.Node jfxNodeBeingDragged = null;
  private Rectangle rectangleSelection = null;

  JFXDialog dialog = new JFXDialog();

  public GraphEditPageController() {}

  private void loadMap() {
    FXMLLoader fxmlLoader = new FXMLLoader();
    try {
      javafx.scene.Node node =
          fxmlLoader.load(getClass().getResource("MapUserControl.fxml").openStream());
      mapInsertController = (MapController) fxmlLoader.getController();
      mapInsertController.setParent(parent);
      stackPane.getChildren().add(node);
      node.toFront();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void loadTable() {
    FXMLLoader fxmlLoader = new FXMLLoader();
    try {
      javafx.scene.Node node =
          fxmlLoader.load(getClass().getResource("EditNodeTable.fxml").openStream());
      editNodeTableController = (EditNodeTableController) fxmlLoader.getController();
      editNodeTableController.setParent(parent);
      hBox.getChildren().add(node);
      node.prefWidth(9999);
      node.toFront();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void initialize() {

    loadMap();
    loadTable();
    anchor.setOnKeyPressed(
        e -> {
          mapInsertController.scrollOnPress(e);

          // Should be improved
          if (e.isShiftDown()) {
            shiftPressed = true;
          } else {
            shiftPressed = false;
          }
        });

    anchor.setOnKeyReleased(
        e -> {
          mapInsertController.scrollOnRelease(e);

          if (e.isShiftDown()) {
            shiftPressed = true;
          } else {
            shiftPressed = false;
          }
        });

    resetView.setOnAction(e -> mapInsertController.resetMapView());
    resetView.toFront();
    mapInsertController.containerStackPane.setOnScroll(e -> mapInsertController.zoom(e));

    panUpButton.setOnAction(e -> mapInsertController.panOnButtons("up"));
    panDownButton.setOnAction(e -> mapInsertController.panOnButtons("down"));
    panLeftButton.setOnAction(e -> mapInsertController.panOnButtons("left"));
    panRightButton.setOnAction(e -> mapInsertController.panOnButtons("right"));
    zoomInButton.setOnAction(e -> mapInsertController.zoomOnButtons(0.05));
    zoomOutButton.setOnAction(e -> mapInsertController.zoomOnButtons(-0.05));

    moveNodeUpButton.setOnAction(
        e -> {
          // up
          moveSelectedCirclesBy(mapInsertController.getSelectedNodes(), 0, 0 - getOneTapMoveDist());
          updateNodePositionsInDB();
        });
    moveNodeDownButton.setOnAction(
        e -> {
          // down
          moveSelectedCirclesBy(mapInsertController.getSelectedNodes(), 0, getOneTapMoveDist());
          updateNodePositionsInDB();
        });
    moveNodeLeftButton.setOnAction(
        e -> {
          // left
          moveSelectedCirclesBy(mapInsertController.getSelectedNodes(), 0 - getOneTapMoveDist(), 0);
          updateNodePositionsInDB();
        });
    moveNodeRightButton.setOnAction(
        e -> {
          // right
          moveSelectedCirclesBy(mapInsertController.getSelectedNodes(), getOneTapMoveDist(), 0);
          updateNodePositionsInDB();
        });

    selectNewAlgo.setText("Select Algorithm");

    dialog.setContent(
        new Label(
            " Scroll to Zoom"
                + "\n Hold CTRL + Scroll to Pan Up and down"
                + "\n Hold SHIFT + Scroll to Pan left and right"
                + "\n Hold SHIFT to select multiple nodes"
                + "\n Reset brings back the original framing"));
    toolTip.setOnAction((action) -> dialog.show(stackPane));
    toolTip.toFront();

    initImage();
    mapInsertController.getFloorMenu().setText("Parking Lot");

    loadNodesButton.setOnAction(
        e -> {
          loadMapFromCSV();
          resetComboBoxes();
        });

    int i = 0;
    for (MenuItem menuItem : mapInsertController.getFloorMenu().getItems()) {
      int index = i;
      menuItem.setOnAction(
          e -> {
            mapInsertController.removeAllAdornerElements();
            mapInsertController.changeMapImage(mapInsertController.getMapOrder().get(index));
            mapInsertController.updateMenuPreview(e, mapInsertController.getFloorMenu());
          });
      i++;
    }

    // attaches a handler to the button with a lambda expression
    //    toHomeBtn.setOnAction(e -> buttonClicked(e));
    export.setOnAction(
        e -> {
          try {
            DataOperations.DBtoCSV("NODE");
            DataOperations.DBtoCSV("EDGE");
          } catch (SQLException throwables) {
            throwables.printStackTrace();
          }
        });

    // run the create methods on the button click
    addNode.setOnAction(
        e -> {
          createNode(e);
        });

    deleteNode.setOnAction(
        e -> {
          removeSelected(e);
        });

    // when checkbox is selected, unselect the other
    addEdgecb.setOnAction(
        e -> {
          startEdgeFlag = true;
          addNodecb.setSelected(false);
        });
    addNodecb.setOnAction(
        e -> {
          addEdgecb.setSelected(false);
        });

    // Mouse Down Override
    mapInsertController
        .getAdornerPane()
        .setOnMousePressed(
            e -> {
              mapInsertController.defaultOnMousePressed(e);
              dragStartX = e.getX();
              dragStartY = e.getY();

              boolean clickedNode =
                  e.getPickResult().getIntersectedNode() instanceof MapController.CircleEx;
              boolean clickedEdge =
                  e.getPickResult().getIntersectedNode() instanceof MapController.LineEx;
              if ((clickedNode || clickedEdge)) {
                // Tentatively initiate them for drag
                boolean headHasFocus = false;
                if (clickedNode) {
                  jfxNodeBeingDragged =
                      (MapController.CircleEx) e.getPickResult().getIntersectedNode();
                  headHasFocus = ((MapController.CircleEx) jfxNodeBeingDragged).hasFocus;
                } else if (clickedEdge) {
                  jfxNodeBeingDragged =
                      (MapController.LineEx) e.getPickResult().getIntersectedNode();
                  headHasFocus = ((MapController.LineEx) jfxNodeBeingDragged).hasFocus;
                } else {
                  // Cannot pass
                  return;
                }

                if (clickedEdge) {
                  dragStartXRelativeEdge =
                      e.getX() - ((MapController.LineEx) jfxNodeBeingDragged).getStartX();
                  dragStartYRelativeEdge =
                      e.getY() - ((MapController.LineEx) jfxNodeBeingDragged).getStartY();
                }

                // already selected one or more nodes/edges
                if (headHasFocus) {
                  nodesAffectedByDrag = mapInsertController.getSelectedNodes();
                  edgesAffectedByDrag = mapInsertController.getSelectedEdges();
                }
                // Starting selection from scratch
                else {
                  if (clickedNode) {
                    nodesAffectedByDrag.add((MapController.CircleEx) jfxNodeBeingDragged);
                  } else if (clickedEdge) {
                    edgesAffectedByDrag.add((MapController.LineEx) jfxNodeBeingDragged);
                  }
                }

                // adding dangly nodes that are part of loose edges

                for (MapController.LineEx edge : edgesAffectedByDrag) {

                  if (edge.startNode != null && !nodesAffectedByDrag.contains(edge.startNode)) {
                    nodesAffectedByDrag.add(edge.startNode);
                  }
                  if (edge.endNode != null && !nodesAffectedByDrag.contains(edge.endNode)) {
                    nodesAffectedByDrag.add(edge.endNode);
                  }
                }
              }
            });

    // Mouse Dragged Override
    mapInsertController
        .getAdornerPane()
        .setOnMouseDragged(
            e -> {
              if (nodesAffectedByDrag.size() > 0) {
                isDraggingAdorner = true;

                mapInsertController.clearSelection();
                mapInsertController.selectCirclesFromList(nodesAffectedByDrag);
                mapInsertController.selectLinesFromList(edgesAffectedByDrag);
                // Circle
                if (jfxNodeBeingDragged instanceof MapController.CircleEx) {
                  MapController.CircleEx handle = (MapController.CircleEx) jfxNodeBeingDragged;
                  double deltaX = e.getX() - handle.getCenterX();
                  double deltaY = e.getY() - handle.getCenterY();
                  moveSelectedCirclesBy(mapInsertController.getSelectedNodes(), deltaX, deltaY);
                }
                // Line
                else if (jfxNodeBeingDragged instanceof MapController.LineEx) {
                  MapController.LineEx handle = (MapController.LineEx) jfxNodeBeingDragged;
                  double deltaX = e.getX() - handle.getStartX() - dragStartXRelativeEdge;
                  double deltaY = e.getY() - handle.getStartY() - dragStartYRelativeEdge;
                  moveSelectedCirclesBy(mapInsertController.getSelectedNodes(), deltaX, deltaY);
                }
              } else if (shiftPressed) {
                if (rectangleSelection == null) {
                  rectangleSelection = new Rectangle();
                  rectangleSelection.setX(dragStartX);
                  rectangleSelection.setY(dragStartY);

                  rectangleSelection.setStroke(Paint.valueOf("Blue"));
                  rectangleSelection.setFill(Paint.valueOf("TRANSPARENT"));

                  mapInsertController.getAdornerPane().getChildren().add(rectangleSelection);
                }

                rectangleSelection.setStrokeWidth(
                    1 / mapInsertController.getAdornerPane().getScaleX());

                rectangleSelection.setX(dragStartX);
                rectangleSelection.setY(dragStartY);

                double desiredWidth = e.getX() - rectangleSelection.getX();
                double desiredHeight = e.getY() - rectangleSelection.getY();

                if (desiredWidth < 0) {
                  rectangleSelection.setX(dragStartX + desiredWidth);
                  desiredWidth *= -1;
                }
                if (desiredHeight < 0) {
                  rectangleSelection.setY(dragStartY + desiredHeight);
                  desiredHeight *= -1;
                }

                rectangleSelection.setWidth(desiredWidth);
                rectangleSelection.setHeight(desiredHeight);
              } else {
                mapInsertController.defaultOnMouseDragged(e);
              }
            });

    // Mouse Up Override
    mapInsertController
        .getAdornerPane()
        .setOnMouseReleased(
            e -> {
              // If not letting go of a drag
              mapInsertController.defaultOnMouseReleased(e);

              if (!mapInsertController.wasLastClickDrag()
                  && !isDraggingAdorner
                  && rectangleSelection == null) {

                // Node
                if (e.getPickResult().getIntersectedNode() instanceof MapController.CircleEx) {
                  if (addEdgecb.isSelected()
                      && mapInsertController.getSelectedNodes().size() == 1) {
                    createEdge((MapController.CircleEx) e.getPickResult().getIntersectedNode());
                  } else {
                    handleClickOnNode(
                        (MapController.CircleEx) e.getPickResult().getIntersectedNode());
                  }
                }
                // Edge
                else if (e.getPickResult().getIntersectedNode() instanceof MapController.LineEx) {
                  handleClickOnEdge((MapController.LineEx) e.getPickResult().getIntersectedNode());
                }
                // Blank Map
                else {
                  // Clicked on blank map
                  mapInsertController.defaultOnMouseReleased(e);

                  if (!mapInsertController.wasLastClickDrag()) {
                    // If wasnt a drag, but clicked on blank map
                    if (addNodecb.isSelected()) {
                      mapInsertController.clearSelection();
                      createNodecb(e);
                    } else {
                      if (!shiftPressed
                          && startEdgeFlag
                          && !(e.getPickResult().getIntersectedNode()
                                  instanceof MapController.CircleEx
                              || e.getPickResult().getIntersectedNode()
                                  instanceof MapController.LineEx))
                        mapInsertController.clearSelection();
                    }
                  }
                }
              }
              // If this is a node drag end
              else if (isDraggingAdorner
                  && !mapInsertController.wasLastClickDrag()
                  && rectangleSelection == null) {
                updateNodePositionsInDB();
              } else if (rectangleSelection != null) {
                ArrayList<MapController.CircleEx> intersectedCircles =
                    new ArrayList<MapController.CircleEx>();
                ArrayList<MapController.LineEx> intersectedLines =
                    new ArrayList<MapController.LineEx>();

                for (javafx.scene.Node adorner :
                    mapInsertController.getAdornerPane().getChildren()) {
                  if (adorner instanceof MapController.CircleEx) {
                    if (rectangleSelection.intersects(adorner.getBoundsInLocal())) {
                      intersectedCircles.add((MapController.CircleEx) adorner);
                    }
                  } else if (adorner instanceof MapController.LineEx) {
                    if (rectangleSelection.intersects(adorner.getBoundsInLocal())) {
                      intersectedLines.add((MapController.LineEx) adorner);
                    }
                  }
                }

                mapInsertController.selectCirclesFromList(intersectedCircles);
                mapInsertController.selectLinesFromList(intersectedLines);
                mapInsertController.getAdornerPane().getChildren().remove(rectangleSelection);
                rectangleSelection = null;
              }

              // Needs to happen anyway because of tentative selecition
              isDraggingAdorner = false;
              nodesAffectedByDrag = new ArrayList<MapController.CircleEx>();
              edgesAffectedByDrag = new ArrayList<MapController.LineEx>();
              rectangleSelection = null; // just in case

              jfxNodeBeingDragged = null;
            });

    // Node selection menus Keys
    addEdge.setOnAction(
        e -> {
          if (startLocationBox.getValue() != null && endLocationBox.getValue() != null) {
            MapController.CircleEx startNode = null;
            MapController.CircleEx endNode = null;
            try {
              startNode =
                  (MapController.CircleEx)
                      mapInsertController
                          .getAdornerPane()
                          .lookup("#" + (String) startLocationBox.getValue());
              endNode =
                  (MapController.CircleEx)
                      mapInsertController
                          .getAdornerPane()
                          .lookup("#" + (String) endLocationBox.getValue());
            } catch (Exception eeeee) {
              return;
            }

            if (startNode != null && endNode != null) {
              createEdge(startNode, endNode);
            }
          }
        });
    Platform.runLater(
        () -> {
          resetComboBoxes();

          Stage stage = (Stage) toolTip.getScene().getWindow();
          StageInformation info = (StageInformation) stage.getUserData();
          if (info.getAlgorithmSelection().getContext() == null) {
            info.setAlgorithmSelection(new AlgoContext());
            info.getAlgorithmSelection().setContext(new AStarI());
            stage.setUserData(info);
          }
          depthFirst.setOnAction(
              e -> {
                info.getAlgorithmSelection().setContext(new DFSI());
                stage.setUserData(info);
              });
          breadthFirst.setOnAction(
              e -> {
                info.getAlgorithmSelection().setContext(new BFSI());
                stage.setUserData(info);
              });
          aStar.setOnAction(
              e -> {
                info.getAlgorithmSelection().setContext(new AStarI());
                stage.setUserData(info);
              });
        });
  }

  private void handleClickOnNode(MapController.CircleEx node) {
    // Node is selected
    if (node.hasFocus) {
      // If shift is pressed, deselect only clicked node
      if (shiftPressed) {
        mapInsertController.deSelectCircle(node);
      }
      // if shift not pressed, but multiple things are selected, only select clicked node
      else if (mapInsertController.getSelectedNodes().size() > 1
          || mapInsertController.getSelectedEdges().size() > 1) {
        mapInsertController.clearSelection();
        mapInsertController.selectCircle((MapController.CircleEx) node);
      }
      // if its the only thing selected and u click again, it turns off
      else {
        mapInsertController.clearSelection();
      }
    }
    // Node is not selected
    else {
      if (!shiftPressed) {
        // No shift means clear and only select clicked
        mapInsertController.clearSelection();
        mapInsertController.selectCircle((MapController.CircleEx) node);
      } else {
        // Shift adds node to selection
        mapInsertController.selectCircle((MapController.CircleEx) node);
      }
    }
  }

  private void resetComboBoxes() {
    startLocationBox.getItems().remove(0, startLocationBox.getItems().size());
    endLocationBox.getItems().remove(0, endLocationBox.getItems().size());

    for (Node node : nodes) {
      startLocationBox.getItems().add(node.nodeID);
    }

    for (Node node : nodes) {
      endLocationBox.getItems().add(node.nodeID);
    }
  }

  private void handleClickOnEdge(MapController.LineEx edge) {
    // Node is selected
    if (edge.hasFocus) {
      // If shift is pressed, deselect only clicked node
      if (shiftPressed) {
        mapInsertController.deSelectLine((MapController.LineEx) edge);
      }
      // if shift not pressed, but multiple things are selected, only select clicked edge
      else if (mapInsertController.getSelectedNodes().size() > 1
          || mapInsertController.getSelectedEdges().size() > 1) {

        // Weird bug. selected nodes returns > 1 because adjacent nodes are selected when clicking
        // on lit
        if ((mapInsertController.getSelectedNodes().contains(edge.startNode)
                || mapInsertController.getSelectedNodes().contains(edge.endNode))
            && (!edge.startNode.hasFocus || !edge.endNode.hasFocus)) {
          mapInsertController.clearSelection();
        } else {
          // System.out.println(mapInsertController.getSelectedNodes());

          mapInsertController.clearSelection();
          mapInsertController.selectLine((MapController.LineEx) edge);
        }
      }
      // if its the only thing selected and u click again, it turns off
      else {
        mapInsertController.clearSelection();
      }
    }
    // Node is not selected
    else {
      if (!shiftPressed) {
        // No shift means clear and only select clicked
        mapInsertController.clearSelection();
        mapInsertController.selectLine((MapController.LineEx) edge);
      } else {
        // Shift adds node to selection
        mapInsertController.selectLine((MapController.LineEx) edge);
      }
    }
  }

  private void loadMapFromCSV() {
    mapInsertController.removeAllAdornerElements();

    nodeIDCounter = nodes.size() + 1;

    nodes = mapInsertController.loadNodesFromCSV();
    edges = mapInsertController.loadEdgesFromCSV();
    mapInsertController.addAdornerElements(nodes, edges, mapInsertController.floorNumber);
  }

  private void initImage() {
    mapInsertController.changeMapImage(MapController.MAP_PAGE.PARKING);
    //    map.setFitHeight(500);
    //    map.fitHeightProperty().bind(anchor.heightProperty());
    //    map.fitWidthProperty().bind(anchor.widthProperty());
  }

  private void removeSelected(ActionEvent e) {
    ArrayList<String> nodeIDs = new ArrayList<String>();
    for (MapController.CircleEx node : mapInsertController.getSelectedNodes()) {
      nodeIDs.add(node.getId());
    }

    ArrayList<String> edgeIDs = new ArrayList<String>();
    for (MapController.LineEx edge : mapInsertController.getSelectedEdges()) {
      nodeIDs.add(edge.getId());
    }

    for (String nodeId : nodeIDs) {
      JDBCUtils.deleteNode(nodeId);
    }

    for (String edgeId : edgeIDs) {
      JDBCUtils.deleteNode(edgeId);
    }

    mapInsertController.removeSelected();
  }

  // button event handler
  //  @FXML
  //  private void buttonClicked(ActionEvent e) {
  //    // error handling for FXMLLoader.load
  //    try {
  //      // initializing stage
  //      Stage stage = null;
  //
  //      if (e.getSource() == toHomeBtn) {
  //        // gets the current stage
  //        stage = (Stage) toHomeBtn.getScene().getWindow();
  //        // sets the new scene to the alex page
  //        CSV.DBtoCSV("NODE");
  //        CSV.DBtoCSV("EDGE");
  //        ActiveGraph.initialize();
  //        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("HomePage.fxml"))));
  //
  //      } else {
  //
  //      }
  //
  //      // display new stage
  //      stage.show();
  //    } catch (Exception exp) {
  //    }
  //  }

  // creates edge
  private void createEdge(MapController.CircleEx endNode) {
    // creates an edge between two selected points when the checkbox is selected
    ArrayList<MapController.CircleEx> selectedNodes = mapInsertController.getSelectedNodes();
    if (addEdgecb.isSelected() && selectedNodes.size() == 1 && endNode != selectedNodes.get(0)) {

      MapController.CircleEx lastSelectedNode = selectedNodes.get(0);
      try {
        startNodeID = lastSelectedNode.getId();
        startx = lastSelectedNode.getCenterX();
        starty = lastSelectedNode.getCenterY();
      } catch (Exception exception) {
        System.out.println("Could not identify start point");
        return;
      }

      try {
        endNodeID = endNode.getId();
        endx = endNode.getCenterX();
        endy = endNode.getCenterY();
      } catch (Exception exception) {
        System.out.println("Could not identify end point");
        return;
      }

      // creating the line and adding as a child to the pane
      String edgeID = startNodeID + "_" + endNodeID;
      Edge ed = new Edge(edgeID, startNodeID, endNodeID);

      // JDBCUtils.insert(3, ed, "EDGE");
      // JDBCUtils.insert(JDBCUtils.insertString(ed));

      try {
        //          DatabaseQueryAdministrator.insertEdge(ed);
        JDBCUtils.insert(3, ed, "Edge");
      } catch (Exception exception) {
        System.out.println("nodeEdgeDispController.createEdgecb");
        return;
      }
      //        CSV.saveEdge(ed);
      mapInsertController.clearSelection();
      mapInsertController.selectLine(mapInsertController.addEdgeLine(ed));
    }
  }

  private void createEdge(MapController.CircleEx startNode, MapController.CircleEx endNode) {

    try {
      startNodeID = startNode.getId();
      startx = startNode.getCenterX();
      starty = startNode.getCenterY();
    } catch (Exception exception) {
      System.out.println("Could not identify start point");
      return;
    }

    try {
      endNodeID = endNode.getId();
      endx = endNode.getCenterX();
      endy = endNode.getCenterY();
    } catch (Exception exception) {
      System.out.println("Could not identify end point");
      return;
    }

    // creating the line and adding as a child to the pane
    String edgeID = startNodeID + "_" + endNodeID;
    Edge ed = new Edge(edgeID, startNodeID, endNodeID);

    // JDBCUtils.insert(3, ed, "EDGE");
    // JDBCUtils.insert(JDBCUtils.insertString(ed));

    try {
      //          DatabaseQueryAdministrator.insertEdge(ed);
      JDBCUtils.insert(3, ed, "Edge");
    } catch (Exception exception) {
      System.out.println("nodeEdgeDispController.createEdgecb");
      return;
    }
    //        CSV.saveEdge(ed);
    mapInsertController.clearSelection();
    mapInsertController.selectLine(mapInsertController.addEdgeLine(ed));
  }

  private void createNodecb(MouseEvent e) {
    // when the add node checkbox is selected, the new nodes can be created
    // wherever the mouse clicks withing the scene
    String nodeID = String.valueOf(nodeIDCounter);
    nodeIDCounter++;
    if (addNodecb.isSelected()) {
      Node n =
          new Node(
              Math.floor(mapInsertController.scaleUpXCoords(e.getX())),
              Math.floor(mapInsertController.scaleUpYCoords(e.getY())),
              mapInsertController.floorNumber,
              nodeID);
      // JDBCUtils.insert(10, n, "NODE");
      // JDBCUtils.insert(JDBCUtils.insertString(n));
      //    JDBCUtils.insert(9, n, "Node");

      try {
        DataOperations.insert(9, n, "Node");
      } catch (Exception exception) {
        System.out.println("nodeEdgeDispController.createNodecb");
        return;
      }
      mapInsertController.clearSelection();
      MapController.CircleEx c = mapInsertController.addNodeCircle(n);
      mapInsertController.selectCircle(c);
    }
  }

  private void createNode(ActionEvent e) {
    // creates a new instance of the local node class and creates a red circle
    // to add as a child of the pane in the scene
    String nodeID = String.valueOf(nodeIDCounter);
    nodeIDCounter++;
    try {
      Node n =
          new Node(
              mapInsertController.scaleUpXCoords(Double.parseDouble(newX.getText())),
              mapInsertController.scaleUpYCoords(Double.parseDouble(newY.getText())),
              mapInsertController.floorNumber,
              nodeID);

      // JDBCUtils.insert(10, n, "NODE");
      // JDBCUtils.insert(JDBCUtils.insertString(n));

      DataOperations.insert(9, n, "Node");

    } catch (Exception exception) {
      System.out.println("Can't create a node with text in the field input");
    }
  }

  private void updateNodePositionsInDB() {
    for (MapController.CircleEx c : movedNodes) {
      // System.out.println("updating DB with moved nodes");
      DataOperations.updateNodeCoordsOnly(
          c.getId(),
          Math.floor(mapInsertController.scaleUpXCoords(c.getCenterX())),
          Math.floor(mapInsertController.scaleUpYCoords(c.getCenterY())));
    }

    // Absolutely not. do not update active graph every time you move a node thats rediculouis
    /*
    try {
      ActiveGraph.initialize();
    } catch (Exception exception) {
      System.out.println("GraphEditPageController.updateNodes");
    }*/
    mapInsertController.updateMapScreen();
  }

  private ArrayList<MapController.CircleEx> movedNodes = new ArrayList<MapController.CircleEx>();

  protected void moveSelectedCirclesBy(
      ArrayList<MapController.CircleEx> circles, double deltaX, double deltaY) {
    for (MapController.CircleEx c : circles) {
      c.setCenterX(c.getCenterX() + deltaX);
      c.setCenterY(c.getCenterY() + deltaY);

      if (!movedNodes.contains(c)) {
        movedNodes.add(c);
      }

      c.updateAdjacentEdges();
    }
  }

  private double getOneTapMoveDist() {
    return minimumSelectionMove / mapInsertController.getAdornerPane().getScaleX();
  }
}
