package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;

public class ServiceRequestNavigatorController extends SubPage {

  public ScrollPane scrollPane;
  @FXML private VBox serviceBox;
  @FXML private JFXButton button2;
  @FXML private ToggleButton myRequestsBtn;
  @FXML private ToggleButton allRequestsBtn;
  @FXML private ToggleButton assignedBtn;
  @FXML private JFXButton backBtn;

  // tooltip instantiations
  Tooltip button2Tooltip = new Tooltip("Export Services");
  Tooltip myRequestsBtnTooltip = new Tooltip("Displays Requests I Created");
  Tooltip allRequestBtnTooltip = new Tooltip("Displays All Current Requests");
  Tooltip assignedBtnTooltip = new Tooltip("Displays Requests I Am Assigned");

  @FXML
  private void initialize() {

    button2.setOnAction(e -> exportServices());
    myRequestsBtn.setOnAction(e -> filterByRequester());
    assignedBtn.setOnAction(e -> filterByEmployee());
    allRequestsBtn.setOnAction(e -> loadServicesFromDB());
    backBtn.setOnAction(e -> buttonClicked(e));
    drawByPermissions();
    Platform.runLater(() -> filterByRequester());

    Tooltip.install(button2, button2Tooltip);
    Tooltip.install(myRequestsBtn, myRequestsBtnTooltip);
    Tooltip.install(allRequestsBtn, allRequestBtnTooltip);
    Tooltip.install(assignedBtn, assignedBtnTooltip);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

    ToggleGroup group = new ToggleGroup();
    myRequestsBtn.setToggleGroup(group);
    assignedBtn.setToggleGroup(group);
    allRequestsBtn.setToggleGroup(group);
    // button3.setToggleGroup(group);
  }

  @Override
  public void drawByPlatform() {
    if (parent.isDesktop) {
      backBtn.setVisible(false);
    }
  }

  @FXML
  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == backBtn) parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
  }

  private void exportServices() {
    try {
      DataOperations.DBtoCSV("SERVICE");
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  private void drawByPermissions() {
    int permissions = Settings.getSettings().getCurrentPermissions();
    if (permissions < 3) {
      allRequestsBtn.setVisible(false);
    }
    if (permissions < 2) {
      assignedBtn.setVisible(false);
    }
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
