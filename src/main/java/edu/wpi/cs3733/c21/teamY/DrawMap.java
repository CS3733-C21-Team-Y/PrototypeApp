package edu.wpi.cs3733.c21.teamY;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class DrawMap {

  private StackPane stackpane;
  private Pane pane;
  private ImageView map;

  protected enum MAP_PAGE {
    PARKING,
    FLOOR1,
    FLOOR2,
    FLOOR3,
    FLOOR4,
    FLOOR5
  };

  private MAP_PAGE mp = MAP_PAGE.PARKING;

  public DrawMap(StackPane stackPane, Pane pane, ImageView map) {
    this.stackpane = stackPane;
    this.pane = pane;
    this.map = map;
  }

  protected Image setImage(MAP_PAGE mp) {
    Image parking = new Image("edu/wpi/cs3733/c21/teamY/FaulknerCampus.png");
    Image f1 = new Image("edu/wpi/cs3733/c21/teamY/FaulknerFloor1_Updated.png");
    Image f2 = new Image("edu/wpi/cs3733/c21/teamY/FaulknerFloor2_Updated.png");
    Image f3 = new Image("edu/wpi/cs3733/c21/teamY/FaulknerFloor3_Updated.png");
    Image f4 = new Image("edu/wpi/cs3733/c21/teamY/FaulknerFloor4_Updated.png");
    Image f5 = new Image("edu/wpi/cs3733/c21/teamY/FaulknerFloor5_Updated.png");

    switch (mp) {
      case FLOOR1:
        map.setImage(f1);
        return f1;
      case FLOOR2:
        return f2;
      case FLOOR3:
        return f3;
      case FLOOR4:
        return f4;
      case FLOOR5:
        return f5;
      case PARKING:
      default:
        return parking;
    }
  }
}
