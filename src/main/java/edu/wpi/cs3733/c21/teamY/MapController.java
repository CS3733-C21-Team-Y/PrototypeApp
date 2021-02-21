package edu.wpi.cs3733.c21.teamY;

import java.io.File;
import java.io.FileInputStream;
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
  @FXML private ImageView map;
  @FXML private Pane pane;
  @FXML protected StackPane stackPane;

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

    pane.toFront();
    stackPane.setMaxWidth(map.getFitWidth());
    stackPane.setMaxHeight(map.getFitHeight());
    pane.maxWidthProperty().setValue(map.getImage().widthProperty().getValue());
    pane.maxHeightProperty().setValue(map.getImage().heightProperty().getValue());

    pane.setScaleX(map.getScaleX());
    pane.setScaleY(map.getScaleY());

    pane.setOnMouseClicked(
        e -> {
          //          getPaneNode();
          highlightCircle();
          highlightLine();
        });
  }

  protected void changeImage(MapController.MAP_PAGE floor) {
    switch (floor) {
      case FLOOR1:
        map.setImage(f1);
        floorNumber = "1";
        break;
      case FLOOR2:
        map.setImage(f2);
        floorNumber = "2";
        break;
      case FLOOR3:
        map.setImage(f3);
        floorNumber = "3";
        break;
      case FLOOR4:
        map.setImage(f4);
        floorNumber = "4";
        break;
      case FLOOR5:
        map.setImage(f5);
        floorNumber = "5";
        break;
      case PARKING:
      default:
        map.setImage(parking);
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

  protected void addNodeCircle(edu.wpi.cs3733.c21.teamY.Node node) {

    Circle circle = new Circle(scaleXCoords(node.getXcoord()), scaleXCoords(node.getYcoord()), 3);
    circle.setId(node.getNodeID());
    currentSelectedCircle = circle;
    circle.setFill(Paint.valueOf("RED"));
    pane.getChildren().add(circle);

    updateMapScreen();
  }

  protected void addEdgeLine(edu.wpi.cs3733.c21.teamY.Edge e) {
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
    line.setId(e.getEdgeID());
    line.setStrokeWidth(3);
    pane.getChildren().add(line);
    line.toBack();

    updateMapScreen();
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

  protected void getPaneNode() {
    Circle c = new Circle();
    Line l = new Line();

    for (javafx.scene.Node p : pane.getChildren()) {
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

  protected Pane getPane() {
    return pane;
  }

  protected void updateMapScreen() {
    // adding the node and refreshing the scene
    Stage stage = (Stage) stackPane.getScene().getWindow();
    stage.setScene(stackPane.getScene());
    stage.show();
  }

  protected void scrollOnPress(KeyEvent e) {
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

  protected void scrollOnRelease(KeyEvent e) {
    direction = "in/out";
    stackPane.setOnScroll(s -> zoom(s));
  }

  protected void resetMapView() {
    map.setScaleX(1);
    map.setScaleY(1);
    pane.setScaleX(1);
    pane.setScaleY(1);

    map.translateXProperty().setValue(0);
    map.translateYProperty().setValue(0);
    pane.translateXProperty().setValue(0);
    pane.translateYProperty().setValue(0);
  }

  protected void zoom(ScrollEvent e) {
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
}
