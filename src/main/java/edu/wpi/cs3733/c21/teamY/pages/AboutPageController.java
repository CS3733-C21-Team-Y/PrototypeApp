package edu.wpi.cs3733.c21.teamY.pages;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class AboutPageController extends SubPage {
  @FXML ImageView imageView;
  @FXML Label label;
  private Scene scene;
  private int counter = 0;

  private void slideshow() {
    ArrayList<Image> images = new ArrayList<>();

    images.add(new Image("/edu/wpi/cs3733/c21/teamY/images/Lia.PNG"));
    images.add(new Image("/edu/wpi/cs3733/c21/teamY/images/Brian.PNG"));
    images.add(new Image("/edu/wpi/cs3733/c21/teamY/images/Cole.PNG"));
    images.add(new Image("/edu/wpi/cs3733/c21/teamY/images/Samantha.PNG"));
    images.add(new Image("/edu/wpi/cs3733/c21/teamY/images/Manjusha.PNG"));
    images.add(new Image("/edu/wpi/cs3733/c21/teamY/images/Corinne.PNG"));
    images.add(new Image("/edu/wpi/cs3733/c21/teamY/images/Yonghua.PNG"));
    images.add(new Image("/edu/wpi/cs3733/c21/teamY/images/Alex.PNG"));
    images.add(new Image("/edu/wpi/cs3733/c21/teamY/images/Ian.PNG"));
    images.add(new Image("/edu/wpi/cs3733/c21/teamY/images/Greg.PNG"));
    images.add(new Image("/edu/wpi/cs3733/c21/teamY/images/Pat.PNG"));

    List<String> nameList = new ArrayList<>();
    nameList.add("Pat Lee: Lead Engineer");
    nameList.add("Lia Davis: Scrum Master");
    nameList.add("Brian Boxell: Assistant Lead Engineer");
    nameList.add("Cole Varney: Assistant Lead Engineer");
    nameList.add("Samantha Braun: Project Manager");
    nameList.add("Manjusha Chava: Product Owner");
    nameList.add("Corinne Hartman: Documentation Analyst");
    nameList.add("Yonghua Wang: Database Engineer");
    nameList.add("Alex Corey: UI/UX Engineer");
    nameList.add("Ian Beazley: Algorithm Engineer");
    nameList.add("Greg Klimov: UI/UX Engineer");
    nameList.add("Pat Lee: Lead Engineer");

    Timeline timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(2.0),
                event -> {
                  imageView.setImage(images.get(counter));
                  counter++;
                  label.setText(nameList.get(counter));
                  if (counter == images.size()) counter = 0;
                }));

    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }

  @FXML
  void initialize() {
    slideshow();

    Platform.runLater(
        () -> {
          scene = imageView.getScene();
          imageView.setFitWidth(.6 * 300);
          imageView.setFitHeight(.6 * 300);
          scene
              .widthProperty()
              .addListener(
                  new ChangeListener<Number>() {
                    @Override
                    public void changed(
                        ObservableValue<? extends Number> observable,
                        Number oldValue,
                        Number newValue) {
                      imageView.setFitHeight(.6 * 300);
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
                      imageView.setFitWidth(.6 * 300);
                    }
                  });
        });
  }
}
