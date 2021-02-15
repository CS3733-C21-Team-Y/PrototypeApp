package edu.wpi.yellowyetis;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class nodeEdgeDispController {

  @FXML private Button addNode;
  @FXML private Button addEdge;

  @FXML private CheckBox addNodecb;
  @FXML private CheckBox addEdgecb;

  @FXML private Button deleteNode;
  @FXML private Button deleteEdge;

  // variables for selecting points and locating edges
  private boolean startEdgeFlag = true;
  private int sx, sy, ex, ey;
  private Circle currentSelectedCircle = new Circle(0, 0, 0);
  private Line currentSelectedLine = new Line(0, 0, 0, 0);

  @FXML private TextField newX;
  @FXML private TextField newY;

  @FXML private TextField startX;
  @FXML private TextField startY;
  @FXML private TextField endX;
  @FXML private TextField endY;

  @FXML private Pane pane;
  @FXML private ImageView map;
  @FXML private StackPane stackPane;

  // -----test--------should be from db classes
  class node {
    private int nodeX;
    private int nodeY;

    node(int x, int y) {
      this.nodeX = x;
      this.nodeY = y;
    }
  }

  class edge {
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    edge(int startX, int startY, int endX, int endY) {
      this.startX = startX;
      this.startY = startY;
      this.endX = endX;
      this.endY = endY;
    }
  }
  // -------------------------

  public nodeEdgeDispController() {}

  @FXML
  private void initialize() {
    // set pane to size of image
    stackPane.setMaxWidth(map.getFitWidth());
    stackPane.setMaxHeight(map.getFitHeight());

    // run the create methods on the button click
    addNode.setOnAction(e -> createNode(e));
    addEdge.setOnAction(e -> createEdge(e));

    deleteNode.setOnAction(e -> removeNode(e));
    deleteEdge.setOnAction(e -> removeEdge(e));

    // create node or edge when pane is clicked
    pane.setOnMouseClicked(
        e -> {
          getPaneNode();

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
                System.out.println(currentSelectedLine);
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

  private void createEdgecb(MouseEvent e) {
    // creates an edge between two selected points when the checkbox is selected
    if (addEdgecb.isSelected()) {
      if (startEdgeFlag) { // decides if its te starting or ending point being selected
        highlightCircle();
        startEdgeFlag = !startEdgeFlag;
        try {
          sx = (int) currentSelectedCircle.getCenterX();
          sy = (int) currentSelectedCircle.getCenterY();
        } catch (Exception exception) {
          System.out.println("no start point");
        }
      } else {
        startEdgeFlag = !startEdgeFlag;
        try {
          ex = (int) currentSelectedCircle.getCenterX();
          ey = (int) currentSelectedCircle.getCenterY();
        } catch (Exception exception) {
          System.out.println("no end point");
        }

        // createing the line and adding as a child to the pane
        edge ed = new edge(sx, sy, ex, ey);
        Line line = new Line(ed.startX, ed.startY, ed.endX, ed.endY);
        line.setStrokeWidth(3);
        pane.getChildren().add(line);
        line.toBack();
        // refreshing and adding to the scene
        Stage stage = (Stage) addEdge.getScene().getWindow();
        stage.setScene(addEdge.getScene());
        stage.show();
      }
    }
  }

  private void createNodecb(MouseEvent e) {
    // when the add node checkbox is selected, the new nodes can be created
    // wherever the mouse clicks withing the scene
    if (addNodecb.isSelected()) {
      node n = new node((int) e.getSceneX(), (int) e.getSceneY());
      Circle circle = new Circle(n.nodeX, n.nodeY, 5);
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

  private void createEdge(ActionEvent e) {
    // creates a new instance of the local edge class and
    // sets the start and end values to that in the text fields
    edge ed =
        new edge(
            Integer.parseInt(startX.getText()),
            Integer.parseInt(startY.getText()),
            Integer.parseInt(endX.getText()),
            Integer.parseInt(endY.getText()));
    // creating a new line Node and adding it as a child to the pane
    Line line = new Line(ed.startX, ed.startY, ed.endX, ed.endY);
    line.setStrokeWidth(3);
    pane.getChildren().add(line);

    // refreshing and adding to scene
    Stage stage = (Stage) addEdge.getScene().getWindow();
    stage.setScene(addEdge.getScene());
    stage.show();
  }

  private void createNode(ActionEvent e) {
    // creates a new instance of the local node class and creates a red circle
    // to add as a child of the pane in the scene
    node n = new node(Integer.parseInt(newX.getText()), Integer.parseInt(newY.getText()));
    Circle circle = new Circle(n.nodeX, n.nodeY, 5);
    currentSelectedCircle = circle;
    circle.setFill(Paint.valueOf("RED"));
    pane.getChildren().add(circle);

    // adding the node and refreshing the scene
    Stage stage = (Stage) addNode.getScene().getWindow();
    stage.setScene(addNode.getScene());
    stage.show();
  }
}
