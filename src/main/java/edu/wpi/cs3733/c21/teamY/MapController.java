package edu.wpi.cs3733.c21.teamY;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.fxml.FXML;
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

  javafx.scene.image.Image parking =
      new javafx.scene.image.Image("edu/wpi/cs3733/c21/teamY/FaulknerCampus.png");
  javafx.scene.image.Image f1 =
      new javafx.scene.image.Image("edu/wpi/cs3733/c21/teamY/FaulknerFloor1_Updated.png");
  javafx.scene.image.Image f2 =
      new javafx.scene.image.Image("edu/wpi/cs3733/c21/teamY/FaulknerFloor2_Updated.png");
  javafx.scene.image.Image f3 =
      new javafx.scene.image.Image("edu/wpi/cs3733/c21/teamY/FaulknerFloor3_Updated.png");
  javafx.scene.image.Image f4 =
      new javafx.scene.image.Image("edu/wpi/cs3733/c21/teamY/FaulknerFloor4_Updated.png");
  javafx.scene.image.Image f5 = new Image("edu/wpi/cs3733/c21/teamY/FaulknerFloor5_Updated.png");

  public MapController() {}

  @FXML
  private void initialize() {

    adornerPane.toFront();
    mapOverlayUIGridPane.toFront();
    containerStackPane.setMaxWidth(mapImageView.getFitWidth());
    containerStackPane.setMaxHeight(mapImageView.getFitHeight());
    adornerPane.maxWidthProperty().setValue(mapImageView.getImage().widthProperty().getValue());
    adornerPane.maxHeightProperty().setValue(mapImageView.getImage().heightProperty().getValue());

    adornerPane.setScaleX(mapImageView.getScaleX());
    adornerPane.setScaleY(mapImageView.getScaleY());

    mapOverlayUIGridPane.setMouseTransparent(true);
  }

  // Image stuff
  protected void changeImage(MapController.MAP_PAGE floor) {
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

  protected void setImage(Image image, MapController.MAP_PAGE floor) {
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

  public Image chooseImage(Stage stage) {
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

  // Adorner Elements
  protected void addNodeCircle(edu.wpi.cs3733.c21.teamY.Node node) {
    CircleEx circleEx =
        new CircleEx(scaleXCoords(node.getXcoord()), scaleXCoords(node.getYcoord()), 3);
    circleEx.setId(node.getNodeID());
    circleEx.setFill(Paint.valueOf("RED"));
    adornerPane.getChildren().add(circleEx);

    updateMapScreen();
  }

  protected void addEdgeLine(edu.wpi.cs3733.c21.teamY.Edge e) {
    try {
      CircleEx n = (CircleEx) adornerPane.getScene().lookup("#" + e.getStartNodeID());
      CircleEx m = (CircleEx) adornerPane.getScene().lookup("#" + e.getEndNodeID());

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
  }

  protected void drawFromCSV(ArrayList<Node> nodes, ArrayList<Edge> edges) {
    try {
      nodes = CSV.getListOfNodes();
      edges = CSV.getListOfEdge();

    } catch (Exception exception) {
      System.out.println("nodeEdgeDispController.drawFromCSV");
    }

    for (edu.wpi.cs3733.c21.teamY.Node n : nodes) {
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

  // scale functions
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

  // selection functions
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

  protected void updateMapScreen() {
    // adding the node and refreshing the scene
    Stage stage = (Stage) containerStackPane.getScene().getWindow();
    stage.setScene(containerStackPane.getScene());
    stage.show();
  }

  // Zoom
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
}
