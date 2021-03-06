package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class LandingPageController extends RightPage implements Initializable {

  @FXML ImageView imageView;
  @FXML JFXButton button;

  int counter = 0;

  public void slideshow() {
    ArrayList<Image> images = new ArrayList<>();

    images.add(new Image("/edu/wpi/cs3733/c21/teamY/images/doctorPic1.jpg"));
    images.add(new Image("/edu/wpi/cs3733/c21/teamY/images/doctorPic2.jpg"));
    images.add(new Image("/edu/wpi/cs3733/c21/teamY/images/doctorPic3.jpg"));
    images.add(new Image("/edu/wpi/cs3733/c21/teamY/images/doctorPic4.jpg"));
    images.add(new Image("/edu/wpi/cs3733/c21/teamY/images/doctorPic5.jpeg"));

    Timeline timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(4.0),
                event -> {
                  imageView.setImage(images.get(counter));
                  counter++;
                  if (counter == images.size()) counter = 0;
                }));

    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }

  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == button) parent.loadRightSubPage("LoginPage.fxml");
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    button.setOnAction(event -> buttonClicked(event));
    slideshow();
  }
}
