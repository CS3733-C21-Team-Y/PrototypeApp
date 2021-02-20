package edu.wpi.cs3733.c21.teamY;

import java.io.File;
import java.io.FileInputStream;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class DrawMap {

  Image parking = new Image("edu/wpi/cs3733/c21/teamY/FaulknerCampus.png");
  Image f1 = new Image("edu/wpi/cs3733/c21/teamY/FaulknerFloor1_Updated.png");
  Image f2 = new Image("edu/wpi/cs3733/c21/teamY/FaulknerFloor2_Updated.png");
  Image f3 = new Image("edu/wpi/cs3733/c21/teamY/FaulknerFloor3_Updated.png");
  Image f4 = new Image("edu/wpi/cs3733/c21/teamY/FaulknerFloor4_Updated.png");
  Image f5 = new Image("edu/wpi/cs3733/c21/teamY/FaulknerFloor5_Updated.png");

  private StackPane stackpane;
  private Pane pane;
  private ImageView map;

  private FileChooser fc = new FileChooser();
  private File file;

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

  protected Image changeImage(MAP_PAGE floor) {
    switch (floor) {
      case FLOOR1:
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

  public void setImage(Image image, MAP_PAGE floor) {
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
}
