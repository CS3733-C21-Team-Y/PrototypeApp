package edu.wpi.cs3733.c21.teamY;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
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

  private double startx, starty, endx, endy;

  protected Circle currentSelectedCircle = new Circle(0, 0, 0);
  protected Line currentSelectedLine = new Line(0, 0, 0, 0);

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
    containerStackPane.setMaxWidth(mapImageView.getFitWidth());
    containerStackPane.setMaxHeight(mapImageView.getFitHeight());
    adornerPane.maxWidthProperty().setValue(mapImageView.getImage().widthProperty().getValue());
    adornerPane.maxHeightProperty().setValue(mapImageView.getImage().heightProperty().getValue());

    adornerPane.setScaleX(mapImageView.getScaleX());
    adornerPane.setScaleY(mapImageView.getScaleY());

    adornerPane.setOnMouseClicked(
        e -> {
          //          getPaneNode();
          highlightCircle();
          highlightLine();
        });
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

    Circle circle = new Circle(scaleXCoords(node.getXcoord()), scaleXCoords(node.getYcoord()), 3);
    circle.setId(node.getNodeID());
    currentSelectedCircle = circle;
    circle.setFill(Paint.valueOf("RED"));
    adornerPane.getChildren().add(circle);

    updateMapScreen();
  }

  protected void addEdgeLine(edu.wpi.cs3733.c21.teamY.Edge e) {
    try {
      Circle n = (Circle) adornerPane.getScene().lookup("#" + e.getStartNodeID());
      Circle m = (Circle) adornerPane.getScene().lookup("#" + e.getEndNodeID());

      startx = n.getCenterX();
      starty = n.getCenterY();
      endx = m.getCenterX();
      endy = m.getCenterY();
    } catch (Exception exception) { // needs work - problem with nodes connecting floors
      endx = startx; // biases the start node and forgets the end
      endy = starty;
    }

    Line line = new Line(startx, starty, endx, endy);
    line.setId(e.getEdgeID());
    line.setStrokeWidth(3);
    adornerPane.getChildren().add(line);
    line.toBack();

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
        Circle n = (Circle) getAdornerPane().getScene().lookup("#" + e.getStartNodeID());
        Circle m = (Circle) getAdornerPane().getScene().lookup("#" + e.getEndNodeID());

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

  // --highlight functions
  protected void highlightCircle() {
    // highlights the selected circle which is the startpoint of the edge
    currentSelectedCircle.setStrokeWidth(2);
    currentSelectedCircle.setStroke(Paint.valueOf("BLUE"));
  }

  protected void unHighlightCircle() {
    // removes highlight on circle
    currentSelectedCircle.setStrokeWidth(0);
  }

  protected void highlightLine() {
    currentSelectedLine.setStrokeWidth(5);
    currentSelectedLine.setStroke(Paint.valueOf("BLUE"));
  }

  protected void unHighglightLine() {
    currentSelectedLine.setStrokeWidth(3);
    currentSelectedLine.setStroke(Paint.valueOf("BLACK"));
  }

  protected void getAdornerElement() {
    Circle c = new Circle();
    Line l = new Line();

    for (javafx.scene.Node p : adornerPane.getChildren()) {
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

  protected Pane getAdornerPane() {
    return adornerPane;
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
}
