package edu.wpi.cs3733.c21.teamY;

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

public class nodeEdgeDispController {

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

  @FXML private MapController mapInsertController;

  public nodeEdgeDispController() {}

  @FXML
  private void initialize() {

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

    selectNewMapImage.setText("Select New Map");
    setParkingPage.setOnAction(
        e -> {
          mapInsertController.setImage(
              mapInsertController.chooseImage((Stage) selectNewMapImage.getScene().getWindow()),
              MapController.MAP_PAGE.PARKING);
          updateMenuPreview(e, selectNewMapImage);
        });
    setFloorOnePage.setOnAction(
        e -> {
          mapInsertController.setImage(
              mapInsertController.chooseImage((Stage) selectNewMapImage.getScene().getWindow()),
              MapController.MAP_PAGE.FLOOR1);
          updateMenuPreview(e, selectNewMapImage);
        });
    setFloorTwoPage.setOnAction(
        e -> {
          mapInsertController.setImage(
              mapInsertController.chooseImage((Stage) selectNewMapImage.getScene().getWindow()),
              MapController.MAP_PAGE.FLOOR2);
          updateMenuPreview(e, selectNewMapImage);
        });
    setFloorThreePage.setOnAction(
        e -> {
          mapInsertController.setImage(
              mapInsertController.chooseImage((Stage) selectNewMapImage.getScene().getWindow()),
              MapController.MAP_PAGE.FLOOR3);
          updateMenuPreview(e, selectNewMapImage);
        });
    setFloorFourPage.setOnAction(
        e -> {
          mapInsertController.setImage(
              mapInsertController.chooseImage((Stage) selectNewMapImage.getScene().getWindow()),
              MapController.MAP_PAGE.FLOOR4);
          updateMenuPreview(e, selectNewMapImage);
        });
    setFloorFivePage.setOnAction(
        e -> {
          mapInsertController.setImage(
              mapInsertController.chooseImage((Stage) selectNewMapImage.getScene().getWindow()),
              MapController.MAP_PAGE.FLOOR5);
          updateMenuPreview(e, selectNewMapImage);
        });

    JFXDialog dialog = new JFXDialog();
    dialog.setContent(
        new Label(
            " Scroll to Zoom"
                + "\n Hold CTRL + Scroll to Pan Up and down"
                + "\n Hold SHIFT + Scroll to Pan left and right"
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
            controlImageShown(e, mapInsertController.getMapOrder().get(index));
            updateMenuPreview(e, mapInsertController.getFloorMenu());
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

    // SHOULD BE IMPROVED
    anchor.setOnKeyPressed(
        k -> {
          if (k.isShiftDown()) {
            shiftPressed = true;
          } else {
            shiftPressed = false;
          }
        });

    // Shift for multiple nodes.
    anchor.setOnKeyReleased(
        k -> {
          if (k.isShiftDown()) {
            shiftPressed = true;
          } else {
            shiftPressed = false;
          }
        });
    // Deselect if not shifting and clicked on not an Adorner
    mapInsertController
        .getAdornerPane()
        .setOnMouseClicked(
            e -> {
              if (!shiftPressed
                  && !(e.getPickResult().getIntersectedNode() instanceof MapController.CircleEx
                      || e.getPickResult().getIntersectedNode() instanceof MapController.LineEx))
                mapInsertController.clearSelection();
            });
  }

  protected void resetMouseHandlingForAdorners() {
    for (javafx.scene.Node p : mapInsertController.getAdornerPane().getChildren()) {
      try {

        if (p instanceof MapController.CircleEx) {
          p.setOnMouseClicked(
              w -> {
                if (!shiftPressed) {
                  mapInsertController.clearSelection();
                }
                mapInsertController.selectCircle((MapController.CircleEx) p);
              });
        } else if (p instanceof MapController.LineEx) {
          p.setOnMouseClicked(
              w -> {
                if (!shiftPressed) {
                  mapInsertController.clearSelection();
                }
                mapInsertController.selectLine((MapController.LineEx) p);
              });
        } else {
          System.out.println("Invalid Type Found: " + p.getTypeSelector());
        }

      } catch (Exception exp) {
        System.out.println("no point selected");
      }
    }
  }

  private void updateMenuPreview(ActionEvent e, SplitMenuButton s) {
    s.setText(((MenuItem) e.getSource()).getText());
  }

  // this sucks
  private void initiateDrawing() {
    removeAllAdornerElements();
    mapInsertController.clearSelection();

    nodeIDCounter = nodes.size() + 1;
    mapInsertController.drawFromCSV(nodes, edges);
    resetMouseHandlingForAdorners();
  }

  private void initImage() {
    mapInsertController.changeImage(MapController.MAP_PAGE.PARKING);
    //    map.setFitHeight(500);
    //    map.fitHeightProperty().bind(anchor.heightProperty());
    //    map.fitWidthProperty().bind(anchor.widthProperty());
  }

  private void controlImageShown(ActionEvent e, MapController.MAP_PAGE mp) {
    removeAllAdornerElements();
    mapInsertController.changeImage(mp);
  }

  // --delete functions only work taking off screen not deleting from DB - oops
  private void removeAllAdornerElements() {
    mapInsertController
        .getAdornerPane()
        .getChildren()
        .remove(0, mapInsertController.getAdornerPane().getChildren().size());
    mapInsertController.updateMapScreen();
  }

  //  private void removeEdge(ActionEvent e) throws SQLException {
  //    String edgeID = currentSelectedLine.getId();
  //    pane.getChildren().remove(currentSelectedLine);
  //    JDBCUtils.deleteEdge(edgeID);
  //  }

  //  private void removeNode(ActionEvent e) throws SQLException {
  //    String nodeID = currentSelectedCircle.getId();
  //    pane.getChildren().remove(currentSelectedCircle);
  //    JDBCUtils.deleteNode(nodeID);
  //
  //    // adding the node and refreshing the scene
  //    Stage stage = (Stage) deleteNode.getScene().getWindow();
  //    stage.setScene(deleteNode.getScene());
  //    stage.show();
  //  }

  /*
  private void removeEdge(ActionEvent e) {
    String edgeID = mapInsertController.currentSelectedLine.getId();
    mapInsertController
        .getAdornerPane()
        .getChildren()
        .remove(mapInsertController.currentSelectedLine);
    mapInsertController.updateMapScreen();
    try {
      JDBCUtils.deleteEdge(edgeID);
    } catch (Exception throwables) {
      throwables.printStackTrace();
    }
  }

  private void removeNode(ActionEvent e) {
    String nodeID = mapInsertController.currentSelectedCircle.getId();
    mapInsertController
        .getAdornerPane()
        .getChildren()
        .remove(mapInsertController.currentSelectedCircle);
    mapInsertController.updateMapScreen();
    try {
      JDBCUtils.deleteNode(nodeID);
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }*/

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
  /*
    // --create node and edges
    private void createEdgecb(MouseEvent e) {
      // creates an edge between two selected points when the checkbox is selected
      if (addEdgecb.isSelected()) {
        if (startEdgeFlag) { // decides if its te starting or ending point being selected
          System.out.println(startEdgeFlag);
          mapInsertController.highlightCircle();
          startEdgeFlag = !startEdgeFlag;
          try {
            startNodeID = mapInsertController.currentSelectedCircle.getId();
            startx = mapInsertController.currentSelectedCircle.getCenterX();
            starty = mapInsertController.currentSelectedCircle.getCenterY();
          } catch (Exception exception) {
            System.out.println("no start point");
          }
        } else {
          System.out.println(startEdgeFlag);
          startEdgeFlag = !startEdgeFlag;
          try {
            endNodeID = mapInsertController.currentSelectedCircle.getId();
            endx = mapInsertController.currentSelectedCircle.getCenterX();
            endy = mapInsertController.currentSelectedCircle.getCenterY();
          } catch (Exception exception) {
            System.out.println("no end point");
          }

          // creating the line and adding as a child to the pane
          String edgeID = startNodeID + "_" + endNodeID;
          Edge ed = new Edge(edgeID, startNodeID, endNodeID);
          // JDBCUtils.insert(3, ed, "EDGE");
          // JDBCUtils.insert(JDBCUtils.insertString(ed));

          //        JDBCUtils.insert(3, ed, "Edge");
          //        Line line = new Line(startx, starty, endx, endy);
          //        line.setId(ed.getEdgeID());

          //        line.setStrokeWidth(3);
          //        pane.getChildren().add(line);
          //        line.toBack();
          //        // refreshing and adding to the scene
          //        Stage stage = (Stage) addEdge.getScene().getWindow();
          //        stage.setScene(addEdge.getScene());
          //        stage.show();

          try {
            //          DatabaseQueryAdministrator.insertEdge(ed);
            JDBCUtils.insert(3, ed, "Edge");
          } catch (Exception exception) {
            System.out.println("nodeEdgeDispController.createEdgecb");
          }
          //        CSV.saveEdge(ed);
          mapInsertController.addEdgeLine(ed);
        }
      }
    }
  */
  private void createNodecb(MouseEvent e) {
    // when the add node checkbox is selected, the new nodes can be created
    // wherever the mouse clicks withing the scene
    String nodeID = String.valueOf(nodeIDCounter);
    nodeIDCounter++;
    if (addNodecb.isSelected()) {
      edu.wpi.cs3733.c21.teamY.Node n =
          new edu.wpi.cs3733.c21.teamY.Node(
              // <<<<<<< HEAD
              //              Math.floor(mapInsertController.scaleUpXCoords(e.getX())),
              //              Math.floor(mapInsertController.scaleUpYCoords(e.getY())),
              //              mapInsertController.floorNumber,
              //              nodeID);
              //      // JDBCUtils.insert(10, n, "NODE");
              //      // JDBCUtils.insert(JDBCUtils.insertString(n));
              //      JDBCUtils.insert(9, n, "Node");
              //
              //    }
              //  }

              //  private void drawFromCSV()
              //      throws IllegalAccessException, IOException, NoSuchFieldException,
              // SQLException,
              //          InstantiationException, ClassNotFoundException {
              //    nodes = CSV.getListOfNodes();
              //    edges = CSV.getListOfEdge();
              // ==========================================================================================
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

      mapInsertController.addNodeCircle(n);
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

      mapInsertController.addNodeCircle(n);

    } catch (Exception exception) {
      System.out.println("Can't create a node with text in the field input");
    }
  }

  /*
  // --display info on nodes/edges
  private void setCurrentDisplay() {
    nodeDisplay.setText(
        String.format(
            "x: %.2f, y: %.2f",
            mapInsertController.currentSelectedCircle.getCenterX(),
            mapInsertController.currentSelectedCircle.getCenterY()));
    edgeDisplay.setText(
        String.format(
            "start x: %.2f, y: %.2f \nend x: %.2f, %.2f",
            mapInsertController.currentSelectedLine.getStartX(),
            mapInsertController.currentSelectedLine.getStartY(),
            mapInsertController.currentSelectedLine.getEndX(),
            mapInsertController.currentSelectedLine.getEndY()));
  }*/
}
