package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class ServiceRequestNavigatorController extends CenterPage {
  @FXML private VBox serviceBox;
  @FXML private JFXButton button2;
  @FXML private JFXButton myRequestsBtn;
  @FXML private JFXButton allRequestsBtn;
  @FXML private JFXButton assignedBtn;

  @FXML
  private void initialize() {

    button2.setOnAction(e -> loadServicesFromDB());
    myRequestsBtn.setOnAction(e -> filterByRequester());
    assignedBtn.setOnAction(e -> filterByEmployee());
    allRequestsBtn.setOnAction(e -> loadServicesFromDB());
  }

  private void filterByRequester() {
    String username = parent.settings.getCurrentUsername();
    serviceBox.getChildren().clear();
    try {
      ArrayList<Service> serviceList = DataOperations.exportService("", "");
      for (Service service : serviceList) {
        if (service.getRequester().equals(username)) addService(service);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  private void filterByEmployee() {
    String username = parent.settings.getCurrentUsername();
    serviceBox.getChildren().clear();
    try {
      ArrayList<Service> serviceList = DataOperations.exportService("", "");
      for (Service service : serviceList) {
        if (service.getEmployee().equals(username)) addService(service);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
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