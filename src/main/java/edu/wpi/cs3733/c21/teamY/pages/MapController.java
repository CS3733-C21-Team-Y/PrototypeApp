package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import edu.wpi.cs3733.c21.teamY.dataops.CSV;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.entity.Edge;
import edu.wpi.cs3733.c21.teamY.entity.Node;
import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MapController extends SubPage {

  @FXML protected AnchorPane anchor;
  @FXML private ImageView mapImageView;
  @FXML private Pane adornerPane;
  @FXML protected StackPane containerStackPane;

  @FXML private GridPane mapOverlayUIGridPane;
  // @FXML private HBox overlayHBox;

  @FXML private JFXButton reset;
  @FXML private JFXNodesList floorList;
  @FXML private JFXButton currentFloorBtn;

  private boolean isAdminPage;
  private boolean floorListOpen = false;
  // endregion

  // region Fields
  private double startx, starty, endx, endy;

  private boolean dragging = false;
  private boolean lastClickDrag = false;

  private double dragStartX;
  private double dragStartY;

  private ArrayList<CircleEx> selectedNodes = new ArrayList<CircleEx>();
  private ArrayList<LineEx> selectedEdges = new ArrayList<LineEx>();

  private FileChooser fc = new FileChooser();
  private File file;

  protected String floorNumber = "0";

  private boolean displayUnselectedAdorners = true;

  // Used for adorner scaling
  private double baseCircleRadius = 6; // 3;
  private double baseLineWidth = 6; // 2;
  private double selectedWidthRatio = 1.5; // 2;

  // Need to update these values properly
  private double scaledCircleRadius = 0;
  private double scaledLineWidth = 0;
  private double scaledLineWidthSelected = 0;

  public boolean isAdminPage() {
    return isAdminPage;
  }

  public void setAdminPage(boolean adminPage) {
    isAdminPage = adminPage;
  }

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

  private double scaleMin = 0.1;
  private double scaleMax = 20;
  private String direction = "in/out";

  // these need to be imageViews
  Image parking = new Image("/edu/wpi/cs3733/c21/teamY/images/FaulknerCampusIT2.png");
  Image f1 = new Image("/edu/wpi/cs3733/c21/teamY/images/FaulknerFloor1.png");
  Image f2 = new Image("/edu/wpi/cs3733/c21/teamY/images/FaulknerFloor2.png");
  Image f3 = new Image("/edu/wpi/cs3733/c21/teamY/images/FaulknerFloor3.png");
  Image f4 = new Image("/edu/wpi/cs3733/c21/teamY/images/FaulknerFloor4.png");
  Image f5 = new Image("/edu/wpi/cs3733/c21/teamY/images/FaulknerFloor5.png");

  private Double mouseX;
  private Double mouseY;
  // endregion

  public MapController() {}

  @FXML
  private void initialize() {

    reset.setOnAction(e -> resetMapView());
    reset.toFront();

    adornerPane.toFront();

    floorList.setRotate(90);
    currentFloorBtn.setOnAction(e -> floorMenuAction());

    //    floorMenu.setOnAction(
    //        event -> {
    //    int i = 0;
    //    for (MenuItem menuItem : floorMenu.getItems()) {
    //      int index = i;
    //      menuItem.setOnAction(
    //          e -> {
    //            removeAllAdornerElements();
    //            changeMapImage(getMapOrder().get(index));
    //            //            System.out.println(mapImageView.getImage());
    //            updateMenuPreview(e, getFloorMenu());
    //          });
    //      i++;
    //    }
    //        });
    mapImageView.toBack();
    mapOverlayUIGridPane.toFront();
    containerStackPane.setMaxWidth(mapImageView.getFitWidth());
    containerStackPane.setMaxHeight(mapImageView.getFitHeight());

    mapImageView.setImage(parking);
    adornerPane.maxWidthProperty().setValue(mapImageView.getImage().widthProperty().getValue());

    adornerPane.maxHeightProperty().setValue(mapImageView.getImage().heightProperty().getValue());

    adornerPane.setScaleX(mapImageView.getScaleX());
    adornerPane.setScaleY(mapImageView.getScaleY());

    mapOverlayUIGridPane.setPickOnBounds(false);
    // overlayHBox.setPickOnBounds(false);

    // Default AdornerPane click and drag
    adornerPane.setOnMousePressed(
        e -> {
          defaultOnMousePressed(e);
        });
    adornerPane.setOnMouseDragged(
        e -> {
          defaultOnMouseDragged(e);
        });
    adornerPane.setOnMouseReleased(
        e -> {
          defaultOnMouseReleased(e);
        });

    // Set Zoom
    anchor.setOnKeyPressed(
        e -> {
          scrollOnPress(e);
        });
    anchor.setOnKeyReleased(e -> scrollOnRelease(e));
    containerStackPane.setOnScroll(e -> zoom(e));

    // Sets Map clip so nothing can appear outside map bounds
    Platform.runLater(
        () -> {
          /*mapImageView
              .getScene()
              .getWindow()
              .widthProperty()
              .addListener(
                  new ChangeListener<Number>() {
                    @Override
                    public void changed(
                        ObservableValue<? extends Number> observable,
                        Number oldValue,
                        Number newValue) {
                      mapImageView.setScaleX(
                          mapImageView.getScene().getWindow().getWidth()
                              / mapImageView.getFitWidth());
                    }
                  });

          mapImageView
              .getScene()
              .getWindow()
              .heightProperty()
              .addListener(
                  new ChangeListener<Number>() {
                    @Override
                    public void changed(
                        ObservableValue<? extends Number> observable,
                        Number oldValue,
                        Number newValue) {
                      mapImageView.setScaleY(
                          mapImageView.getScene().getWindow().getHeight()
                              / mapImageView.getFitHeight());
                    }
                  });*/

          mapImageView.fitWidthProperty().bind(mapImageView.getImage().widthProperty());
          mapImageView.fitHeightProperty().bind(mapImageView.getImage().heightProperty());

          adornerPane.minWidthProperty().bind(mapImageView.getImage().widthProperty());
          adornerPane.minHeightProperty().bind(mapImageView.getImage().heightProperty());

          mapImageView.setTranslateX(0 - mapImageView.getFitWidth() / 2);
          // mapImageView.setTranslateY(0 - mapImageView.getFitHeight() / 2);
          adornerPane.setTranslateX(0 - mapImageView.getFitWidth() / 2);
          // adornerPane.setTranslateY(0 - mapImageView.getFitHeight() / 2);

          mapImageView.setScaleX(0.5);
          mapImageView.setScaleY(0.5);
          adornerPane.setScaleX(0.5);
          adornerPane.setScaleY(0.5);

          mapImageView.setTranslateX(mapImageView.getTranslateX() + adornerPane.getWidth() / 2);
          // mapImageView.setTranslateY(0 - mapImageView.getFitHeight() / 2);
          adornerPane.setTranslateX(adornerPane.getTranslateX() + adornerPane.getWidth() / 2);
          // adornerPane.setTranslateY(0 - mapImageView.getFitHeight() / 2);

          Rectangle viewWindow = new Rectangle(0, 0, 9999, 9999999);
          containerStackPane.setClip(viewWindow);

          containerStackPane.setOnMouseMoved(
              e -> {
                mouseX = e.getX();
                mouseY = e.getY();
              });

          updateAdornerVisualsOnZoom();
          resetMapView();
        });
  }

  // region Getters and Setters

  public JFXButton getReset() {
    return reset;
  }

  protected Pane getAdornerPane() {
    return adornerPane;
  }

  protected JFXNodesList getFloorList() {
    return floorList;
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

  public boolean isDragging() {
    return dragging;
  }

  public boolean wasLastClickDrag() {
    return lastClickDrag;
  }

  public boolean isDisplayUnselectedAdorners() {
    return displayUnselectedAdorners;
  }

  public double getBaseCircleRadius() {
    return baseCircleRadius;
  }

  public void setBaseCircleRadius(double baseCircleRadius) {
    this.baseCircleRadius = baseCircleRadius;
    updateAdornerVisualsOnZoom();
  }

  public double getBaseLineWidth() {
    return baseLineWidth;
  }

  public void setBaseLineWidth(double baseLineWidth) {
    this.baseLineWidth = baseLineWidth;
    updateAdornerVisualsOnZoom();
  }

  public double getSelectedWidthRatio() {
    return selectedWidthRatio;
  }

  public void setSelectedWidthRatio(double selectedWidthRatio) {
    this.selectedWidthRatio = selectedWidthRatio;
    updateAdornerVisualsOnZoom();
  }

  public void setFitHeight(double h) {
    mapImageView.setFitHeight(h);
  }

  public void setDisplayUnselectedAdorners(boolean displayUnselectedAdorners) {
    if (displayUnselectedAdorners != this.displayUnselectedAdorners) {
      this.displayUnselectedAdorners = displayUnselectedAdorners;

      if (!displayUnselectedAdorners) {
        for (javafx.scene.Node child : adornerPane.getChildren()) {

          if (child instanceof CircleEx) {
            CircleEx c = (CircleEx) child;
            if (c.isVisible() && !c.hasFocus) {
              c.setVisible(false);
            }
          }

          if (child instanceof LineEx) {
            LineEx l = (LineEx) child;
            if (l.isVisible() && !l.hasFocus) {
              l.setVisible(false);
            }
          }
        }
      } else {
        for (javafx.scene.Node child : adornerPane.getChildren()) {
          if (!child.isVisible()) {
            child.setVisible(true);
          }
        }
      }
    }
  }

  public double getScaledLineWidthSelected() {
    return scaledLineWidthSelected;
  }
  // endregion

  // region Default Drag Handlers
  protected void defaultOnMousePressed(MouseEvent e) {
    dragStartX = e.getX();
    dragStartY = e.getY();
  }

  protected void defaultOnMouseDragged(MouseEvent e) {

    dragging = true;

    double dragDeltaX = dragStartX - e.getX();
    double dragDeltaY = dragStartY - e.getY();

    mapImageView.setTranslateX(
        mapImageView.getTranslateX() - dragDeltaX * mapImageView.getScaleX());
    mapImageView.setTranslateY(
        mapImageView.getTranslateY() - dragDeltaY * mapImageView.getScaleY());
    adornerPane.setTranslateX(adornerPane.getTranslateX() - dragDeltaX * mapImageView.getScaleX());
    adornerPane.setTranslateY(adornerPane.getTranslateY() - dragDeltaY * mapImageView.getScaleY());
  }

  protected void defaultOnMouseReleased(MouseEvent e) {
    lastClickDrag = dragging;
    dragging = false;
  }
  // endregion

  // region Change Floor or Image
  private void floorMenuAction() {
    Timeline timeline = new Timeline();
    // Timeline timeline2 = new Timeline();

    ArrayList<KeyValue> values = new ArrayList<KeyValue>();

    KeyValue kv1;
    KeyValue kv2;

    Background background;

    if (floorListOpen) {
      background = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
      //      kv1 = new KeyValue(anchor.backgroundProperty(), background, Interpolator.EASE_IN);
      kv2 = new KeyValue(containerStackPane.opacityProperty(), 1, Interpolator.EASE_IN);

    } else {
      background = new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY));
      //      kv1 = new KeyValue(anchor.backgroundProperty(), background, Interpolator.EASE_IN);
      kv2 = new KeyValue(containerStackPane.opacityProperty(), 0.3, Interpolator.EASE_IN);
      anchor.setBackground(background);
    }

    ArrayList<KeyValue> keyValues = new ArrayList<KeyValue>();
    //  keyValues.add(kv1);
    keyValues.add(kv2);
    KeyFrame kf2 = new KeyFrame(Duration.seconds(0.25), "", null, keyValues);
    timeline.getKeyFrames().add(kf2);
    timeline.setOnFinished(event -> anchor.setBackground(background));
    timeline.play();
    floorListOpen = !floorListOpen;
  }

  protected void changeMapImage(MapController.MAP_PAGE floor) {
    switch (floor) {
      case FLOOR1:
        mapImageView.setImage(f1);
        floorNumber = "1";
        currentFloorBtn.setText("1");
        break;
      case FLOOR2:
        mapImageView.setImage(f2);
        floorNumber = "2";
        currentFloorBtn.setText("2");
        break;
      case FLOOR3:
        mapImageView.setImage(f3);
        floorNumber = "3";
        currentFloorBtn.setText("3");
        break;
      case FLOOR4:
        mapImageView.setImage(f4);
        floorNumber = "4";
        currentFloorBtn.setText("4");
        break;
      case FLOOR5:
        mapImageView.setImage(f5);
        floorNumber = "5";
        currentFloorBtn.setText("5");
        break;
      case PARKING:
      default:
        mapImageView.setImage(parking);
        floorNumber = "0";
        currentFloorBtn.setText("P");
        break;
    }
    floorList.animateList(false);
    floorMenuAction();
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
  // endregion

  // region Menu Updates
  protected void updateMenuPreview(ActionEvent e, SplitMenuButton s) {
    s.setText(((MenuItem) e.getSource()).getText());
  }
  // endregion

  // region Manage Adorner Elements
  protected CircleEx addNodeCircle(Node node) {
    CircleEx circleEx =
        new CircleEx(
            scaleXCoords(node.getXcoord()),
            scaleXCoords(node.getYcoord()),
            baseCircleRadius); // adornerPane.getScaleX());
    // System.out.println(node.getNodeID());
    circleEx.setId(node.getNodeID());
    circleEx.setFill(Paint.valueOf("RED"));
    // circleEx.setNode(node);
    //    System.out.println("About to Add!");
    //    Button button = new Button("B");
    //    button.setTranslateX(scaleXCoords(node.getXcoord()));
    //    button.setTranslateY(scaleXCoords(node.getYcoord()));
    //    adornerPane.getChildren().add(button);
    adornerPane.getChildren().add(circleEx);
    circleEx.setOpacity(1);
    adornerPane.setOpacity(1);
    circleEx.toFront();
    // System.out.println("Added!");

    if (!displayUnselectedAdorners) {
      circleEx.setVisible(false);
    }

    updateMapScreen();
    return circleEx;
  }

  protected LineEx addEdgeLine(Edge edge) {
    CircleEx n = null;
    CircleEx m = null;
    try {
      n = (CircleEx) adornerPane.lookup("#" + edge.getStartNodeID());
      m = (CircleEx) adornerPane.lookup("#" + edge.getEndNodeID());

      // System.out.println(e.edgeID);
      startx = n.getCenterX();
      starty = n.getCenterY();
      endx = m.getCenterX();
      endy = m.getCenterY();
    } catch (Exception exception) { // needs work - problem with nodes connecting floors
      endx = startx; // biases the start node and forgets the end
      endy = starty;
    }

    if (n == null && m == null) {
      System.out.println("OK ITS NULL");
      return null;
    }

    LineEx lineEx = new LineEx(startx, starty, endx, endy);
    lineEx.startNode = n;
    lineEx.endNode = m;
    lineEx.setId(edge.getEdgeID());
    lineEx.setStrokeWidth(scaledLineWidth);
    // lineEx.setEdge(edge);
    adornerPane.getChildren().add(lineEx);
    lineEx.toBack();

    if (!displayUnselectedAdorners) {
      lineEx.setVisible(false);
    }

    if (n != null && m != null) {
      if (n != null && !n.connectingEdges.contains(lineEx)) {
        n.connectingEdges.add(lineEx);
      }
      if (m != null && !m.connectingEdges.contains(lineEx)) {
        m.connectingEdges.add(lineEx);
      }
    }

    updateMapScreen();
    return lineEx;
  }

  protected void addAdornerElements(ArrayList<Node> nodes, ArrayList<Edge> edges, String floor) {
    //    Button button = new Button("BUTTTONNNNNNN");
    //    adornerPane.getChildren().add(button);
    if (nodes == null || edges == null) {
      System.out.println("Had no nodes or edges!");
    }

    if (nodes.size() == 0 || edges.size() == 0) {
      System.out.println("No nodes or edges");
    }
    for (Node n : nodes) {
      if (n.floor.equals(floor)) {

        double x = n.getXcoord();
        double y = n.getYcoord();
        addNodeCircle(n);

      } else {
        // do nothing
        // System.out.println(n.floor);
      }
    }

    for (Edge e : edges) {
      // System.out.println(pane.getScene());
      CircleEx n = null;
      CircleEx m = null;
      try {
        n = (CircleEx) adornerPane.lookup("#" + e.getStartNodeID());
        m = (CircleEx) adornerPane.lookup("#" + e.getEndNodeID());

        startx = n.getCenterX();
        starty = n.getCenterY();
        endx = m.getCenterX();
        endy = m.getCenterY();
      } catch (Exception exception) { // needs work - problem with nodes connecting floors
        endx = startx; // biases the start node and forgets the end
        endy = starty;
      }

      if (n != m) {
        if (!(n == null || m == null)) {
          addEdgeLine(e);
        }
      }
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
  // endregion

  // region Load Nodes Edges
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

  protected ArrayList<Node> loadNodesFromDB() {
    try {
      return DataOperations.getListOfNodes();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      return null;
    }
  }

  protected ArrayList<Edge> loadEdgesFromDB() {
    try {
      return DataOperations.getListOfEdge();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      return null;
    }
  }
  // endregion

  // region Scaling
  protected double scaleXCoords(double x) {
    double scale = 1; // 1485.0 / 350.0;
    return x / scale;
  }

  protected double scaleUpXCoords(double x) {
    double scale = 1; // mapImageView.getImage().getWidth() / ;
    return x * scale;
  }

  protected double scaleYCoords(double y) {
    double scale = 1; // 1485.0 / 350.0;
    return y / scale;
  }

  protected double scaleUpYCoords(double y) {
    double scale = 1; // 1485.0 / 350.0;
    return y * scale;
  }
  // endregion

  // region Selection
  protected void clearSelection() {
    // Cannot just deselect because for loop
    clearCircleSelection();
    clearLineSelection();
  }

  protected void clearCircleSelection() {
    for (CircleEx c : selectedNodes) {
      // Cannot call deselect because it removs from selectedNodes
      c.hasFocus = false;
      c.updateVisuals();
      if (!displayUnselectedAdorners) {
        c.setVisible(false);
      }
    }
    selectedNodes = new ArrayList<CircleEx>();
  }

  protected void clearLineSelection() {
    for (LineEx l : selectedEdges) {
      // Cannot call deselect because it removs from selectedEdges
      l.hasFocus = false;
      l.clearDirectionality();
      if (!displayUnselectedAdorners) {
        l.setVisible(false);
      }
    }
    selectedEdges = new ArrayList<LineEx>();
  }

  protected void selectCircle(CircleEx c) {
    if (!c.hasFocus) {
      c.hasFocus = true;
      c.updateVisuals();
      c.updateAdjacentEdges();
      selectedNodes.add(c);
      c.setVisible(true);
    }
  }

  protected void deSelectCircle(CircleEx c) {
    if (c.hasFocus) {
      c.hasFocus = false;
      c.updateVisuals();
      c.updateAdjacentEdges();
      selectedNodes.remove(c);
      if (!displayUnselectedAdorners) {
        c.setVisible(false);
      }
    }
  }

  protected void selectLine(LineEx l) {
    if (!l.hasFocus) {
      l.hasFocus = true;
      l.updateVisuals();
      selectedEdges.add(l);
      l.setVisible(true);
    }
  }

  protected void deSelectLine(LineEx l) {
    if (l.hasFocus) {
      l.hasFocus = false;
      l.clearDirectionality();
      selectedEdges.remove(l);
      if (!displayUnselectedAdorners) {
        l.setVisible(false);
      }
    }
  }

  protected void selectCirclesFromList(ArrayList<CircleEx> list) {
    for (CircleEx circle : list) {
      selectCircle(circle);
    }
  }

  protected void selectLinesFromList(ArrayList<LineEx> list) {
    for (LineEx line : list) {
      selectLine(line);
    }
  }
  // endregion

  // region ScrollHandlers and Pan
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
  }
  // endregion

  // region Zoom
  protected void zoom(ScrollEvent e) {
    double widthBefore = adornerPane.getWidth();
    double heightBefore = adornerPane.getHeight();

    double scale = Math.pow(Math.E, e.getDeltaY() * 0.005);
    zoomStolen(scale, e.getX(), e.getY());
    updateAdornerVisualsOnZoom();
  }

  protected void shiftedZoom(ScrollEvent e, double xTrans, double yTrans) {
    double widthBefore = adornerPane.getWidth();
    double heightBefore = adornerPane.getHeight();

    double scale = Math.pow(Math.E, e.getDeltaY() * 0.005);
    zoomStolen(scale, e.getX() + xTrans, e.getY() + yTrans);
    updateAdornerVisualsOnZoom();
  }

  protected void zoomOnButtons(double scrollAmount) {

    double scale = Math.pow(Math.E, scrollAmount);

    mapImageView.setPreserveRatio(true);

    // System.out.println(adornerPane.getScaleX() * scale);
    // System.out.println(adornerPane.getScaleY() * scale);

    if (adornerPane.getScaleX() * scale > scaleMax || adornerPane.getScaleX() * scale < scaleMin) {
      return;
    }

    adornerPane.setScaleX(adornerPane.getScaleX() * scale);
    adornerPane.setScaleY(adornerPane.getScaleY() * scale);

    mapImageView.setScaleX(mapImageView.getScaleX() * scale);
    mapImageView.setScaleY(mapImageView.getScaleY() * scale);
    updateAdornerVisualsOnZoom();
  }

  public void zoomStolen(double factor, double x, double y) {
    // determine scale
    double oldScale = mapImageView.getScaleX();
    double scale = oldScale * factor;
    double f = (scale / oldScale) - 1;

    if (scale < scaleMin || scale > scaleMax) {
      return;
    }

    // determine offset that we will have to move the node
    Bounds bounds = mapImageView.localToScene(mapImageView.getBoundsInLocal());
    double dx = (x - (bounds.getWidth() / 2 + bounds.getMinX()));
    double dy = (y - (bounds.getHeight() / 2 + bounds.getMinY()));

    /*
    // timeline that scales and moves the node
    timeline.getKeyFrames().clear();
    timeline.getKeyFrames().addAll(
            new KeyFrame(Duration.millis(200), new KeyValue(node.translateXProperty(), node.getTranslateX() - f * dx)),
            new KeyFrame(Duration.millis(200), new KeyValue(node.translateYProperty(), node.getTranslateY() - f * dy)),
            new KeyFrame(Duration.millis(200), new KeyValue(node.scaleXProperty(), scale)),
            new KeyFrame(Duration.millis(200), new KeyValue(node.scaleYProperty(), scale))
    );
    timeline.play();*/
    mapImageView.setTranslateX(mapImageView.getTranslateX() - f * dx);
    adornerPane.setTranslateX(adornerPane.getTranslateX() - f * dx);

    mapImageView.setTranslateY(mapImageView.getTranslateY() - f * dy);
    adornerPane.setTranslateY(adornerPane.getTranslateY() - f * dy);

    mapImageView.setScaleX(scale);
    mapImageView.setScaleY(scale);
    adornerPane.setScaleX(scale);
    adornerPane.setScaleY(scale);
  }

  protected void updateAdornerVisualsOnZoom() {
    scaledCircleRadius = baseCircleRadius; // adornerPane.getScaleX();
    scaledLineWidth = baseLineWidth; // adornerPane.getScaleX();
    scaledLineWidthSelected = baseLineWidth; // adornerPane.getScaleX() * selectedWidthRatio;

    for (javafx.scene.Node adorner : adornerPane.getChildren()) {
      if (adorner instanceof CircleEx) {
        CircleEx circ = (CircleEx) adorner;
        if (circ.hasFocus) {
          circ.setStrokeWidth(scaledLineWidth);
        }
        circ.setRadius(scaledCircleRadius);
      } else {
        if (adorner instanceof LineEx) {
          LineEx line = (LineEx) adorner;
          if (line.hasFocus) {
            line.setStrokeWidth(scaledLineWidthSelected);
          } else {
            line.setStrokeWidth(scaledLineWidth);
          }
        }
      }
    }
  }
  // endregion

  // region resetView
  //  protected void resetMapView() {
  //    mapImageView.setScaleX(0.8);
  //    mapImageView.setScaleY(0.8);
  //    adornerPane.setScaleX(0.8);
  //    adornerPane.setScaleY(0.8);
  //
  //    mapImageView.translateXProperty().setValue(-500);
  //    mapImageView.translateYProperty().setValue(-500);
  //    adornerPane.translateXProperty().setValue(-500);
  //    adornerPane.translateYProperty().setValue(-500);
  //
  //    updateAdornerVisualsOnZoom();
  //  }

  protected void resetMapView() {
    System.out.println(parent.isDesktop);
    if (parent.isDesktop) {
      if (isAdminPage) {
        mapImageView.setScaleX(0.8);
        mapImageView.setScaleY(0.8);
        adornerPane.setScaleX(0.8);
        adornerPane.setScaleY(0.8);

        mapImageView.translateXProperty().setValue(-500);
        mapImageView.translateYProperty().setValue(-500);
        adornerPane.translateXProperty().setValue(-500);
        adornerPane.translateYProperty().setValue(-500);
      } else {
        mapImageView.setScaleX(0.8);
        mapImageView.setScaleY(0.8);
        adornerPane.setScaleX(0.8);
        adornerPane.setScaleY(0.8);

        mapImageView.translateXProperty().setValue(-500);
        mapImageView.translateYProperty().setValue(-500);
        adornerPane.translateXProperty().setValue(-500);
        adornerPane.translateYProperty().setValue(-500);
      }
    } else {
      mapImageView.setScaleX(0.28);
      mapImageView.setScaleY(0.28);
      adornerPane.setScaleX(0.28);
      adornerPane.setScaleY(0.28);

      mapImageView.translateXProperty().setValue(-1010);
      mapImageView.translateYProperty().setValue(-540);
      adornerPane.translateXProperty().setValue(-1010);
      adornerPane.translateYProperty().setValue(-540);
    }

    updateAdornerVisualsOnZoom();
  }

  // endregion

  // region CircleEx and LineEx
  public class CircleEx extends Circle {
    public boolean hasFocus = false;
    public ArrayList<LineEx> connectingEdges = new ArrayList<LineEx>();

    public void updateAdjacentEdges() {
      for (LineEx edge : connectingEdges) {
        if (edge.startNode == this) {
          edge.setStartX(this.getCenterX());
          edge.setStartY(this.getCenterY());
        } else if (edge.endNode == this) {
          edge.setEndX(this.getCenterX());
          edge.setEndY(this.getCenterY());
        } else {
          System.out.println("Circle found edge that didnt have it added");
        }
      }
    }

    public void updateVisuals() {
      if (hasFocus) {
        this.setStrokeWidth(scaledLineWidth);
        this.setStroke(Paint.valueOf("BLUE"));
      } else {
        this.setStrokeWidth(0);
      }
    }

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

  public class LineEx extends javafx.scene.Group {

    public CircleEx startNode;
    public CircleEx endNode;

    private Line mainLine = null;
    private Line halfLine1 = null;
    private Line halfLine2 = null;

    public boolean hasFocus = false;
    public boolean biDirectional = false;
    public boolean direcVisualsEnabled = false;

    private Timeline timeline = null;

    public LineEx(double startX, double startY, double endX, double endY) {
      mainLine = new Line(startX, startY, endX, endY);
      this.getChildren().add(mainLine);
    }

    // region JavaFX Line Methods
    public double getStartX() {
      return mainLine.getStartX();
    }

    public double getStartY() {
      return mainLine.getStartY();
    }

    public double getEndX() {
      return mainLine.getEndX();
    }

    public double getEndY() {
      return mainLine.getEndY();
    }

    public void setStartX(double startX) {
      mainLine.setStartX(startX);
      updateHalfLinePositions();
    }

    public void setStartY(double startY) {
      mainLine.setStartY(startY);
      updateHalfLinePositions();
    }

    public void setEndX(double endX) {
      mainLine.setEndX(endX);
      updateHalfLinePositions();
    }

    public void setEndY(double endY) {
      mainLine.setEndY(endY);
      updateHalfLinePositions();
    }

    public void setStroke(Paint stroke) {
      mainLine.setStroke(stroke);
      updateHalfLinePositions();
    }

    public void setStrokeWidth(double strokeWidth) {
      mainLine.setStrokeWidth(strokeWidth);
      updateHalfLinePositions();
    }
    // endregion

    // region Directionality
    private LinearGradient getGradient() {
      return new LinearGradient(
          this.getStartX(),
          this.getStartY(),
          this.getEndX(),
          this.getEndY(),
          false,
          CycleMethod.REFLECT,
          new Stop(0, Color.RED),
          new Stop(1, Color.GREEN));
    }

    public boolean setDirection(CircleEx newStartNode) {
      if (endNode == newStartNode) {
        flipDirection();

        return true;
      }
      return false;
    }

    public void flipDirection() {
      CircleEx newStartNode = endNode;
      endNode = startNode;
      startNode = newStartNode;

      double startX = this.getStartX();
      double startY = this.getStartY();

      this.setStartX(this.getEndX());
      this.setStartY(this.getEndY());

      this.setEndX(startX);
      this.setEndY(startY);
    }

    private void updateHalfLinePositions() {

      double startX = mainLine.getStartX();
      double startY = mainLine.getStartY();

      double endX = mainLine.getEndX();
      double endY = mainLine.getEndY();

      double width = mainLine.getStrokeWidth();

      double length = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
      double directionX = (endX - startX) / length;
      double directionY = (endY - startY) / length;

      if (halfLine1 != null) {
        halfLine1.setStrokeWidth(width);
        //        halfLine1.setStroke(Color.RED); // getGradient());
        setLineAnimation(halfLine1);

        halfLine1.setStartX(startX + directionY * width);
        halfLine1.setStartY(startY + directionX * width);

        halfLine1.setEndX(endX + directionY * width);
        halfLine1.setEndY(endY + directionX * width);
        if (!this.getChildren().contains(halfLine1)) {
          this.getChildren().add(halfLine1);
        }
        halfLine1.toFront();
      }

      if (halfLine2 != null) {
        halfLine2.setStrokeWidth(width);
        //        halfLine2.setStroke(Color.BLUE); // getGradient());
        setLineAnimation(halfLine2);

        halfLine2.setEndX(startX - directionY * width);
        halfLine2.setEndY(startY - directionX * width);

        halfLine2.setStartX(endX - directionY * width);
        halfLine2.setStartY(endY - directionX * width);
        if (!this.getChildren().contains(halfLine2)) {
          this.getChildren().add(halfLine2);
        }
        halfLine2.toFront();
      }
    }

    public void updateVisuals() {
      if (direcVisualsEnabled && biDirectional) {
        if (halfLine1 == null && halfLine2 == null) {
          halfLine1 = new Line();
          halfLine2 = new Line();
          updateHalfLinePositions();
        } else {
          updateHalfLinePositions();
        }
        mainLine.setVisible(false);
      } else {

        mainLine.setVisible(true);
        // remove biDirectional components if any
        if (this.getChildren().size() > 1) {
          getChildren().remove(halfLine1);
          getChildren().remove(halfLine2);
          halfLine1 = null;
          halfLine2 = null;
        }
        mainLine.setStrokeLineJoin(StrokeLineJoin.ROUND);

        if (direcVisualsEnabled) {
          //          this.setStroke(getGradient());
          setLineAnimation(mainLine);
        } else if (this.hasFocus) {
          this.setStrokeWidth(scaledLineWidthSelected);
          this.setStroke(Paint.valueOf("BLUE"));
          if (timeline != null) {
            timeline.stop();
          }
          mainLine.getStrokeDashArray().clear();
        } else {
          this.setStrokeWidth(scaledLineWidth);
          this.setStroke(Paint.valueOf("BLACK"));
          if (timeline != null) {
            timeline.stop();
          }
          mainLine.getStrokeDashArray().clear();
        }
      }
    }

    /**
     * Sets the animation of the provided line to a rounded green rectangle that moves towards to
     * end node
     *
     * @param line
     */
    private void setLineAnimation(Line line) {
      line.getStrokeDashArray().setAll(20d, 20d, 20d, 20d);
      line.setStrokeLineCap(StrokeLineCap.ROUND);
      //      mainLine.setStrokeWidth(10);
      line.setStroke(Paint.valueOf("GREEN"));
      final double maxOffset = line.getStrokeDashArray().stream().reduce(0d, (a, b) -> a + b);

      timeline =
          new Timeline(
              new KeyFrame(
                  Duration.ZERO,
                  new KeyValue(line.strokeDashOffsetProperty(), maxOffset, Interpolator.LINEAR)),
              new KeyFrame(
                  Duration.seconds(2),
                  new KeyValue(line.strokeDashOffsetProperty(), 0, Interpolator.LINEAR)));
      timeline.setCycleCount(Timeline.INDEFINITE);
      timeline.play();
    }

    public void clearDirectionality() {
      this.direcVisualsEnabled = false;
      this.biDirectional = false;
      updateVisuals();
    }
    // endregion
  }
  // endregion
}
