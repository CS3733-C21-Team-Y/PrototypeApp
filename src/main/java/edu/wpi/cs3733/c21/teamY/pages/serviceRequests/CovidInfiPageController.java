package edu.wpi.cs3733.c21.teamY.pages.serviceRequests;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CovidInfiPageController extends Application {
  Stage stage = new Stage();

  @Override
  public void start(Stage primaryStage) throws Exception {
    ResiableScrollPane root = new ResiableScrollPane();
    root.setOnZoom(
        e -> {
          System.out.println("jhhh");
        });
    Scene scene = new Scene(root);
    final ChangeListener<Number> listener =
        new ChangeListener<Number>() {
          final Timer timer = new Timer(); // uses a timer to call your resize method
          TimerTask task = null; // task to execute after defined delay
          final long delayTime =
              10; // delay that has to pass in order to consider an operation done

          @Override
          public void changed(
              ObservableValue<? extends Number> observable,
              Number oldValue,
              final Number newValue) {
            if (task
                != null) { // there was already a task scheduled from the previous operation ...
              task.cancel(); // cancel it, we have a new size to consider
            }

            task =
                new TimerTask() // create new task that calls your resize operation
                {
                  @Override
                  public void run() {
                    // here you can place your resize code
                    //                        System.out.println("resize to " +
                    // primaryStage.getWidth() + " " + primaryStage.getHeight());
                    root.getBorderPane().setPrefWidth(primaryStage.getWidth());
                  }
                };
            // schedule new task
            timer.schedule(task, delayTime);
          }
        };

    // finally we have to register the listener
    primaryStage.widthProperty().addListener(listener);
    primaryStage.heightProperty().addListener(listener);
    primaryStage.setTitle("Covid Info");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public void showMain() throws Exception {
    start(stage);
  }
}
