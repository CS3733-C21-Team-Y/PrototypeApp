package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class LandingPageController extends SubPage implements Initializable {

  @FXML ImageView imageView;
  @FXML JFXButton loginBtn;
  @FXML JFXButton guestBtn;

  Scene scene;

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
    if (e.getSource() == loginBtn) parent.loadRightSubPage("LoginPage.fxml");
    if (e.getSource() == guestBtn) {
      parent.loadRightSubPage("CovidScreening.fxml");
      Settings.getSettings().loginSuccess("guest", 0);
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    loginBtn.setOnAction(event -> buttonClicked(event));
    loginBtn.setCursor(Cursor.HAND);
    guestBtn.setOnAction(event -> buttonClicked(event));
    guestBtn.setCursor(Cursor.HAND);
    slideshow();

    Platform.runLater(
        () -> {
          scene = imageView.getScene();
          imageView.setFitWidth(.45 * scene.getWidth());
          imageView.setFitHeight(.3 * scene.getWidth());
          scene
              .widthProperty()
              .addListener(
                  new ChangeListener<Number>() {
                    @Override
                    public void changed(
                        ObservableValue<? extends Number> observable,
                        Number oldValue,
                        Number newValue) {
                      imageView.setFitHeight(.6 * (double) newValue);
                    }
                  });
          scene
              .heightProperty()
              .addListener(
                  new ChangeListener<Number>() {
                    @Override
                    public void changed(
                        ObservableValue<? extends Number> observable,
                        Number oldValue,
                        Number newValue) {
                      imageView.setFitWidth(.9 * (double) newValue);
                    }
                  });
        });
  }
}
