package edu.wpi.cs3733.c21.teamY.pages;

import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class ServiceRequestNavigatorController extends CenterPage {
  @FXML private VBox serviceBox;
  @FXML private Button button2;

  @FXML
  private void initialize() {
    button2.setOnAction(e -> loadServicesFromDB());
  }

  private void loadServicesFromDB() {
    serviceBox.getChildren().clear();
    try {
      ArrayList<Service> serviceList = DataOperations.exportService("", "");
      for (Service service : serviceList) {
        addService(service);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  private void addService(Service service) {
    FXMLLoader fxmlLoader = new FXMLLoader();
    try {
      Node node =
          fxmlLoader.load(getClass().getResource("ServiceRequestElement.fxml").openStream());
      ServiceRequestElementController controller =
          (ServiceRequestElementController) fxmlLoader.getController();
      controller.setParent(parent);
      controller.populateInformation(service);
      serviceBox.getChildren().add(node);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
