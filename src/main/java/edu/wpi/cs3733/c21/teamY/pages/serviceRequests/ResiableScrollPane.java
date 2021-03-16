package edu.wpi.cs3733.c21.teamY.pages.serviceRequests;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;

public class ResiableScrollPane extends BorderPane {
  BorderPane borderPane;

  public BorderPane getBorderPane() {
    return borderPane;
  }

  ResiableScrollPane() throws IOException {
    borderPane = FXMLLoader.load(getClass().getResource("covid.fxml"));
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setContent(borderPane);
    this.setOnZoom(
        event -> {
          System.out.println("zoom");
          borderPane.setPrefWidth(scrollPane.getPrefWidth());
        });

    this.setPrefSize(600, 900);
    super.setCenter(scrollPane);
  }
}
