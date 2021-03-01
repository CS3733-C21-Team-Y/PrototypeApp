package edu.wpi.cs3733.c21.teamY.pages;

import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class RequestInfoPageController<label> extends RightPage {
  @FXML private Label title;
  @FXML private VBox infoBox;

  @FXML
  private void initialize() {
    Platform.runLater(() -> loadInformation());
  }

  private void loadInformation() {
    StageInformation info = (StageInformation) title.getScene().getWindow().getUserData();
    Service service = info.getService();

    createInfoBox("Type: ", service.getType());
    if (service.getDescription().length() > 0)
      createInfoBox("Description: ", service.getDescription());
    if (service.getLocation().length() > 0) createInfoBox("Location: ", service.getLocation());
    if (service.getCategory().length() > 0) createInfoBox("Category: ", service.getCategory());
    if (service.getUrgency().length() > 0) createInfoBox("Urgency: ", service.getUrgency());
    if (service.getDate().length() > 0) createInfoBox("Date: ", service.getDate());
    if (service.getRequester().length() > 0) createInfoBox("Requester: ", service.getRequester());
    if (service.getAdditionalInfo().length() > 0)
      createInfoBox("Additional Info: ", service.getAdditionalInfo());
  }

  @Override
  public void loadNavigationBar() {
    parent.setCenterColumnWidth(350);
  }

  private void createInfoBox(String title, String data) {
    FXMLLoader fxmlLoader = new FXMLLoader();
    try {
      Node node =
          fxmlLoader.load(getClass().getResource("ServiceRequestInfoElement.fxml").openStream());
      ServiceRequestInfoElementController controller =
          (ServiceRequestInfoElementController) fxmlLoader.getController();
      controller.populateInformation(title, data);
      infoBox.getChildren().add(node);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
