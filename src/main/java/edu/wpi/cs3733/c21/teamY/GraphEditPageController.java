package edu.wpi.cs3733.c21.teamY;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GraphEditPageController {

  @FXML private Pane anchor;
  @FXML private HBox header;

  @FXML private Button toHomeBtn;
  @FXML private Button addNode;
  @FXML private Button loadNodesButton;

  @FXML private CheckBox addNodecb;
  @FXML private CheckBox addEdgecb;

  @FXML private Button deleteNode;

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

  @FXML private SplitMenuButton selectNewMapImage;
  @FXML private MenuItem setParkingPage;
  @FXML private MenuItem setFloorOnePage;
  @FXML private MenuItem setFloorTwoPage;
  @FXML private MenuItem setFloorThreePage;
  @FXML private MenuItem setFloorFourPage;
  @FXML private MenuItem setFloorFivePage;

  private ArrayList<Edge> edges = new ArrayList<Edge>();
  private ArrayList<edu.wpi.cs3733.c21.teamY.Node> nodes = new ArrayList<Node>();

  private int nodeIDCounter;
  private boolean shiftPressed = false;

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

  @FXML private MapController mapInsertController;

  public GraphEditPageController() {}

  @FXML
  private void initialize() {

    anchor.setOnKeyPressed(
        e -> {
          mapInsertController.scrollOnPress(e);
          Rectangle viewWindow =
              new Rectangle(
                  0, 0, stackPane.getWidth(), mapInsertController.containerStackPane.getHeight());
          mapInsertController.containerStackPane.setClip(viewWindow);

          // SHOULD BE IMPROVED

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
    zoomInButton.setOnAction(e -> mapInsertController.zoomOnButtons("in"));
    zoomOutButton.setOnAction(e -> mapInsertController.zoomOnButtons("out"));

    moveNodeUpButton.setOnAction(
        e -> {
          mapInsertController.moveSelected(
              mapInsertController.getSelectedNodes(), mapInsertController.getSelectedEdges(), "up");
          updateNodes();
        });
    moveNodeDownButton.setOnAction(
        e -> {
          mapInsertController.moveSelected(
              mapInsertController.getSelectedNodes(),
              mapInsertController.getSelectedEdges(),
              "down");
          updateNodes();
        });
    moveNodeLeftButton.setOnAction(
        e -> {
          mapInsertController.moveSelected(
              mapInsertController.getSelectedNodes(),
              mapInsertController.getSelectedEdges(),
              "left");
          updateNodes();
        });
    moveNodeRightButton.setOnAction(
        e -> {
          mapInsertController.moveSelected(
              mapInsertController.getSelectedNodes(),
              mapInsertController.getSelectedEdges(),
              "right");
          updateNodes();
        });

    selectNewMapImage.setText("Select New Map");
    setParkingPage.setOnAction(
        e -> {
          mapInsertController.setImage(
              mapInsertController.chooseImage((Stage) selectNewMapImage.getScene().getWindow()),
              MapController.MAP_PAGE.PARKING);
          mapInsertController.updateMenuPreview(e, selectNewMapImage);
        });
    setFloorOnePage.setOnAction(
        e -> {
          mapInsertController.setImage(
              mapInsertController.chooseImage((Stage) selectNewMapImage.getScene().getWindow()),
              MapController.MAP_PAGE.FLOOR1);
          mapInsertController.updateMenuPreview(e, selectNewMapImage);
        });
    setFloorTwoPage.setOnAction(
        e -> {
          mapInsertController.setImage(
              mapInsertController.chooseImage((Stage) selectNewMapImage.getScene().getWindow()),
              MapController.MAP_PAGE.FLOOR2);
          mapInsertController.updateMenuPreview(e, selectNewMapImage);
        });
    setFloorThreePage.setOnAction(
        e -> {
          mapInsertController.setImage(
              mapInsertController.chooseImage((Stage) selectNewMapImage.getScene().getWindow()),
              MapController.MAP_PAGE.FLOOR3);
          mapInsertController.updateMenuPreview(e, selectNewMapImage);
        });
    setFloorFourPage.setOnAction(
        e -> {
          mapInsertController.setImage(
              mapInsertController.chooseImage((Stage) selectNewMapImage.getScene().getWindow()),
              MapController.MAP_PAGE.FLOOR4);
          mapInsertController.updateMenuPreview(e, selectNewMapImage);
        });
    setFloorFivePage.setOnAction(
        e -> {
          mapInsertController.setImage(
              mapInsertController.chooseImage((Stage) selectNewMapImage.getScene().getWindow()),
              MapController.MAP_PAGE.FLOOR5);
          mapInsertController.updateMenuPreview(e, selectNewMapImage);
        });

    JFXDialog dialog = new JFXDialog();
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
          initiateDrawing();
        });

    int i = 0;
    for (MenuItem menuItem : mapInsertController.getFloorMenu().getItems()) {
      int index = i;
      menuItem.setOnAction(
          e -> {
            mapInsertController.switchImage(e, mapInsertController.getMapOrder().get(index));
            mapInsertController.updateMenuPreview(e, mapInsertController.getFloorMenu());
          });
      i++;
    }

    // attaches a handler to the button with a lambda expression
    toHomeBtn.setOnAction(e -> buttonClicked(e));

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

    resetMouseHandlingForAdorners();

    // Deselect if not shifting and clicked on not an Adorner
    mapInsertController
        .getAdornerPane()
        .setOnMouseClicked(
            e -> {
              if (addEdgecb.isSelected()) {
                createEdgecb(e);
              } else if (addNodecb.isSelected()) {
                mapInsertController.clearSelection();
                createNodecb(e);
              } else {
                if (!shiftPressed
                    && startEdgeFlag
                    && !(e.getPickResult().getIntersectedNode() instanceof MapController.CircleEx
                        || e.getPickResult().getIntersectedNode() instanceof MapController.LineEx))
                  mapInsertController.clearSelection();
              }
            });
  }

  // Set selection click handlers
  protected void resetMouseHandlingForAdorners() {
    for (javafx.scene.Node p : mapInsertController.getAdornerPane().getChildren()) {
      try {

        if (p instanceof MapController.CircleEx) {
          setNodeOnClick((MapController.CircleEx) p);
        } else if (p instanceof MapController.LineEx) {
          setEdgeOnClick((MapController.LineEx) p);
        } else {
          System.out.println("Invalid Type Found: " + p.getTypeSelector());
        }

      } catch (Exception exp) {
        System.out.println("no point selected");
      }
    }
  }

  private void setNodeOnClick(MapController.CircleEx node) {
    node.setOnMouseClicked(
        w -> {
          if (!shiftPressed) {
            mapInsertController.clearSelection();
          }
          mapInsertController.selectCircle((MapController.CircleEx) node);
          lastSelectedNode = (MapController.CircleEx) node;
        });
  }

  private void setEdgeOnClick(MapController.LineEx edge) {
    edge.setOnMouseClicked(
        w -> {
          if (!shiftPressed) {
            mapInsertController.clearSelection();
          }
          mapInsertController.selectLine((MapController.LineEx) edge);
        });
  }

  // this sucks
  private void initiateDrawing() {
    mapInsertController.removeAllAdornerElements();

    nodeIDCounter = nodes.size() + 1;

    nodes = mapInsertController.loadNodesFromCSV();
    edges = mapInsertController.loadEdgesFromCSV();
    mapInsertController.drawFromCSV(nodes, edges, mapInsertController.floorNumber);
    resetMouseHandlingForAdorners();
  }

  private void initImage() {
    mapInsertController.changeImage(MapController.MAP_PAGE.PARKING);
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
        CSV.DBtoCSV("NODE");
        CSV.DBtoCSV("EDGE");
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("HomePage.fxml"))));

      } else {

      }

      // display new stage
      stage.show();
    } catch (Exception exp) {
    }
  }

  private MapController.CircleEx lastSelectedNode;

  // --create node and edges
  private void createEdgecb(MouseEvent e) {
    // creates an edge between two selected points when the checkbox is selected
    ArrayList<MapController.CircleEx> selectedNodes = mapInsertController.getSelectedNodes();
    if (addEdgecb.isSelected() && lastSelectedNode != null) {
      if (startEdgeFlag) { // decides if its te starting or ending point being selected
        System.out.println(startEdgeFlag);

        startEdgeFlag = !startEdgeFlag;
        try {
          startNodeID = lastSelectedNode.getId();
          startx = lastSelectedNode.getCenterX();
          starty = lastSelectedNode.getCenterY();
        } catch (Exception exception) {
          System.out.println("no start point");
        }
      } else {
        System.out.println(startEdgeFlag);
        startEdgeFlag = !startEdgeFlag;
        try {
          endNodeID = lastSelectedNode.getId();
          endx = lastSelectedNode.getCenterX();
          endy = lastSelectedNode.getCenterY();
        } catch (Exception exception) {
          System.out.println("no end point");
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
        }
        //        CSV.saveEdge(ed);
        setEdgeOnClick(mapInsertController.addEdgeLine(ed));
        mapInsertController.clearSelection();
      }
    }
  }

  private void createNodecb(MouseEvent e) {
    // when the add node checkbox is selected, the new nodes can be created
    // wherever the mouse clicks withing the scene
    String nodeID = String.valueOf(nodeIDCounter);
    nodeIDCounter++;
    if (addNodecb.isSelected()) {
      edu.wpi.cs3733.c21.teamY.Node n =
          new edu.wpi.cs3733.c21.teamY.Node(
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
      }

      setNodeOnClick(mapInsertController.addNodeCircle(n));
    }
  }

  private void createNode(ActionEvent e) {
    // creates a new instance of the local node class and creates a red circle
    // to add as a child of the pane in the scene
    String nodeID = String.valueOf(nodeIDCounter);
    nodeIDCounter++;
    try {
      edu.wpi.cs3733.c21.teamY.Node n =
          new edu.wpi.cs3733.c21.teamY.Node(
              mapInsertController.scaleUpXCoords(Double.parseDouble(newX.getText())),
              mapInsertController.scaleUpYCoords(Double.parseDouble(newY.getText())),
              mapInsertController.floorNumber,
              nodeID);

      // JDBCUtils.insert(10, n, "NODE");
      // JDBCUtils.insert(JDBCUtils.insertString(n));

      JDBCUtils.insert(9, n, "Node");

      setNodeOnClick(mapInsertController.addNodeCircle(n));

    } catch (Exception exception) {
      System.out.println("Can't create a node with text in the field input");
    }
  }

  private void updateNodes() {
    for (MapController.CircleEx c : mapInsertController.movedNodes) {
      JDBCUtils.updateNodeCoordsOnly(
          c.getId(), Math.floor(c.getCenterX()), Math.floor(c.getCenterY()));
    }
    try {
      ActiveGraph.initialize();
    } catch (Exception exception) {
      System.out.println("GraphEditPageController.updateNodes");
    }
    mapInsertController.updateMapScreen();
  }
}
