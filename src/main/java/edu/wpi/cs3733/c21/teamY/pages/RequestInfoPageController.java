package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import edu.wpi.cs3733.c21.teamY.entity.ActiveGraph;
import edu.wpi.cs3733.c21.teamY.entity.Employee;
import edu.wpi.cs3733.c21.teamY.entity.Graph;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RequestInfoPageController<label> extends SubPage {
  @FXML private Label title;
  @FXML private VBox infoBox;
  @FXML private VBox leftBox;
  @FXML private VBox centerBox;
  @FXML private VBox rightBox;
  @FXML private JFXButton submitBtn;
  @FXML private JFXComboBox employeeComboBox;
  @FXML private JFXButton backBtn;
  // @FXML private AnchorPane annoyingVbox;
  Scene scene;

  // private boolean desktop;

  private Service service;

  JFXTextField leftArea;
  JFXTextField centerArea;
  JFXButton saveBtn;

  @FXML
  private void initialize() {
    Platform.runLater(() -> loadInformation());
    leftArea = new JFXTextField();
    centerArea = new JFXTextField();
    saveBtn = new JFXButton();
    saveBtn.setOnAction(e -> buttonClicked(e));
    submitBtn.setOnAction(e -> submitEmployee());
    backBtn.setOnAction(e -> back());
    // desktop = parent.isDesktop;

    try {
      ArrayList<Employee> employeeList = DataOperations.getStaffList();
      for (Employee employee : employeeList) {
        employeeComboBox.getItems().add(employee.getEmployeeID());
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  private void submitEmployee() {
    System.out.println((String) employeeComboBox.getValue());
    service.setEmployee((String) employeeComboBox.getValue());
    parent.loadRightSubPage("RequestInfoPage.fxml");

    try {
      DataOperations.updateServiceAssignedEmployee(service, service.getEmployee());
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    parent.loadCenterSubPage("ServiceRequestNavigator.fxml");
  }

  private void loadInformation() {
    service = Settings.getSettings().getCurrentDisplayedService();

    createInfoBox("Type: ", service.getType());
    if (service.getDescription().length() > 0)
      createInfoBox("Description: ", service.getDescription());
    if (service.getLocation().length() > 0) {
      ServiceRequestInfoElementController locationController =
          createInfoBox("Location: ", service.getLocation());
      configSeeLocationButton(locationController);
    }
    if (service.getCategory().length() > 0) createInfoBox("Category: ", service.getCategory());
    if (service.getUrgency().length() > 0) createInfoBox("Urgency: ", service.getUrgency());
    if (service.getDate().length() > 0) createInfoBox("Date: ", service.getDate());
    if (service.getRequester().length() > 0) createInfoBox("Requester: ", service.getRequester());
    if (service.getEmployee().length() > 0) {
      createInfoBox("Employee Assigned: ", service.getEmployee());
      employeeComboBox.setValue(service.getEmployee());
    }
    if (service.getAdditionalInfo().length() > 0)
      createInfoBox("Additional Info: ", service.getAdditionalInfo());

    if (service.getType().equals("Laundry")) {

      saveBtn.setText("Save");
      saveBtn.setPrefWidth(9999);
      saveBtn.setFont(new Font("Calibri", 15));
      saveBtn.setStyle("-fx-background-color: #efeff9; ");

      leftArea.setPromptText("Loads of laundry done:");
      centerArea.setPromptText("Number of garments discarded:");
      leftArea.setLabelFloat(true);
      centerArea.setLabelFloat(true);
      leftArea.setFocusColor(Color.web("#5a5c94"));

      leftBox.getChildren().add(leftArea);
      centerBox.getChildren().add(centerArea);
      rightBox.getChildren().add(saveBtn);
    }
  }

  private void back() {
    parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
  }

  private void buttonClicked(ActionEvent e) {

    if (!leftArea.getText().equals("") && !leftArea.getText().equals("")) {
      // # of loads
      String numLoads =
          "# loads done: " + leftArea.getText() + "  # discarded garments: " + centerArea.getText();

      StageInformation info = (StageInformation) title.getScene().getWindow().getUserData();
      Service service = info.getService();
      service.setAdditionalInfo(numLoads);
      try {
        DataOperations.updateServiceAdditionalInfoOnly(service.getServiceID(), numLoads);
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }
      parent.loadRightSubPage("RequestInfoPage.fxml");
    }
  }

  @Override
  public void loadNavigationBar() {
    parent.setCenterColumnWidth(350);
  }

  private ServiceRequestInfoElementController createInfoBox(String title, String data) {
    if (parent.isDesktop) {
      infoBox.setPadding(new Insets(2, 5, 4, 50));
    } else {
      infoBox.setPadding(new Insets(2, 100, 4, 50));
    }
    scene = infoBox.getScene();
    FXMLLoader fxmlLoader = new FXMLLoader();
    ServiceRequestInfoElementController controller = null;
    try {
      Node node =
          fxmlLoader.load(getClass().getResource("ServiceRequestInfoElement.fxml").openStream());
      controller = (ServiceRequestInfoElementController) fxmlLoader.getController();
      controller.populateInformation(title, data, parent.isDesktop);
      infoBox.getChildren().add(node);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return controller;
  }

  private void configSeeLocationButton(ServiceRequestInfoElementController locationController) {
    HBox dataHBox = locationController.getDataHBox();
    Button viewLocationButtton = new Button();
    viewLocationButtton.setText("View Location");
    viewLocationButtton.setOnAction(
        e -> {
          MapController mapInsertController = null;
          FXMLLoader fxmlLoader = new FXMLLoader();
          try {
            Node node = fxmlLoader.load(getClass().getResource("MapUserControl.fxml").openStream());
            mapInsertController = (MapController) fxmlLoader.getController();
            mapInsertController.setParent(parent);
            mapInsertController.setAdminPage(false);
            // call method before page load
          } catch (IOException exception) {
            exception.printStackTrace();
          }
          if (mapInsertController == null) {
            return;
          }

          final Stage dialog = new Stage();
          dialog.initModality(Modality.APPLICATION_MODAL);
          dialog.initOwner(scene.getWindow());
          Scene dialogScene =
              new Scene(
                  mapInsertController.getAnchorPane(),
                  scene.getWindow().getWidth() / 2,
                  scene.getWindow().getHeight() / 2);
          dialog.setScene(dialogScene);

          // popupAnchor.setClip(popupAnchor);

          Graph graph = null;
          try {
            graph = ActiveGraph.getActiveGraph();
          } catch (Exception exception) {
          }
          if (graph == null) {
            return;
          }

          String longname = locationController.getData().getText();
          edu.wpi.cs3733.c21.teamY.entity.Node node = graph.longNodes.get(longname);
          if (node == null) {
            System.out.println("Node not found " + longname);
            return;
          }
          Integer floor = null;
          try {
            floor = Integer.parseInt(node.floor);
          } catch (Exception ee) {
            ee.printStackTrace();
          }

          if (floor == null) {
            System.out.println("Floor could not be found");
            return;
          }
          mapInsertController.changeMapImage(mapInsertController.getMapOrder().get(floor));
          MapController.CircleEx nodeCircle = mapInsertController.addNodeCircle(node);
          if (nodeCircle == null) {
            System.out.println("OOF");
          }
          mapInsertController.selectCircle(nodeCircle);
          mapInsertController.hideFloorMenu();
          dialog.show();
        });
    dataHBox.getChildren().add(viewLocationButtton);
  }
}
