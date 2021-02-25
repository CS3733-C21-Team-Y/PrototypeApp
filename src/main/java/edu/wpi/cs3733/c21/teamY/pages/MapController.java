package edu.wpi.cs3733.c21.teamY.pages;

import edu.wpi.cs3733.c21.teamY.dataops.CSV;
import edu.wpi.cs3733.c21.teamY.entity.Edge;
import edu.wpi.cs3733.c21.teamY.entity.Node;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MapController {

  @FXML private AnchorPane anchor;
  @FXML private ImageView mapImageView;
  @FXML private Pane adornerPane;
  @FXML protected StackPane containerStackPane;

  @FXML private GridPane mapOverlayUIGridPane;
  @FXML private SplitMenuButton floorMenu;

  private double startx, starty, endx, endy;

  private ArrayList<CircleEx> selectedNodes = new ArrayList<CircleEx>();
  private ArrayList<LineEx> selectedEdges = new ArrayList<LineEx>();

  protected ArrayList<CircleEx> movedNodes = new ArrayList<CircleEx>();

  private FileChooser fc = new FileChooser();
  private File file;

  protected String floorNumber = "0";

  protected enum MAP_PAGE {
    PARKING,
    FLOOR1,
    FLOOR2,
    FLOOR3,
    FLOOR4,
    FLOOR5
  }

  private MapController.MAP_PAGE mp = MapController.MAP_PAGE.PARKING;

  // Used for dynamic menu item creation.
  private ArrayList<MAP_PAGE> mapOrder =
      new ArrayList<MAP_PAGE>(
          Arrays.asList(
              MAP_PAGE.PARKING,
              MAP_PAGE.FLOOR1,
              MAP_PAGE.FLOOR2,
              MAP_PAGE.FLOOR3,
              MAP_PAGE.FLOOR4,
              MAP_PAGE.FLOOR5));

  private double scaleMin = 0.75;
  private double scaleMax = 2.5;
  private String direction = "in/out";

  // these need to be imageViews
  Image parking = new Image("edu/wpi/cs3733/c21/teamY/images/FaulknerParking.png");
  Image f1 = new Image("edu/wpi/cs3733/c21/teamY/images/FaulknerFloor1_Updated.png");
  Image f2 = new Image("edu/wpi/cs3733/c21/teamY/images/FaulknerFloor2_Updated.png");
  Image f3 = new Image("edu/wpi/cs3733/c21/teamY/images/FaulknerFloor3_Updated.png");
  Image f4 = new Image("edu/wpi/cs3733/c21/teamY/images/FaulknerFloor4_Updated.png");
  Image f5 = new Image("edu/wpi/cs3733/c21/teamY/images/FaulknerFloor5_Updated.png");

  // Getters
  protected Pane getAdornerPane() {
    return adornerPane;
  }

  protected SplitMenuButton getFloorMenu() {
    return floorMenu;
  }

  protected ArrayList<MAP_PAGE> getMapOrder() {
    return mapOrder;
  }

  protected ArrayList<CircleEx> getSelectedNodes() {
    return selectedNodes;
  }

  protected ArrayList<LineEx> getSelectedEdges() {
    return selectedEdges;
  }

  public StackPane getContainerStackPane() {
    return containerStackPane;
  }

  public ImageView getMapImageView() {
    return mapImageView;
  }

  public GridPane getMapOverlayUIGridPane() {
    return mapOverlayUIGridPane;
  }

  public MapController() {}

  @FXML
  private void initialize() {

    // scale and fit parking map

    adornerPane.toFront();
    mapOverlayUIGridPane.toFront();
    containerStackPane.setMaxWidth(mapImageView.getFitWidth());
    containerStackPane.setMaxHeight(mapImageView.getFitHeight());
    adornerPane.maxWidthProperty().setValue(mapImageView.getImage().widthProperty().getValue());
    adornerPane.maxHeightProperty().setValue(mapImageView.getImage().heightProperty().getValue());

    adornerPane.setScaleX(mapImageView.getScaleX());
    adornerPane.setScaleY(mapImageView.getScaleY());

    mapOverlayUIGridPane.setPickOnBounds(false);
  }

  // Image stuff
  protected void changeMapImage(MapController.MAP_PAGE floor) {
    switch (floor) {
      case FLOOR1:
        mapImageView.setImage(f1);
        floorNumber = "1";
        break;
      case FLOOR2:
        mapImageView.setImage(f2);
        floorNumber = "2";
        break;
      case FLOOR3:
        mapImageView.setImage(f3);
        floorNumber = "3";
        break;
      case FLOOR4:
        mapImageView.setImage(f4);
        floorNumber = "4";
        break;
      case FLOOR5:
        mapImageView.setImage(f5);
        floorNumber = "5";
        break;
      case PARKING:
      default:
        mapImageView.setImage(parking);
        floorNumber = "0";
        break;
    }
  }

  protected void setNewMapImage(Image image, MapController.MAP_PAGE floor) {
    switch (floor) {
      case FLOOR1:
        f1 = image;
        break;
      case FLOOR2:
        f2 = image;
        break;
      case FLOOR3:
        f3 = image;
        break;
      case FLOOR4:
        f4 = image;
        break;
      case FLOOR5:
        f5 = image;
        break;
      case PARKING:
        parking = image;
        break;
      default:
        System.out.println("image not set");
        break;
    }
    updateMapScreen();
  }

  public Image chooseImageNewFile(Stage stage) {
    fc.setTitle("New Map Image");
    fc.getExtensionFilters()
        .addAll(
            new FileChooser.ExtensionFilter("PNG", "*.png"),
            new FileChooser.ExtensionFilter("JPG", "*.jpg"));
    file = fc.showOpenDialog(stage);
    System.out.println(file);

    Image im = null;
    try {
      im = new Image(new FileInputStream(String.valueOf(file)));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return im;
  }

  // Menu Updates
  protected void updateMenuPreview(ActionEvent e, SplitMenuButton s) {
    s.setText(((MenuItem) e.getSource()).getText());
  }

  // Adorner Elements
  protected CircleEx addNodeCircle(Node node) {
    CircleEx circleEx =
        new CircleEx(scaleXCoords(node.getXcoord()), scaleXCoords(node.getYcoord()), 3);
    circleEx.setId(node.getNodeID());
    circleEx.setFill(Paint.valueOf("RED"));
    adornerPane.getChildren().add(circleEx);

    updateMapScreen();
    return circleEx;
  }

  protected LineEx addEdgeLine(Edge e) {
    try {
      CircleEx n = (CircleEx) adornerPane.getScene().lookup("#" + e.getStartNodeID());
      CircleEx m = (CircleEx) adornerPane.getScene().lookup("#" + e.getEndNodeID());

      // System.out.println(e.edgeID);
      startx = n.getCenterX();
      starty = n.getCenterY();
      endx = m.getCenterX();
      endy = m.getCenterY();
    } catch (Exception exception) { // needs work - problem with nodes connecting floors
      endx = startx; // biases the start node and forgets the end
      endy = starty;
    }

    LineEx lineEx = new LineEx(startx, starty, endx, endy);
    lineEx.setId(e.getEdgeID());
    lineEx.setStrokeWidth(3);
    adornerPane.getChildren().add(lineEx);
    lineEx.toBack();

    updateMapScreen();

    return lineEx;
  }

  protected void addAdornerElements(ArrayList<Node> nodes, ArrayList<Edge> edges, String floor) {

    if (nodes == null || edges == null) {
      nodes = loadNodesFromCSV();
      edges = loadEdgesFromCSV();
      System.out.println("Had to load FromCSV");
    }

    if (nodes.size() == 0 || edges.size() == 1) {
      System.out.println("No nodes or edges");
    }
    for (Node n : nodes) {
      if (n.floor.equals(floorNumber)) {

        double x = n.getXcoord();
        double y = n.getYcoord();
        addNodeCircle(n);

      } else {
        // do nothing
      }
    }

    for (Edge e : edges) {
      // System.out.println(pane.getScene());
      try {
        CircleEx n = (CircleEx) getAdornerPane().getScene().lookup("#" + e.getStartNodeID());
        CircleEx m = (CircleEx) getAdornerPane().getScene().lookup("#" + e.getEndNodeID());

        startx = n.getCenterX();
        starty = n.getCenterY();
        endx = m.getCenterX();
        endy = m.getCenterY();
      } catch (Exception exception) { // needs work - problem with nodes connecting floors
        endx = startx; // biases the start node and forgets the end
        endy = starty;
      }

      addEdgeLine(e);
    }
  }

  protected void removeAllAdornerElements() {
    adornerPane.getChildren().remove(0, adornerPane.getChildren().size());
    selectedNodes = new ArrayList<CircleEx>();
    selectedEdges = new ArrayList<LineEx>();
    updateMapScreen();
  }

  protected void updateMapScreen() {
    // adding the node and refreshing the scene
    Stage stage = (Stage) containerStackPane.getScene().getWindow();
    stage.setScene(containerStackPane.getScene());
    stage.show();
  }

  protected void removeSelected() {
    for (CircleEx c : selectedNodes) {
      adornerPane.getChildren().remove(c);
    }
    for (LineEx l : selectedEdges) {
      adornerPane.getChildren().remove(l);
    }

    selectedNodes = new ArrayList<CircleEx>();
    selectedEdges = new ArrayList<LineEx>();
  }

  // Load From CSV
  protected ArrayList<Node> loadNodesFromCSV() {
    try {
      return CSV.getListOfNodes();

    } catch (Exception exception) {
      System.out.println("nodeEdgeDispController.drawFromCSV");
      return null;
    }
  }

  protected ArrayList<Edge> loadEdgesFromCSV() {
    try {
      return CSV.getListOfEdge();

    } catch (Exception exception) {
      System.out.println("nodeEdgeDispController.drawFromCSV");
      return null;
    }
  }

  // Scale functions
  protected double scaleXCoords(double x) {
    double scale = 1485.0 / 350.0;
    return x / scale;
  }

  protected double scaleUpXCoords(double x) {
    double scale = 1485.0 / 350.0;
    return x * scale;
  }

  protected double scaleYCoords(double y) {
    double scale = 1485.0 / 350.0;
    return y / scale;
  }

  protected double scaleUpYCoords(double y) {
    double scale = 1485.0 / 350.0;
    return y * scale;
  }

  // Selection functions
  protected void clearSelection() {
    // Cannot just deselect because for loop
    for (CircleEx c : selectedNodes) {
      c.setStrokeWidth(0);
      c.hasFocus = false;
    }
    for (LineEx l : selectedEdges) {
      l.setStrokeWidth(3);
      l.setStroke(Paint.valueOf("BLACK"));
      l.hasFocus = false;
    }

    selectedNodes = new ArrayList<CircleEx>();
    selectedEdges = new ArrayList<LineEx>();
  }

  protected void selectCircle(CircleEx c) {
    if (!c.hasFocus) {
      c.setStrokeWidth(2);
      c.setStroke(Paint.valueOf("BLUE"));
      selectedNodes.add(c);
      c.hasFocus = true;
    }
  }

  protected void deSelectCircle(CircleEx c) {
    if (c.hasFocus) {
      c.setStrokeWidth(0);
      selectedNodes.remove(c);
      c.hasFocus = false;
    }
  }

  protected void selectLine(LineEx l) {
    if (!l.hasFocus) {
      l.setStrokeWidth(5);
      l.setStroke(Paint.valueOf("BLUE"));
      selectedEdges.add(l);
      l.hasFocus = true;
    }
  }

  protected void deSelectLine(LineEx l) {
    if (l.hasFocus) {
      l.setStrokeWidth(3);
      l.setStroke(Paint.valueOf("BLACK"));
      selectedEdges.remove(l);
      l.hasFocus = false;
    }
  }

  // Zoom and Pan
  protected void scrollOnPress(KeyEvent e) {
    if (e.getCode() == KeyCode.CONTROL) {
      direction = "up/down";
    } else if (e.getCode() == KeyCode.SHIFT) {
      direction = "left/right";
    } else {
    }
    containerStackPane.setOnScroll(
        s -> {
          double scaleY = s.getDeltaY() * 0.5;
          double scaleX = s.getDeltaX() * 0.5;
          if (direction.equals("in/out")) {
            zoom(s);
          } else if (direction.equals("up/down")) {
            mapImageView.translateYProperty().setValue(mapImageView.getTranslateY() + scaleY);
            adornerPane.translateYProperty().setValue(adornerPane.getTranslateY() + scaleY);
          } else if (direction.equals("left/right")) {
            mapImageView.translateXProperty().setValue(mapImageView.getTranslateX() + scaleX);
            adornerPane.translateXProperty().setValue(adornerPane.getTranslateX() + scaleX);
          } else {

          }
        });
  }

  protected void scrollOnRelease(KeyEvent e) {
    direction = "in/out";
    containerStackPane.setOnScroll(s -> zoom(s));
  }

  protected void panOnButtons(String dir) {
    int movement = 10;
    if (dir.equals("up")) {
      mapImageView.translateYProperty().setValue(mapImageView.getTranslateY() + movement);
      adornerPane.translateYProperty().setValue(adornerPane.getTranslateY() + movement);
    } else if (dir.equals("down")) {
      mapImageView.translateYProperty().setValue(mapImageView.getTranslateY() - movement);
      adornerPane.translateYProperty().setValue(adornerPane.getTranslateY() - movement);
    } else if (dir.equals("right")) {
      mapImageView.translateXProperty().setValue(mapImageView.getTranslateX() - movement);
      adornerPane.translateXProperty().setValue(adornerPane.getTranslateX() - movement);
    } else if (dir.equals("left")) {
      mapImageView.translateXProperty().setValue(mapImageView.getTranslateX() + movement);
      adornerPane.translateXProperty().setValue(adornerPane.getTranslateX() + movement);
    } else {
    }

    Rectangle viewWindow =
        new Rectangle(0, 0, containerStackPane.getWidth(), containerStackPane.getHeight());
    containerStackPane.setClip(viewWindow);
  }

  protected void resetMapView() {
    mapImageView.setScaleX(1);
    mapImageView.setScaleY(1);
    adornerPane.setScaleX(1);
    adornerPane.setScaleY(1);

    mapImageView.translateXProperty().setValue(0);
    mapImageView.translateYProperty().setValue(0);
    adornerPane.translateXProperty().setValue(0);
    adornerPane.translateYProperty().setValue(0);
  }

  protected void zoom(ScrollEvent e) {
    double scale = e.getDeltaY() * 0.005;
    mapImageView.setPreserveRatio(true);

    if (mapImageView.getScaleY() > scaleMax && scale > 0) {
      scale = 0;
    } else if (mapImageView.getScaleY() < scaleMin && scale < 0) {
      scale = 0;
    } else {

    }
    Rectangle viewWindow =
        new Rectangle(0, 0, containerStackPane.getWidth(), containerStackPane.getHeight());
    containerStackPane.setClip(viewWindow);
    adornerPane.setScaleY(adornerPane.getScaleY() + scale);
    adornerPane.setScaleX(adornerPane.getScaleX() + scale);

    mapImageView.setScaleY(mapImageView.getScaleY() + scale);
    mapImageView.setScaleX(mapImageView.getScaleX() + scale);
  }

  protected void zoomOnButtons(String dir) {
    double scale = 0.1;
    if (dir.equals("in")) {
      adornerPane.setScaleY(adornerPane.getScaleY() + scale);
      adornerPane.setScaleX(adornerPane.getScaleX() + scale);

      mapImageView.setScaleY(mapImageView.getScaleY() + scale);
      mapImageView.setScaleX(mapImageView.getScaleX() + scale);

    } else if (dir.equals("out")) {

      adornerPane.setScaleY(adornerPane.getScaleY() - scale);
      adornerPane.setScaleX(adornerPane.getScaleX() - scale);

      mapImageView.setScaleY(mapImageView.getScaleY() - scale);
      mapImageView.setScaleX(mapImageView.getScaleX() - scale);

    } else {
    }

    Rectangle viewWindow =
        new Rectangle(0, 0, containerStackPane.getWidth(), containerStackPane.getHeight());
    containerStackPane.setClip(viewWindow);
  }

  // Better Adorners
  public class CircleEx extends Circle {
    public boolean hasFocus = false;

    public CircleEx(double radius) {
      super(radius);
    }

    public CircleEx(double radius, Paint fill) {
      super(radius, fill);
    }

    public CircleEx() {}

    public CircleEx(double centerX, double centerY, double radius) {
      super(centerX, centerY, radius);
    }

    public CircleEx(double centerX, double centerY, double radius, Paint fill) {
      super(centerX, centerY, radius, fill);
    }
  }

  public class LineEx extends Line {
    public boolean hasFocus = false;

    public LineEx() {}

    public LineEx(double startX, double startY, double endX, double endY) {
      super(startX, startY, endX, endY);
    }
  }

  protected void moveSelected(ArrayList<CircleEx> circles, ArrayList<LineEx> lines, String dir) {
    int movement = 10;
    for (CircleEx c : circles) {
      if (dir.equals("up")) {
        c.setCenterY(c.getCenterY() - movement);
      } else if (dir.equals("down")) {
        c.setCenterY(c.getCenterY() + movement);
      } else if (dir.equals("left")) {
        c.setCenterX(c.getCenterX() - movement);
      } else if (dir.equals("right")) {
        c.setCenterX(c.getCenterX() + movement);
      } else {

      }

      if (!movedNodes.contains(c)) {
        movedNodes.add(c);
      }
    }

    System.out.println(movedNodes);

    for (LineEx l : lines) {
      if (dir.equals("up")) {
        l.setStartY(l.getStartY() - movement);
        l.setEndY(l.getEndY() - movement);
      } else if (dir.equals("down")) {
        l.setStartY(l.getStartY() + movement);
        l.setEndY(l.getEndY() + movement);
      } else if (dir.equals("left")) {
        l.setStartX(l.getStartX() - movement);
        l.setEndX(l.getEndX() - movement);
      } else if (dir.equals("right")) {
        l.setStartX(l.getStartX() + movement);
        l.setEndX(l.getEndX() + movement);
      } else {

      }
    }
  }
}