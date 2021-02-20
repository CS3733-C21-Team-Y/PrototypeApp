package edu.wpi.teamY;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomMapController extends VBox {
  @FXML private Pane basePane;
  @FXML private Button backButton;
  @FXML private Button clearButton;

  @FXML private Canvas mapSurface;
  @FXML private ImageView mapImage;
  @FXML private Group mapScaleGroup;

  @FXML private ComboBox startNode;
  @FXML private ComboBox endNode;

  Graph graph;

  public CustomMapController() {}

  private double screenRatio;
  private double offset;

  @FXML
  public void initialize() {
    backButton.setOnAction(e -> buttonClicked(e));
    clearButton.setOnAction(e -> buttonClicked(e));

    this.graph = ActiveGraph.getActiveGraph();
    // mapScaleGroup.setScaleX(0.5);
    // mapScaleGroup.setScaleY(0.5);

    screenRatio = 992 / 11; // mapSurface.getWidth() / mapImage.getImage().getWidth();
    offset = 1 * screenRatio;
    for (Node node : graph.nodeList) {
      startNode.getItems().add(node.nodeID);
      endNode.getItems().add(node.nodeID);
    }
    redrawLabels();
  }

  public void redrawLabels() {
    GraphicsContext g = mapSurface.getGraphicsContext2D();

    g.setStroke(javafx.scene.paint.Color.color(0, 0, 0));

    for (Node node : graph.nodeList) {

      g.fillOval(
          (int) (node.xcoord * screenRatio + offset / 1.7),
          (int) (node.ycoord * screenRatio + offset),
          5,
          5);

      g.fillText(
          node.nodeID,
          (int) (node.xcoord * screenRatio + offset / 1.7),
          (int) (node.ycoord * screenRatio + offset));
    }
  }

  @FXML
  public void calculatePath() {
    if (startNode.getValue() != null && endNode.getValue() != null) {
      ArrayList<Node> nodes =
          AStarAlgorithm.aStar(graph, (String) startNode.getValue(), (String) endNode.getValue());

      System.out.println(startNode.getValue() + " " + endNode.getValue());

      if (nodes != null) {

        GraphicsContext g = mapSurface.getGraphicsContext2D();
        clearMap(g);

        redrawLabels();

        g.setStroke(javafx.scene.paint.Color.color(0, 0, 1));
        g.beginPath();
        for (int i = 0; i < nodes.size() - 1; i++) {

          Node node1 = nodes.get(i);
          Node node2 = nodes.get(i + 1);

          System.out.println(node1.nodeID);
          g.setLineWidth(2.0);
          g.moveTo(
              (int) (node1.xcoord * screenRatio + offset / 1.7),
              (int) (node1.ycoord * screenRatio + offset));
          g.lineTo(
              (int) (node2.xcoord * screenRatio + offset / 1.7),
              (int) (node2.ycoord * screenRatio + offset));
        }
        g.stroke();
      }
    }
  }

  private void clearMap(GraphicsContext g) {
    g.clearRect(0, 0, mapSurface.getWidth(), mapSurface.getHeight());
  }

  // button event handler
  @FXML
  private void buttonClicked(ActionEvent e) {
    // error handling for FXMLLoader.load
    try {
      // initializing stage
      Stage stage = null;

      if (e.getSource() == backButton) {
        // gets the current stage
        stage = (Stage) backButton.getScene().getWindow();
        // sets the new scene to the alex page
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("HomePage.fxml"))));

      } else if (e.getSource() == clearButton) {
        GraphicsContext g = mapSurface.getGraphicsContext2D();
        clearMap(g);
        redrawLabels();
      } else {

      }

      // display new stage
      stage.show();
    } catch (Exception exp) {
    }
  }
}
