package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import edu.wpi.cs3733.c21.teamY.SuperSecretSurprise.KnockKnockServer;
import edu.wpi.cs3733.c21.teamY.algorithms.*;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.JDBCUtils;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import edu.wpi.cs3733.c21.teamY.entity.Edge;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class AdminPageController extends SubPage {

  @FXML private SplitPane splitPane;
  @FXML private AnchorPane splitPaneTop;
  @FXML private AnchorPane splitPaneBottom;

  @FXML private MapController mapInsertController;
  @FXML private EditNodeTableController editNodeTableController;

  //  private Button toHomeBtn;
  @FXML private TextField newX;
  @FXML private TextField newY;

  //  private Button resetView;

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

  @FXML private Pane anchor;
  @FXML private HBox header;
  @FXML private VBox mapBox;
  @FXML private HBox hBox;

  @FXML private JFXButton addEdge;

  @FXML private Text nodeDisplay = new Text("Selected Node");
  @FXML private Text edgeDisplay = new Text("Selected Edge");

  @FXML private StackPane stackPane;

  @FXML private Button toolTip;
  @FXML private Button resetView;

  @FXML private MenuItem depthFirst;
  @FXML private MenuItem breadthFirst;
  @FXML private MenuItem aStar;
  // @FXML private MenuItem dijkstra;

  @FXML private StackPane popupStack;
  //  @FXML private MenuItem setFloorThreePage;
  //  @FXML private MenuItem setFloorFourPage;
  //  @FXML private MenuItem setFloorFivePage;

  @FXML private JFXComboBox selectNewAlgo;
  @FXML private JFXComboBox startLocationBox;
  @FXML private JFXComboBox endLocationBox;

  @FXML private Button secret;

  JFXDialog dialog = new JFXDialog();

  MapController.CircleEx rightClickedNode;
  MapController.LineEx rightClickedEdge;

  boolean creatingEdge = false;

  public AdminPageController() {}

  public void initialize() {

    secret.setVisible(false);
    secret.setOnAction(
        e -> {
          KnockKnockServer kn = new KnockKnockServer("ESP_Connection", nodes.get(0), nodes.get(3));
          kn.start();
        });

    Platform.runLater(
        () -> {
          addMapPage();
          loadNodesFromDB();
          resetComboBoxes();

          // Shift!!!!!!
          mapInsertController.getContainerStackPane().requestFocus();
          anchor
              .getScene()
              .setOnKeyPressed(
                  e -> {
                    mapInsertController.scrollOnPress(e);

                    // Should be improved
                    if (e.isShiftDown()) {
                      shiftPressed = true;
                      System.out.println("Shift pressed");
                    } else {
                      shiftPressed = false;
                    }
                  });

          anchor
              .getScene()
              .setOnKeyReleased(
                  e -> {
                    mapInsertController.scrollOnRelease(e);

                    if (e.getCode() == KeyCode.DELETE) {
                      removeSelected();
                    }
                    if (e.getCode() == KeyCode.ESCAPE) {
                      mapInsertController.clearSelection();
                    }
                    if (e.getCode() == KeyCode.W) {
                      moveSelectedCirclesBy(mapInsertController.getSelectedNodes(), 0.0, -5);
                    }
                    if (e.getCode() == KeyCode.S) {
                      moveSelectedCirclesBy(mapInsertController.getSelectedNodes(), 0.0, 5);
                    }
                    if (e.getCode() == KeyCode.A) {
                      moveSelectedCirclesBy(mapInsertController.getSelectedNodes(), -5, 0.0);
                    }
                    if (e.getCode() == KeyCode.D) {
                      moveSelectedCirclesBy(mapInsertController.getSelectedNodes(), 5, 0.0);
                    }

                    if (e.isShiftDown()) {
                      shiftPressed = true;
                    } else {
                      shiftPressed = false;
                    }
                  });

          // selectNewAlgo.setText("Select Algorithm");

          //          mapInsertController.getMapImageView().setScaleX(0.25);

          int i = -1;
          for (Node menuItem : mapInsertController.getFloorList().getChildren()) {
            if (i != -1) {
              int index = i;
              ((JFXButton) menuItem).setOnAction(e -> handleFloorChanged(e, index));
              i++;
            } else {
              i++;
            }
          }

          // mouse move

          mapInsertController
              .getAdornerPane()
              .setOnMouseMoved(
                  e -> {
                    if (creatingEdge) {
                      edgeCreationLine.setEndX(e.getX());
                      edgeCreationLine.setEndY(e.getY());
                      edgeCreationLine.setStrokeWidth(
                          mapInsertController.getScaledLineWidthSelected());
                    }
                  });

          //          Mouse Down Override

          mapInsertController
              .getAdornerPane()
              .setOnMousePressed(
                  e -> {
                    if (e.isSecondaryButtonDown()) {
                      System.out.println("Right button clicked");
                      rightClicked = true;
                    } else {
                      handleMouseDown(e);
                      resetContextMenus();
                    }
                  });

          // Mouse Dragged Override
          mapInsertController
              .getAdornerPane()
              .setOnMouseDragged(
                  e -> {
                    if (e.isSecondaryButtonDown() || rightClicked) {
                      System.out.println("Right button moved");
                      rightClicked = true;
                    } else {
                      handleMouseDragged(e);
                    }
                  });

          // Mouse Up Override
          mapInsertController
              .getAdornerPane()
              .setOnMouseReleased(
                  e -> {
                    if (rightClicked) {
                      System.out.println("Right button released");
                      handleRightClick(e);
                      rightClicked = false;
                    } else {
                      handleMouseReleased(e);
                    }
                  });

          selectNewAlgo.setOnAction(e -> selectAlgo(e));

          addEdge.setOnAction(
              event -> {
                createEdge(
                    (String) startLocationBox.getValue(), (String) endLocationBox.getValue());
              });
          // Resize the adorners so that they are easier to see
          // Only has to happen once on page load

          //                    mapInsertController.setBaseCircleRadius(6);
          //                    mapInsertController.setBaseLineWidth(5);
          //                    mapInsertController.setSelectedWidthRatio(5.0 / 3);

          assignContextMenuActions();
        });
  }

  private boolean rightClicked;

  private void handleMouseDown(MouseEvent e) {
    mapInsertController.defaultOnMousePressed(e);
    dragStartX = e.getX();
    dragStartY = e.getY();

    boolean clickedNode = e.getPickResult().getIntersectedNode() instanceof MapController.CircleEx;
    boolean clickedEdge = e.getPickResult().getIntersectedNode() instanceof Line;
    if ((clickedNode || clickedEdge)) {
      // Tentatively initiate them for drag
      boolean headHasFocus = false;
      if (clickedNode) {
        jfxNodeBeingDragged = (MapController.CircleEx) e.getPickResult().getIntersectedNode();
        headHasFocus = ((MapController.CircleEx) jfxNodeBeingDragged).hasFocus;
      } else if (clickedEdge) {
        jfxNodeBeingDragged =
            (MapController.LineEx) e.getPickResult().getIntersectedNode().getParent();
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
  }

  private void handleMouseDragged(MouseEvent e) {
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

      rectangleSelection.setStrokeWidth(1 / mapInsertController.getAdornerPane().getScaleX());

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
  }

  private void handleMouseReleased(MouseEvent e) {
    // If not letting go of a drag
    mapInsertController.defaultOnMouseReleased(e);
    if (!mapInsertController.wasLastClickDrag()
        && !isDraggingAdorner
        && rectangleSelection == null) {

      // Node
      if (e.getPickResult().getIntersectedNode() instanceof MapController.CircleEx) {
        if (creatingEdge) {
          System.out.println("got here");
          endEdgeCreation((MapController.CircleEx) e.getPickResult().getIntersectedNode());
          // end the thing
        }
        // else if (addEdgecb.isSelected() && mapInsertController.getSelectedNodes().size() == 1) {
        //  checkBoxCreateEdge((MapController.CircleEx) e.getPickResult().getIntersectedNode());
        // }
        else {
          handleClickOnNode((MapController.CircleEx) e.getPickResult().getIntersectedNode());
        }
      }
      // Edge
      else if (!creatingEdge
          && e.getPickResult().getIntersectedNode() instanceof Line
          && e.getPickResult().getIntersectedNode().getParent() instanceof MapController.LineEx) {
        handleClickOnEdge(
            (MapController.LineEx) e.getPickResult().getIntersectedNode().getParent());
      }
      // Blank Map
      else {
        // Clicked on blank map
        mapInsertController.defaultOnMouseReleased(e);

        if (!mapInsertController.wasLastClickDrag()) {
          //                     If wasnt a drag, but clicked on blank map
          // if (addNodecb.isSelected()) {
          //  mapInsertController.clearSelection();
          //  createNodecb(e);
          // }
          if (!shiftPressed
              && startEdgeFlag
              && !(e.getPickResult().getIntersectedNode() instanceof MapController.CircleEx
                  || e.getPickResult().getIntersectedNode() instanceof MapController.LineEx))
            mapInsertController.clearSelection();

          if (creatingEdge) {
            endEdgeCreation(null);
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
      ArrayList<MapController.LineEx> intersectedLines = new ArrayList<MapController.LineEx>();

      for (Node adorner : mapInsertController.getAdornerPane().getChildren()) {
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
  }

  private ContextMenu contextMenu;

  private double contextMenuX;
  private double contextMenuY;

  private void handleRightClick(MouseEvent e) {
    // Right Clicked Node
    resetContextMenus();

    Point2D point = mapInsertController.getAdornerPane().sceneToLocal(e.getSceneX(), e.getSceneY());
    contextMenuX = point.getX();
    contextMenuY = point.getY();

    System.out.println(" " + contextMenuX + " " + contextMenuY);

    if (!isDraggingAdorner && !creatingEdge) {

      ArrayList<MapController.CircleEx> selNodes = mapInsertController.getSelectedNodes();
      ArrayList<MapController.LineEx> selEdges = mapInsertController.getSelectedEdges();

      if (e.getPickResult().getIntersectedNode() instanceof MapController.CircleEx
          && (!((MapController.CircleEx) e.getPickResult().getIntersectedNode()).hasFocus
              || (selEdges.size() == 0
                  && ((selNodes.size() == 1
                      && selNodes.get(0) == e.getPickResult().getIntersectedNode()))))) {
        mapInsertController.clearSelection();
        rightClickedNode = (MapController.CircleEx) e.getPickResult().getIntersectedNode();
        rightClickedEdge = null;
        mapInsertController.selectCircle(rightClickedNode);

        // NODE MENU
        contextMenu.getItems().addAll(addEdgeMenuItem, deleteMenuItem);
        contextMenu.show(mapInsertController.getContainerStackPane(), e.getSceneX(), e.getSceneY());
      }
      // Right Clicked Edge
      else if (e.getPickResult().getIntersectedNode() instanceof Line
          && e.getPickResult().getIntersectedNode().getParent() instanceof MapController.LineEx
          && (!((MapController.LineEx) e.getPickResult().getIntersectedNode().getParent()).hasFocus
              || (selNodes.size() == 0
                  && (selEdges.size() == 0
                      || (selEdges.size() == 1
                          && selEdges.get(0)
                              == e.getPickResult().getIntersectedNode().getParent()))))) {
        mapInsertController.clearSelection();
        rightClickedNode = null;
        rightClickedEdge =
            (MapController.LineEx) e.getPickResult().getIntersectedNode().getParent();

        mapInsertController.selectLine(rightClickedEdge);
        // EDGE MENU
        contextMenu.getItems().addAll(makeNodeHorizontal, makeNodeVertical, deleteMenuItem);
        contextMenu.show(mapInsertController.getContainerStackPane(), e.getSceneX(), e.getSceneY());
      }
      // Right Clicked MultiSelect
      else if (e.getPickResult().getIntersectedNode() instanceof MapController.CircleEx
          || (e.getPickResult().getIntersectedNode() instanceof Line
              && e.getPickResult().getIntersectedNode().getParent()
                  instanceof MapController.LineEx)) {
        rightClickedNode = null; // just in case really
        rightClickedEdge = null;

        boolean manyNodes = mapInsertController.getSelectedNodes().size() < 2;
        // disables them if <2, enables otherwise.
        alignVertical.setDisable(manyNodes);
        alignHorizontal.setDisable(manyNodes);

        // MULTIPLE MENU
        contextMenu.getItems().addAll(alignHorizontal, alignVertical, deleteMenuItem);
        contextMenu.show(mapInsertController.getContainerStackPane(), e.getSceneX(), e.getSceneY());
      } else {
        mapInsertController.clearSelection();
        rightClickedNode = null; // just in case really
        rightClickedEdge = null;

        // MAP MENU
        contextMenu.getItems().addAll(addNodeMenuItem);
        contextMenu.show(mapInsertController.getContainerStackPane(), e.getSceneX(), e.getSceneY());
      }
    }
  }

  // These are referenced in handler
  MenuItem alignVertical = new MenuItem("Align Nodes Vertically");
  MenuItem alignHorizontal = new MenuItem("Align Nodes Horizontally");
  MenuItem addEdgeMenuItem = new MenuItem("Edge to...");
  MenuItem addNodeMenuItem = new MenuItem("Add Node");
  MenuItem makeNodeVertical = new MenuItem("Make Vertical");
  MenuItem makeNodeHorizontal = new MenuItem("Make Horizontal");
  MenuItem deleteMenuItem = new MenuItem("Delete");

  private void assignContextMenuActions() {

    contextMenu = new ContextMenu();

    addEdgeMenuItem.setOnAction(
        event -> {
          // Only happens when right clicking on unselected node or the only node selected.
          if (rightClickedNode != null && !creatingEdge) {
            mapInsertController.selectCircle(rightClickedNode);
            startEdgeCreation(rightClickedNode);
          }
        });

    /*hideNode.setOnAction(
    event -> {
      System.out.println("hide node");
    });*/

    deleteMenuItem.setOnAction(
        event -> {
          contextMenuActions_Delete();
        });

    addNodeMenuItem.setOnAction(
        event -> {
          createNodeAt(Math.floor(contextMenuX), Math.floor(contextMenuY));
        });

    alignHorizontal.setOnAction(
        event -> {
          contextMenuActions_Align(false);
        });

    alignVertical.setOnAction(
        event -> {
          contextMenuActions_Align(true);
        });

    makeNodeVertical.setOnAction(
        event -> {
          contextMenuActions_MakeEdgeVertical();
        });
    makeNodeHorizontal.setOnAction(
        event -> {
          contextMenuActions_MakeEdgeHorizontal();
        });
  }

  private void contextMenuActions_Delete() {

    if (rightClickedNode != null) {
      mapInsertController.selectCircle(rightClickedNode);
      rightClickedNode = null;
    } else if (rightClickedEdge != null) {
      mapInsertController.selectLine(rightClickedEdge);
      rightClickedEdge = null;
    }
    removeSelected();
  }

  // Should combine these
  private void contextMenuActions_Align(boolean vertical) {
    double centerX = 0;
    double centerY = 0;
    if (rightClickedNode != null) {
      centerX = rightClickedNode.getCenterX();
      centerY = rightClickedNode.getCenterY();
    } else if (rightClickedEdge != null) {
      centerX = contextMenuX;
      centerY = contextMenuX;
    } else {
      MapController.CircleEx centerNode =
          mapInsertController
              .getSelectedNodes()
              .get(mapInsertController.getSelectedNodes().size() - 1);
      centerX = centerNode.getCenterX();
      centerY = centerNode.getCenterY();
    }

    for (MapController.CircleEx c : mapInsertController.getSelectedNodes()) {
      if (vertical) {
        c.setCenterY(centerY);
      } else {
        c.setCenterX(centerX);
      }

      movedNodes = new ArrayList<MapController.CircleEx>();
      if (!movedNodes.contains(c)) {
        movedNodes.add(c);
      }

      c.updateAdjacentEdges();

      updateNodePositionsInDB();
      movedNodes = new ArrayList<MapController.CircleEx>();
    }
  }

  private void contextMenuActions_MakeEdgeVertical() {
    if (rightClickedEdge != null) {
      if (rightClickedEdge.startNode != null) {
        mapInsertController.selectCircle(rightClickedEdge.startNode);
      }
      if (rightClickedEdge.endNode != null) {
        mapInsertController.selectCircle(rightClickedEdge.endNode);
      }
      contextMenuActions_Align(false);
    }
  }

  private void contextMenuActions_MakeEdgeHorizontal() {
    if (rightClickedEdge != null) {
      if (rightClickedEdge.startNode != null) {
        mapInsertController.selectCircle(rightClickedEdge.startNode);
      }
      if (rightClickedEdge.endNode != null) {
        mapInsertController.selectCircle(rightClickedEdge.endNode);
      }
      contextMenuActions_Align(true);
    }
  }

  private void resetContextMenus() {
    if (contextMenu != null) {
      contextMenu.getItems().remove(0, contextMenu.getItems().size());
      contextMenu.hide();
    }
  }

  private void loadNodesFromDB() {
    mapInsertController.removeAllAdornerElements();

    try {
      nodes = DataOperations.getListOfNodes();
      edges = DataOperations.getListOfEdge();
      nodeIDCounter = nodes.size() + 1;
    } catch (Exception e) {
      e.printStackTrace();
    }
    mapInsertController.addAdornerElements(nodes, edges, mapInsertController.floorNumber);
  }

  private void addMapPage() {
    FXMLLoader fxmlLoader = new FXMLLoader();
    try {
      Node node = fxmlLoader.load(getClass().getResource("MapUserControl.fxml").openStream());
      mapInsertController = (MapController) fxmlLoader.getController();
      mapInsertController.setParent(parent);
      mapInsertController.setAdminPage(true);

      // call method before page load
      splitPaneTop.getChildren().add(node);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void handleFloorChanged(ActionEvent e, int menuItemIndex) {
    // This should be optimised to only switch if the floor actually changed, but its very fast, so
    // I cant be bothered
    mapInsertController.removeAllAdornerElements();
    mapInsertController.changeMapImage(mapInsertController.getMapOrder().get(menuItemIndex));
    mapInsertController.addAdornerElements(nodes, edges, mapInsertController.floorNumber);
    //    drawPath(pathNodes);
    // mapInsertController.updateMenuPreview(e, mapInsertController.getFloorMenu());
  }

  private void selectAlgo(Event e) {
    if (((ComboBox) e.getSource()).getValue().equals("Set DFS")) {
      Settings.getSettings().getAlgorithmSelection().setContext(new DFSI());
      algoPopUp(popupStack, "DFS");
    } else if (((ComboBox) e.getSource()).getValue().equals("Set BFS")) {
      Settings.getSettings().getAlgorithmSelection().setContext(new BFSI());
      algoPopUp(popupStack, "BFS");
    } else if (((ComboBox) e.getSource()).getValue().equals("Set A*")) {
      Settings.getSettings().getAlgorithmSelection().setContext(new AStarI());
      algoPopUp(popupStack, "A*");
    } else if (((ComboBox) e.getSource()).getValue().equals("Set Dijkstra")) {
      Settings.getSettings().getAlgorithmSelection().setContext(new DijkstraI());
      algoPopUp(popupStack, "Dijkstra");
    }
  }

  public void algoPopUp(StackPane _stackPane, String selectedAlgo) {
    createPopUp(_stackPane, "#5a5c94", "#ffffff", selectedAlgo + " was successfully selected.");
  }

  private void createPopUp(
      StackPane _stackPane, String backgroundColor, String textColor, String textContent) {
    JFXDialog submitted = new JFXDialog();

    Label message = new Label();
    message.setStyle(
        " -fx-background-color: "
            + backgroundColor
            + "; -fx-background-radius: 6; -fx-font-size: 25; -fx-text-fill: "
            + textColor);
    message.setText(textContent);
    message.maxHeight(70);
    message.maxWidth(300);
    message.prefHeight(70);
    message.prefWidth(250);
    Insets myInset = new Insets(10);
    message.setPadding(myInset);
    BorderStroke myStroke =
        new BorderStroke(
            Paint.valueOf(backgroundColor),
            new BorderStrokeStyle(null, null, null, 6, 1, null),
            new CornerRadii(6),
            new BorderWidths(3));
    Border myB = new Border(myStroke);
    message.setBorder(myB);

    submitted.setContent(message);
    submitted.show(_stackPane);
  }

  private void resetComboBoxes() {
    startLocationBox.getItems().remove(0, startLocationBox.getItems().size());
    endLocationBox.getItems().remove(0, endLocationBox.getItems().size());
    selectNewAlgo.getItems().remove(0, selectNewAlgo.getItems().size());

    for (edu.wpi.cs3733.c21.teamY.entity.Node node : nodes) {
      startLocationBox.getItems().add(node.nodeID);
    }

    for (edu.wpi.cs3733.c21.teamY.entity.Node node : nodes) {
      endLocationBox.getItems().add(node.nodeID);
    }

    selectNewAlgo.setPromptText("Select New Algorithm");
    selectNewAlgo.getItems().add("Set DFS");
    selectNewAlgo.getItems().add("Set BFS");
    selectNewAlgo.getItems().add("Set A*");
    selectNewAlgo.getItems().add("Set Dijkstra");
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
              Math.floor(mapInsertController.scaleUpXCoords(Double.parseDouble(newX.getText()))),
              Math.floor(mapInsertController.scaleUpYCoords(Double.parseDouble(newY.getText()))),
              mapInsertController.floorNumber,
              nodeID);

      // JDBCUtils.insert(10, n, "NODE");
      // JDBCUtils.insert(JDBCUtils.insertString(n));

      JDBCUtils.insert(9, n, "Node");

    } catch (Exception exception) {
      System.out.println("Can't create a node with text in the field input");
    }
  }

  /*private void createNodecb(MouseEvent e) {
    // when the add node checkbox is selected, the new nodes can be created
    // wherever the mouse clicks withing the scene
    if (addNodecb.isSelected()) {
      createNodeAt(
          Math.floor(mapInsertController.scaleUpXCoords(e.getX())),
          Math.floor(mapInsertController.scaleUpYCoords(e.getY())));
    }
  }*/

  private void createNodeAt(double x, double y) {
    String nodeID = String.valueOf(nodeIDCounter);
    nodeIDCounter++;

    edu.wpi.cs3733.c21.teamY.entity.Node n =
        new edu.wpi.cs3733.c21.teamY.entity.Node(x, y, mapInsertController.floorNumber, nodeID);
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

  private void createEdge(String startNodeString, String endNodeString) {

    if (startNodeString == null || endNodeString == null) {
      System.out.println("invalid entry");
    }

    MapController.CircleEx startNode =
        (MapController.CircleEx) mapInsertController.getAdornerPane().lookup("#" + startNodeString);
    MapController.CircleEx endNode =
        (MapController.CircleEx) mapInsertController.getAdornerPane().lookup("#" + endNodeString);

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

  private void checkBoxCreateEdge(MapController.CircleEx endNode) {
    // creates an edge between two selected points when the checkbox is selected
    ArrayList<MapController.CircleEx> selectedNodes = mapInsertController.getSelectedNodes();
    if (selectedNodes.size() == 1 && endNode != selectedNodes.get(0)) {

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

  Line edgeCreationLine = null;

  private void startEdgeCreation(MapController.CircleEx startNode) {
    creatingEdge = true;
    edgeCreationLine =
        new Line(
            startNode.getCenterX(),
            startNode.getCenterY(),
            startNode.getCenterX(),
            startNode.getCenterY());

    edgeCreationLine.setStroke(Paint.valueOf("BLUE"));
    edgeCreationLine.setStrokeWidth(mapInsertController.getScaledLineWidthSelected());
    mapInsertController.getAdornerPane().getChildren().add(edgeCreationLine);
    edgeCreationLine.toBack();
  }

  private void endEdgeCreation(MapController.CircleEx endNode) {
    if (endNode != null) {
      contextMenuCreateEdge(endNode);
    }
    mapInsertController.getAdornerPane().getChildren().remove(edgeCreationLine);
    edgeCreationLine = null;
    creatingEdge = false;
  }

  // ONLY TO BE USED BY CONTEXT MENU FOR NOW (FIRST NODE IS SELECTED, SECOND NODE IS CLICKED)
  private void contextMenuCreateEdge(MapController.CircleEx endNode) {
    MapController.CircleEx lastSelectedNode = mapInsertController.getSelectedNodes().get(0);

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

  //        @FXML
  //        private void buttonClicked(ActionEvent e) {
  //          // error handling for FXMLLoader.load
  //          try {
  //            // initializing stage
  //            Stage stage = null;
  //
  //            if (e.getSource() == toHomeBtn) {
  //              // gets the current stage
  //              stage = (Stage) toHomeBtn.getScene().getWindow();
  //              // sets the new scene to the alex page
  //              DataOperations.DBtoCSV("NODE");
  //              DataOperations.DBtoCSV("EDGE");
  //              ActiveGraph.initialize();
  //              stage.setScene(new
  //                      Scene(FXMLLoader.load(getClass().getResource("HomePage.fxml"))));
  //
  //            } else {
  //
  //            }
  //
  //            // display new stage
  //            stage.show();
  //          } catch (Exception exp) {
  //          }
  //        }

  private void removeSelected() {
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
