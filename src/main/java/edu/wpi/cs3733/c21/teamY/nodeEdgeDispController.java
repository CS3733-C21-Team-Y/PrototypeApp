package edu.wpi.cs3733.c21.teamY;

import com.jfoenix.controls.JFXDialog;
import java.awt.*;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class nodeEdgeDispController {

  @FXML private Pane anchor;
  @FXML private HBox header;

  @FXML private Button toHomeBtn;
  @FXML private Button addNode;
  @FXML private Button testButton;

  @FXML private CheckBox addNodecb;
  @FXML private CheckBox addEdgecb;

  @FXML private Button deleteNode;
  @FXML private Button deleteEdge;

  @FXML private Text nodeDisplay = new Text("Selected Node");
  @FXML private Text edgeDisplay = new Text("Selected Edge");

  // variables for selecting points and locating edges
  private boolean startEdgeFlag = true;
  private double startx, starty, endx, endy;
  private String startNodeID, endNodeID;
  private Circle currentSelectedCircle = new Circle(0, 0, 0);
  private Line currentSelectedLine = new Line(0, 0, 0, 0);

  @FXML private TextField newX;
  @FXML private TextField newY;

  @FXML private Pane pane;
  @FXML private ImageView map;
  @FXML private StackPane stackPane;
  private DrawMap dm = new DrawMap(stackPane, pane, map);

  @FXML private Button toolTip;
  @FXML private Button resetView;

  @FXML private SplitMenuButton floorMenu;
  @FXML private MenuItem parkingPage;
  @FXML private MenuItem floorOnePage;
  @FXML private MenuItem floorTwoPage;
  @FXML private MenuItem floorThreePage;
  @FXML private MenuItem floorFourPage;
  @FXML private MenuItem floorFivePage;

  private String floorNumber = "0";
  private int nodeIDCounter;

  private double scaleMin = 0.75;
  private double scaleMax = 2.5;
  private String direction = "in/out";

  public nodeEdgeDispController() {}

  @FXML
  private void initialize() {
    //    header.toFront();

    anchor.setOnKeyPressed(
        e -> {
          scrollOnPress(e);
          Rectangle viewWindow = new Rectangle(0, 0, stackPane.getWidth(), stackPane.getHeight());
          stackPane.setClip(viewWindow);
        });
    anchor.setOnKeyReleased(e -> scrollOnRelease(e));
    resetView.setOnAction(e -> resetMapView());
    resetView.toFront();
    stackPane.setOnScroll(e -> zoom(e));
    //    stackPane.toBack();

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
    floorMenu.setText("Parking Lot");
    testButton.setOnAction(
        e -> {
          initiateDrawing();
        });

    parkingPage.setOnAction(
        e -> {
          controlImageShown(e, DrawMap.MAP_PAGE.PARKING);
          updateMenuPreview(e);
        });
    floorOnePage.setOnAction(
        e -> {
          controlImageShown(e, DrawMap.MAP_PAGE.FLOOR1);
          updateMenuPreview(e);
        });
    floorTwoPage.setOnAction(
        e -> {
          controlImageShown(e, DrawMap.MAP_PAGE.FLOOR2);
          updateMenuPreview(e);
        });
    floorThreePage.setOnAction(
        e -> {
          controlImageShown(e, DrawMap.MAP_PAGE.FLOOR3);
          updateMenuPreview(e);
        });
    floorFourPage.setOnAction(
        e -> {
          controlImageShown(e, DrawMap.MAP_PAGE.FLOOR4);
          updateMenuPreview(e);
        });
    floorFivePage.setOnAction(
        e -> {
          controlImageShown(e, DrawMap.MAP_PAGE.FLOOR5);
          updateMenuPreview(e);
        });

    // attaches a handler to the button with a lambda expression
    toHomeBtn.setOnAction(e -> buttonClicked(e));

    // set pane to size of image
    stackPane.setMaxWidth(map.getFitWidth());
    stackPane.setMaxHeight(map.getFitHeight());

    // run the create methods on the button click
    addNode.setOnAction(
        e -> {
          createNode(e);
        });
    // addEdge.setOnAction(e -> createEdge(e));

    deleteNode.setOnAction(e -> removeNode(e));
    deleteEdge.setOnAction(e -> removeEdge(e));

    // create node or edge when pane is clicked
    pane.setOnMouseClicked(
        e -> {
          getPaneNode();
          setCurrentDisplay();

          if (addEdgecb.isSelected()) {
            unHighlightCircle();
            unHighglightLine();
            createEdgecb(e);
          } else if (addNodecb.isSelected()) {
            unHighlightCircle();
            unHighglightLine();
            createNodecb(e);

          } else {
            highlightCircle();
            highlightLine();
          }
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
  }

  private void scrollOnPress(KeyEvent e) {
    if (e.getCode() == KeyCode.CONTROL) {
      direction = "up/down";
    } else if (e.getCode() == KeyCode.SHIFT) {
      direction = "left/right";
    } else {
    }
    stackPane.setOnScroll(
        s -> {
          double scaleY = s.getDeltaY() * 0.5;
          double scaleX = s.getDeltaX() * 0.5;
          if (direction.equals("in/out")) {
            zoom(s);
          } else if (direction.equals("up/down")) {
            map.translateYProperty().setValue(map.getTranslateY() + scaleY);
            pane.translateYProperty().setValue(pane.getTranslateY() + scaleY);
          } else if (direction.equals("left/right")) {
            map.translateXProperty().setValue(map.getTranslateX() + scaleX);
            pane.translateXProperty().setValue(pane.getTranslateX() + scaleX);
          } else {

          }
        });
  }

  private void scrollOnRelease(KeyEvent e) {
    direction = "in/out";
    stackPane.setOnScroll(s -> zoom(s));
  }

  private void resetMapView() {
    map.setScaleX(1);
    map.setScaleY(1);
    pane.setScaleX(1);
    pane.setScaleY(1);

    map.translateXProperty().setValue(0);
    map.translateYProperty().setValue(0);
    pane.translateXProperty().setValue(0);
    pane.translateYProperty().setValue(0);
  }

  private void zoom(ScrollEvent e) {
    double scale = e.getDeltaY() * 0.005;
    map.setPreserveRatio(true);

    if (map.getScaleY() > scaleMax && scale > 0) {
      scale = 0;
    } else if (map.getScaleY() < scaleMin && scale < 0) {
      scale = 0;
    } else {

    }
    Rectangle viewWindow = new Rectangle(0, 0, stackPane.getWidth(), stackPane.getHeight());
    stackPane.setClip(viewWindow);
    pane.setScaleY(pane.getScaleY() + scale);
    pane.setScaleX(pane.getScaleX() + scale);

    map.setScaleY(map.getScaleY() + scale);
    map.setScaleX(map.getScaleX() + scale);
  }

  private void updateMenuPreview(ActionEvent e) {
    floorMenu.setText(((MenuItem) e.getSource()).getText());
  }

  // this sucks
  private void initiateDrawing() {
    drawFromCSV();
  }

  private void setImage(DrawMap.MAP_PAGE mp) {

    switch (mp) {
      case FLOOR1:
        map.setImage(dm.setImage(DrawMap.MAP_PAGE.FLOOR1));
        floorNumber = "1";
        break;
      case FLOOR2:
        map.setImage(dm.setImage(DrawMap.MAP_PAGE.FLOOR2));
        floorNumber = "2";
        break;
      case FLOOR3:
        map.setImage(dm.setImage(DrawMap.MAP_PAGE.FLOOR3));
        floorNumber = "3";
        break;
      case FLOOR4:
        map.setImage(dm.setImage(DrawMap.MAP_PAGE.FLOOR4));
        floorNumber = "4";
        break;
      case FLOOR5:
        map.setImage(dm.setImage(DrawMap.MAP_PAGE.FLOOR5));
        floorNumber = "5";
        break;
      case PARKING:
      default:
        map.setImage(dm.setImage(DrawMap.MAP_PAGE.PARKING));
        floorNumber = "0";
        break;
    }
  }

  private void initImage() {
    setImage(DrawMap.MAP_PAGE.PARKING);
    //    map.setFitHeight(500);
    //    map.fitHeightProperty().bind(anchor.heightProperty());
    //    map.fitWidthProperty().bind(anchor.widthProperty());
  }

  private double scaleXCoords(double x) {
    double scale = 1485.0 / 350.0;
    return x / scale;
  }

  private double scaleUpXCoords(double x) {
    double scale = 1485.0 / 350.0;
    return x * scale;
  }

  private double scaleYCoords(double y) {
    double scale = 1485.0 / 350.0;
    return y / scale;
  }

  private double scaleUpYCoords(double y) {
    double scale = 1485.0 / 350.0;
    return y * scale;
  }

  private void controlImageShown(ActionEvent e, DrawMap.MAP_PAGE mp) {
    removeAll(e);
    setImage(mp);
  }

  // --delete functions
  private void removeAll(ActionEvent e) {
    pane.getChildren().remove(0, pane.getChildren().size());
  }

  private void removeEdge(ActionEvent e) {
    pane.getChildren().remove(currentSelectedLine);
  }

  private void removeNode(ActionEvent e) {
    pane.getChildren().remove(currentSelectedCircle);

    // adding the node and refreshing the scene
    Stage stage = (Stage) deleteNode.getScene().getWindow();
    stage.setScene(deleteNode.getScene());
    stage.show();
  }

  // --highlight functions
  private void highlightCircle() {
    // highlights the selected circle which is the startpoint of the edge
    currentSelectedCircle.setStrokeWidth(2);
    currentSelectedCircle.setStroke(Paint.valueOf("BLUE"));
  }

  private void unHighlightCircle() {
    // removes highlight on circle
    currentSelectedCircle.setStrokeWidth(0);
  }

  private void highlightLine() {
    currentSelectedLine.setStrokeWidth(5);
    currentSelectedLine.setStroke(Paint.valueOf("BLUE"));
  }

  private void unHighglightLine() {
    currentSelectedLine.setStrokeWidth(3);
    currentSelectedLine.setStroke(Paint.valueOf("BLACK"));
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

  private void getPaneNode() {
    Circle c = new Circle();
    Line l = new Line();

    for (Node p : pane.getChildren()) {
      try {
        p.setOnMouseClicked(
            w -> {
              if (w.getSource().getClass() == c.getClass()) {
                unHighlightCircle(); // removes current highlight
                currentSelectedCircle =
                    (Circle) w.getSource(); // sets circle to a new selected circle
                p.toFront();
              } else if (w.getSource().getClass() == l.getClass()) {
                unHighglightLine();
                currentSelectedLine = (Line) w.getSource();
                // System.out.println(currentSelectedLine);
                p.toBack();
              } else {
                p.toBack();
              }
            });
      } catch (Exception exp) {
        System.out.println("no point selected");
      }
    }
  }

  // --create node and edges
  private void createEdgecb(MouseEvent e) {
    // creates an edge between two selected points when the checkbox is selected
    if (addEdgecb.isSelected()) {
      if (startEdgeFlag) { // decides if its te starting or ending point being selected
        highlightCircle();
        startEdgeFlag = !startEdgeFlag;
        try {
          startNodeID = currentSelectedCircle.getId();
          startx = currentSelectedCircle.getCenterX();
          starty = currentSelectedCircle.getCenterY();
        } catch (Exception exception) {
          System.out.println("no start point");
        }
      } else {
        startEdgeFlag = !startEdgeFlag;
        try {
          endNodeID = currentSelectedCircle.getId();
          endx = currentSelectedCircle.getCenterX();
          endy = currentSelectedCircle.getCenterY();
        } catch (Exception exception) {
          System.out.println("no end point");
        }

        // createing the line and adding as a child to the pane
        String edgeID = startNodeID + "_" + endNodeID;
        Edge ed = new Edge(edgeID, startNodeID, endNodeID);
        // JDBCUtils.insert(3, ed, "EDGE");
        // JDBCUtils.insert(JDBCUtils.insertString(ed));
        try {
          DatabaseQueryAdministrator.insertEdge(ed);
        } catch (Exception exception) {
          System.out.println("nodeEdgeDispController.createEdgecb");
        }
        CSV.saveEdge(ed);
        Line line = new Line(startx, starty, endx, endy);
        line.setStrokeWidth(3);
        pane.getChildren().add(line);
        line.toBack();
        // refreshing and adding to the scene
        Stage stage = (Stage) anchor.getScene().getWindow();
        stage.setScene(anchor.getScene());
        stage.show();
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
              scaleUpXCoords(e.getX()), scaleUpYCoords(e.getY()), floorNumber, nodeID);
      // JDBCUtils.insert(10, n, "NODE");
      // JDBCUtils.insert(JDBCUtils.insertString(n));
      CSV.saveNode(n);
      try {
        DatabaseQueryAdministrator.insertNode(n);
      } catch (Exception exception) {
        System.out.println("nodeEdgeDispController.createNodecb");
      }
      Circle circle = new Circle(scaleXCoords(n.getXcoord()), scaleYCoords(n.getYcoord()), 3);
      circle.setId(n.getNodeID());
      currentSelectedCircle = circle;
      circle.setFill(Paint.valueOf("RED"));
      pane.getChildren().add(circle);

      // refreshing and adding to scene
      Stage stage = (Stage) addNode.getScene().getWindow();
      stage.setScene(addNode.getScene());
      stage.show();
      //      System.out.println(n.nodeX + "," + n.nodeY);
    }
  }

  private void drawFromCSV() {
    try {
      CSV.getNodes();
      CSV.getEdges();
    } catch (Exception exception) {
      System.out.println("nodeEdgeDispController.drawFromCSV");
    }
    ArrayList<Edge> edgeArrayList;

    nodeIDCounter = CSV.nodes.size() + 1;

    for (edu.wpi.cs3733.c21.teamY.Node n : CSV.nodes) {
      if (n.floor.equals(floorNumber)) {
        double x = n.getXcoord();
        double y = n.getYcoord();
        Circle circle = new Circle(scaleXCoords(x), scaleYCoords(y), 5);
        circle.setId(n.getNodeID());
        currentSelectedCircle = circle;
        circle.setFill(Paint.valueOf("RED"));
        pane.getChildren().add(circle);
      } else {
        // do nothing
      }

      // adding the node and refreshing the scene
      Stage stage = (Stage) addNode.getScene().getWindow();
      stage.setScene(addNode.getScene());
      stage.show();
    }

    for (Edge e : CSV.edges) {
      // System.out.println(pane.getScene());
      try {
        Circle n = (Circle) pane.getScene().lookup("#" + e.getStartNodeID());
        Circle m = (Circle) pane.getScene().lookup("#" + e.getEndNodeID());

        startx = n.getCenterX();
        starty = n.getCenterY();
        endx = m.getCenterX();
        endy = m.getCenterY();
      } catch (Exception exception) { // needs work - problem with nodes connecting floors
        endx = startx; // biases the start node and forgets the end
        endy = starty;
      }

      Line line = new Line(startx, starty, endx, endy);
      line.setStrokeWidth(3);
      pane.getChildren().add(line);
      line.toBack();
      // refreshing and adding to the scene
      Stage stage = (Stage) anchor.getScene().getWindow();
      stage.setScene(anchor.getScene());
      stage.show();
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
              scaleUpXCoords(Double.parseDouble(newX.getText())),
              scaleUpYCoords(Double.parseDouble(newY.getText())),
              floorNumber,
              nodeID);

      // JDBCUtils.insert(10, n, "NODE");
      // JDBCUtils.insert(JDBCUtils.insertString(n));
      DatabaseQueryAdministrator.insertNode(n);
      CSV.saveNode(n);
      Circle circle = new Circle(n.getXcoord(), n.getYcoord(), 3);
      circle.setId(n.getNodeID());
      currentSelectedCircle = circle;
      circle.setFill(Paint.valueOf("RED"));
      pane.getChildren().add(circle);
    } catch (Exception exception) {
      System.out.println("Can't create a node with text in the field input");
    }
    // adding the node and refreshing the scene
    Stage stage = (Stage) addNode.getScene().getWindow();
    stage.setScene(addNode.getScene());
    stage.show();
  }

  // --display info on nodes/edges
  private void setCurrentDisplay() {
    nodeDisplay.setText(
        String.format(
            "x: %.2f, y: %.2f",
            currentSelectedCircle.getCenterX(), currentSelectedCircle.getCenterY()));
    edgeDisplay.setText(
        String.format(
            "start x: %.2f, y: %.2f \nend x: %.2f, %.2f",
            currentSelectedLine.getStartX(),
            currentSelectedLine.getStartY(),
            currentSelectedLine.getEndX(),
            currentSelectedLine.getEndY()));
  }
}
