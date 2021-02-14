package edu.wpi.yellowyetis;

import java.awt.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class nodeEdgeDispController {

  @FXML private Button addNode;
  @FXML private Button addEdge;

  @FXML private CheckBox addNodecb;
  @FXML private CheckBox addEdgecb;
  private boolean startEdgeFlag = true;
  private int sx, sy, ex, ey;

  @FXML private TextField newX;
  @FXML private TextField newY;

  @FXML private TextField startX;
  @FXML private TextField startY;
  @FXML private TextField endX;
  @FXML private TextField endY;

  @FXML private Pane pane;
  @FXML private ImageView map;

  // -----test--------
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
    addNode.setOnAction(e -> createNode(e));
    addEdge.setOnAction(e -> createEdge(e));
    pane.setOnMouseClicked(
        e -> {
          if (addEdgecb.isSelected()) {
            createEdgecb(e);
          } else if (addNodecb.isSelected()) {
            createNodecb(e);
          }
        });

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

  private void createEdgecb(MouseEvent e) {

    if (addEdgecb.isSelected()) {
      if (startEdgeFlag) {
        startEdgeFlag = !startEdgeFlag;

        sx = (int) e.getSceneX();
        sy = (int) e.getSceneY();

      } else {
        startEdgeFlag = !startEdgeFlag;
        ex = (int) e.getSceneX();
        ey = (int) e.getSceneY();
        edge ed = new edge(sx, sy, ex, ey);
        Line line = new Line(ed.startX, ed.startY, ed.endX, ed.endY);
        line.setFill(Paint.valueOf("BLUE"));
        pane.getChildren().add(line);

        Stage stage = (Stage) addEdge.getScene().getWindow();
        stage.setScene(addEdge.getScene());
        stage.show();
      }
    }
  }

  private void createNodecb(MouseEvent e) {
    if (addNodecb.isSelected()) {
      node n = new node((int) e.getSceneX(), (int) e.getSceneY());
      Circle circle = new Circle(n.nodeX, n.nodeY, 3);
      circle.setFill(Paint.valueOf("RED"));
      pane.getChildren().add(circle);

      Stage stage = (Stage) addNode.getScene().getWindow();
      stage.setScene(addNode.getScene());
      stage.show();
      System.out.println(n.nodeX + "," + n.nodeY);
    }
  }

  private void createEdge(ActionEvent e) {
    edge ed =
        new edge(
            Integer.parseInt(startX.getText()),
            Integer.parseInt(startY.getText()),
            Integer.parseInt(endX.getText()),
            Integer.parseInt(endY.getText()));
    Line line = new Line(ed.startX, ed.startY, ed.endX, ed.endY);
    line.setFill(Paint.valueOf("BLUE"));
    pane.getChildren().add(line);

    Stage stage = (Stage) addEdge.getScene().getWindow();
    stage.setScene(addEdge.getScene());
    stage.show();
  }

  private void createNode(ActionEvent e) {
    node n = new node(Integer.parseInt(newX.getText()), Integer.parseInt(newY.getText()));
    Circle circle = new Circle(n.nodeX, n.nodeY, 3);
    circle.setFill(Paint.valueOf("RED"));
    pane.getChildren().add(circle);

    Stage stage = (Stage) addNode.getScene().getWindow();
    stage.setScene(addNode.getScene());
    stage.show();
  }
}
