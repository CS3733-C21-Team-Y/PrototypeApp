package edu.wpi.yellowyetis;

import java.awt.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class nodeEdgeDispController {

  @FXML private Button addNode;
  @FXML private Button addEdge;

  @FXML private TextField newX;
  @FXML private TextField newY;

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
  // -------------------------

  public nodeEdgeDispController() {}

  @FXML
  private void initialize() {
    addNode.setOnAction(e -> createNode(e));
  }

  private void createNode(ActionEvent e) {
    node n = new node(Integer.parseInt(newX.getText()), Integer.parseInt(newY.getText()));
    Circle circle = new Circle(n.nodeX, n.nodeY, 3);
    circle.setFill(Paint.valueOf("RED"));

    pane.getChildren().add(circle);

    Stage stage = (Stage) addNode.getScene().getWindow();

    stage.setScene(addNode.getScene());

    stage.show();

    System.out.println(n.nodeX);
  }
}
