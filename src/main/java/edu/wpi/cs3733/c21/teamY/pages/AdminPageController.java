package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.c21.teamY.dataops.JDBCUtils;
import edu.wpi.cs3733.c21.teamY.entity.Edge;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class AdminPageController extends RightPage {

  @FXML private SplitPane splitPane;
  @FXML private GridPane grid;
  @FXML private VBox vBox;
  @FXML private AnchorPane splitPaneTop;
  @FXML private AnchorPane splitPaneBottom;
  @FXML private AnchorPane baseAnchorPane;

  @FXML private MapController mapInsertController;
  @FXML private EditNodeTableController editNodeTableController;

  @FXML private CheckBox addNodecb;
  @FXML private CheckBox addEdgecb;
  @FXML private JFXButton delete;
  //  private Button toHomeBtn;
  @FXML private TextField newX;
  @FXML private TextField newY;
  @FXML private JFXButton resetView;

  private boolean startEdgeFlag = true;
  private double startx, starty, endx, endy;
  private String startNodeID, endNodeID;

  private ArrayList<Edge> edges = new ArrayList<Edge>();
  private ArrayList<edu.wpi.cs3733.c21.teamY.entity.Node> nodes =
      new ArrayList<edu.wpi.cs3733.c21.teamY.entity.Node>();

  private ArrayList<MapController.CircleEx> movedNodes = new ArrayList<MapController.CircleEx>();

  private boolean shiftPressed = false;

  private double minimumSelectionMove = 10;

  private int nodeIDCounter;

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

  public AdminPageController() {}

  public void initialize() {

    delete.setOnAction(e -> removeSelected(e));

    //    addEdgecb.setVisible(false);
    //    addNodecb.setVisible(false);
    newX.setVisible(false);
    newY.setVisible(false);

    addEdgecb.setOnAction(
        e -> {
          startEdgeFlag = true;
          addNodecb.setSelected(false);
        });
    addNodecb.setOnAction(
        e -> {
          addEdgecb.setSelected(false);
        });

    Platform.runLater(
        () -> {
          addMapPage();
          addTablePage();

          System.out.println("2");
          mapInsertController.removeAllAdornerElements();
          nodes = mapInsertController.loadNodesFromDB();
          edges = mapInsertController.loadEdgesFromDB();
          mapInsertController.addAdornerElements(nodes, edges, mapInsertController.floorNumber);

          mapInsertController
              .getFloorMenu()
              .setOnAction(
                  e -> {
                    mapInsertController.changeMapImage(
                        mapInsertController.determineNewMap(
                            (String) mapInsertController.getFloorMenu().getValue()));
                    mapInsertController.removeAllAdornerElements();
                    nodes = mapInsertController.loadNodesFromDB();
                    edges = mapInsertController.loadEdgesFromDB();
                    mapInsertController.addAdornerElements(
                        nodes, edges, mapInsertController.floorNumber);
                  });

          baseAnchorPane
              .getScene()
              .setOnKeyPressed(
                  e -> {
                    System.out.println("SHIFT");
                    mapInsertController.scrollOnPress(e);

                    // Should be improved
                    if (e.isShiftDown()) {
                      shiftPressed = true;
                    } else {
                      shiftPressed = false;
                    }
                  });

          baseAnchorPane
              .getScene()
              .setOnKeyReleased(
                  e -> {
                    mapInsertController.scrollOnRelease(e);

                    if (e.isShiftDown()) {
                      shiftPressed = true;
                    } else {
                      shiftPressed = false;
                    }
                  });

          //          mapInsertController.removeAllAdornerElements();
          //          nodes = mapInsertController.loadNodesFromDB();
          //          edges = mapInsertController.loadEdgesFromDB();
          //          mapInsertController.addAdornerElements(nodes, edges,
          // mapInsertController.floorNumber);

          //          Mouse Down Override
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

                        if (edge.startNode != null
                            && !nodesAffectedByDrag.contains(edge.startNode)) {
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
                        MapController.CircleEx handle =
                            (MapController.CircleEx) jfxNodeBeingDragged;
                        double deltaX = e.getX() - handle.getCenterX();
                        double deltaY = e.getY() - handle.getCenterY();
                        moveSelectedCirclesBy(
                            mapInsertController.getSelectedNodes(), deltaX, deltaY);
                      }
                      // Line
                      else if (jfxNodeBeingDragged instanceof MapController.LineEx) {
                        MapController.LineEx handle = (MapController.LineEx) jfxNodeBeingDragged;
                        double deltaX = e.getX() - handle.getStartX() - dragStartXRelativeEdge;
                        double deltaY = e.getY() - handle.getStartY() - dragStartYRelativeEdge;
                        moveSelectedCirclesBy(
                            mapInsertController.getSelectedNodes(), deltaX, deltaY);
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
                      if (e.getPickResult().getIntersectedNode()
                          instanceof MapController.CircleEx) {
                        if (addEdgecb.isSelected()
                            && mapInsertController.getSelectedNodes().size() == 1) {
                          createEdge(
                              (MapController.CircleEx) e.getPickResult().getIntersectedNode());
                        } else {
                          handleClickOnNode(
                              (MapController.CircleEx) e.getPickResult().getIntersectedNode());
                        }
                      }
                      // Edge
                      else if (e.getPickResult().getIntersectedNode()
                          instanceof MapController.LineEx) {
                        handleClickOnEdge(
                            (MapController.LineEx) e.getPickResult().getIntersectedNode());
                      }
                      // Blank Map
                      else {
                        // Clicked on blank map
                        mapInsertController.defaultOnMouseReleased(e);

                        if (!mapInsertController.wasLastClickDrag()) {
                          //                     If wasnt a drag, but clicked on blank map
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

          // Resize the adorners so that they are easier to see
          // Only has to happen once on page load
          mapInsertController.setBaseCircleRadius(6);
          mapInsertController.setBaseLineWidth(5);
          mapInsertController.setSelectedWidthRatio(5.0 / 3);

          System.out.println("1");
        });

    resetView.setOnAction(e -> mapInsertController.resetMapView());
    resetView.toFront();
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

  private void addTablePage() {
    FXMLLoader fxmlLoader = new FXMLLoader();
    try {
      Node node = fxmlLoader.load(getClass().getResource("EditNodeTable.fxml").openStream());
      editNodeTableController = (EditNodeTableController) fxmlLoader.getController();
      editNodeTableController.setParent(parent);
      // call method before page load
      vBox.getChildren().add(node);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // stuff from other
  private double getOneTapMoveDist() {
    return minimumSelectionMove / mapInsertController.getAdornerPane().getScaleX();
  }

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

  private void updateNodePositionsInDB() {
    for (MapController.CircleEx c : movedNodes) {
      // System.out.println("updating DB with moved nodes");
      JDBCUtils.updateNodeCoordsOnly(
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

  private void createNode(ActionEvent e) {
    // creates a new instance of the local node class and creates a red circle
    // to add as a child of the pane in the scene
    String nodeID = String.valueOf(nodeIDCounter);
    nodeIDCounter++;
    try {
      edu.wpi.cs3733.c21.teamY.entity.Node n =
          new edu.wpi.cs3733.c21.teamY.entity.Node(
              mapInsertController.scaleUpXCoords(Double.parseDouble(newX.getText())),
              mapInsertController.scaleUpYCoords(Double.parseDouble(newY.getText())),
              mapInsertController.floorNumber,
              nodeID);

      // JDBCUtils.insert(10, n, "NODE");
      // JDBCUtils.insert(JDBCUtils.insertString(n));

      JDBCUtils.insert(9, n, "Node");

    } catch (Exception exception) {
      System.out.println("Can't create a node with text in the field input");
    }
  }

  private void createNodecb(MouseEvent e) {
    // when the add node checkbox is selected, the new nodes can be created
    // wherever the mouse clicks withing the scene
    String nodeID = String.valueOf(nodeIDCounter);
    nodeIDCounter++;
    if (addNodecb.isSelected()) {
      edu.wpi.cs3733.c21.teamY.entity.Node n =
          new edu.wpi.cs3733.c21.teamY.entity.Node(
              Math.floor(mapInsertController.scaleUpXCoords(e.getX())),
              Math.floor(mapInsertController.scaleUpYCoords(e.getY())),
              mapInsertController.floorNumber,
              nodeID);
      // JDBCUtils.insert(10, n, "NODE");
      // JDBCUtils.insert(JDBCUtils.insertString(n));
      //    JDBCUtils.insert(9, n, "Node");

      try {
        JDBCUtils.insert(9, n, "Node");
      } catch (Exception exception) {
        System.out.println("nodeEdgeDispController.createNodecb");
        return;
      }
      mapInsertController.clearSelection();
      MapController.CircleEx c = mapInsertController.addNodeCircle(n);
      mapInsertController.selectCircle(c);
    }
  }

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

  //      @FXML
  //      private void buttonClicked(ActionEvent e) {
  //        // error handling for FXMLLoader.load
  //        try {
  //          // initializing stage
  //          Stage stage = null;
  //
  //          if (e.getSource() == toHomeBtn) {
  //            // gets the current stage
  //            stage = (Stage) toHomeBtn.getScene().getWindow();
  //            // sets the new scene to the alex page
  //            CSV.DBtoCSV("NODE");
  //            CSV.DBtoCSV("EDGE");
  //            ActiveGraph.initialize();
  //            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("HomePage.fxml"))));
  //
  //          } else {
  //
  //          }
  //
  //          // display new stage
  //          stage.show();
  //        } catch (Exception exp) {
  //        }
  //      }

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
      try {
        JDBCUtils.deleteNode(nodeId);
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }
    }

    for (String edgeId : edgeIDs) {
      try {
        JDBCUtils.deleteNode(edgeId);
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }
    }

    mapInsertController.removeSelected();
  }

  private void loadMapFromCSV() {
    mapInsertController.removeAllAdornerElements();

    nodeIDCounter = nodes.size() + 1;

    nodes = mapInsertController.loadNodesFromCSV();
    edges = mapInsertController.loadEdgesFromCSV();
    mapInsertController.addAdornerElements(nodes, edges, mapInsertController.floorNumber);
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
}
