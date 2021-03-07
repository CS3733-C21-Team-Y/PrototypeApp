package edu.wpi.cs3733.c21.teamY.pages;

import edu.wpi.cs3733.c21.teamY.dataops.JDBCUtils;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class RequestsManagerController extends GenericPage {

  @FXML private Button backBtn;
  @FXML private Button exitBtn;
  @FXML private CheckBox reqCheckbox;
  @FXML private RowConstraints firstRow;
  @FXML private GridPane serviceGrid;
  @FXML private Button addServiceBtn;

  private int rowCount;
  private HashMap services;

  public RequestsManagerController() {
    super();
  }

  @FXML
  private void initialize() {
    backBtn.setOnAction(e -> buttonClicked(e));
    addServiceBtn.setOnAction(e -> loadServicesFromDB());
    // addServiceBtn.setOnAction(
    //     e -> addServiceToGrid(new Service(rowCount, "Laundry", "", "Cafeteria", "", "", "")));
    rowCount = 0;
    RowConstraints constraints = new RowConstraints();
    constraints.setMinHeight(50);
    serviceGrid.getRowConstraints().add(constraints);
    Platform.runLater(() -> loadServicesFromDB());
  }

  private void loadServicesFromDB() {

    try {
      ArrayList<Service> serviceList = JDBCUtils.exportService("", "");
      for (Service service : serviceList) {
        addServiceToGrid(service);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  private void addServiceToGrid(Service service) {
    rowCount++;
    // services.put(service.getServiceID(), service);
    Label serviceType = new Label(service.getType());
    Label serviceLocation = new Label(service.getLocation());
    Label serviceID = new Label(("" + service.getServiceID()));
    Label status = new Label();
    if (service.getStatus() == 1) {
      status.setText("Complete");
      status.setId("completeService");
    } else if (service.getStatus() == 0) {
      status.setText("In Progress");
      status.setId("incompleteService");
    } else {
      status.setText("Not Started");
      status.setId("notStartedService");
    }
    status.setPrefWidth(80);
    status.setAlignment(Pos.CENTER);
    status.setPadding(new Insets(5, 5, 5, 5));
    CheckBox checkBox = new CheckBox();
    Button button = new Button("View");
    button.setOnAction(e -> serviceClicked(service));
    // serviceGrid.addColumn(rowCount);
    serviceGrid.addRow(rowCount);
    serviceGrid.add(serviceType, 0, rowCount);
    serviceGrid.add(serviceLocation, 1, rowCount);
    serviceGrid.add(serviceID, 2, rowCount);
    serviceGrid.add(status, 3, rowCount);
    serviceGrid.add(checkBox, 4, rowCount);
    serviceGrid.add(button, 5, rowCount);

    RowConstraints constraints = new RowConstraints();
    constraints.setMinHeight(35);
    serviceGrid.getRowConstraints().add(constraints);
  }

  @FXML
  private void serviceClicked(Service service) {
    // error handling for FXMLLoader.load
    // System.out.println("hello world");

    try {
      // initializing stage
      Stage stage = null;

      // gets the current stage
      stage = (Stage) backBtn.getScene().getWindow();

      // sets the new scene to the alex page
      stage.setUserData(service);
      Parent root = FXMLLoader.load(getClass().getResource("ServiceRequestInfoPage.fxml"));
      Scene newScene = new Scene(root);

      stage.setScene(newScene);

      // display new stage

      stage.show();
    } catch (Exception exp) {
    }
  }

  @FXML
  private void buttonClicked(ActionEvent e) {
    // error handling for FXMLLoader.load
    try {
      // initializing stage
      Stage stage = null;

      if (e.getSource() == backBtn) {
        // gets the current stage
        stage = (Stage) backBtn.getScene().getWindow();
        // sets the new scene to the alex page
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("HomePage.fxml"))));
      } else {

      }

      // display new stage
      stage.show();
    } catch (Exception exp) {
    }
  }
}
