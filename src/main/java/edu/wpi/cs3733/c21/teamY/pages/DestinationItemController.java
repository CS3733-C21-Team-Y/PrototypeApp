package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c21.teamY.dataops.FuzzySearchComboBoxListener;
import edu.wpi.cs3733.c21.teamY.entity.Node;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

public class DestinationItemController {
  @FXML private JFXComboBox destinationCB;
  @FXML private HBox destinationRootHBox;

  public int index = 0;

  public JFXButton getDownBtn() {
    return downBtn;
  }

  public JFXButton getUpBtn() {
    return upBtn;
  }

  public JFXButton getRemoveBtn() {
    return removeBtn;
  }

  @FXML private JFXButton downBtn;
  @FXML private JFXButton upBtn;
  @FXML private JFXButton removeBtn;

  private FuzzySearchComboBoxListener locationFuzzy;

  public ComboBox getDestinationCB() {
    return destinationCB;
  }

  public HBox getDestinationRootHBox() {
    return destinationRootHBox;
  }

  public void populateComboBox(ArrayList<Node> nodes) {
    destinationCB.getItems().remove(0, destinationCB.getItems().size());
    for (Node node : nodes) {
      String name = node.longName;
      String type = node.nodeType;
      // Filtering out the unwanted midway points
      if (!type.equals("WALK")
          && !type.equals("ELEV")
          && !type.equals("HALL")
          && !type.equals("STAI")) {
        destinationCB.getItems().add(name);
      }
    }
    locationFuzzy = new FuzzySearchComboBoxListener(destinationCB);
  }
}
