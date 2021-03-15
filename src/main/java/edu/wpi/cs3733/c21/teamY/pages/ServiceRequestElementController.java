package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;

public class ServiceRequestElementController extends SubPage {
  @FXML private Label type;
  @FXML private Label test;
  @FXML private Label serviceID;
  @FXML private Label status;
  @FXML private GridPane statusGrid;
  @FXML private GridPane actualStatusGrid;
  @FXML private JFXButton statusBtn;
  @FXML private JFXButton incompleteBtn;
  @FXML private JFXButton inProgressBtn;
  @FXML private JFXButton deleteBtn;
  @FXML private JFXButton completeBtn;
  @FXML private Label employeeLabel;
  Scene scene;

  private Service service;

  Tooltip statusBtnTooltip = new Tooltip("Toggle Actions");

  @FXML
  private void initialize() {
    incompleteBtn.setOnAction(e -> statusBtnClicked(e));
    inProgressBtn.setOnAction(e -> statusBtnClicked(e));
    completeBtn.setOnAction(e -> statusBtnClicked(e));
    deleteBtn.setOnAction(e -> deleteService());
    statusBtn.setOnAction(e -> toggleStatusGrid());
    statusGrid.setVisible(false);

    Tooltip.install(statusBtn, statusBtnTooltip);
  }

  private void deleteService() {
    try {
      DataOperations.removeService(service.getServiceID());
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    parent.loadCenterSubPage("ServiceRequestNavigator.fxml");

    // parent.setCenterColumnWidth(350);

  }

  private void toggleStatusGrid() {
    statusGrid.setVisible(!statusGrid.isVisible());
    int permissions = Settings.getSettings().getCurrentPermissions();
    if (permissions >= 2) {
      actualStatusGrid.setMinHeight(120);
      incompleteBtn.setVisible(true);
      inProgressBtn.setVisible(true);
      completeBtn.setVisible(true);
    } else {
      actualStatusGrid.setMinHeight(30);
      incompleteBtn.setVisible(false);
      inProgressBtn.setVisible(false);
      completeBtn.setVisible(false);
    }
  }

  private void statusBtnClicked(ActionEvent e) {
    if (e.getSource() == incompleteBtn) {
      service.setStatus(-1);
    } else if (e.getSource() == inProgressBtn) {
      service.setStatus(0);
    } else if (e.getSource() == completeBtn) {
      service.setStatus(1);
    }

    try {
      DataOperations.updateServiceStatus(service, service.getStatus());
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }

    toggleStatusGrid();
    populateInformation(service);
  }

  public void populateInformation(Service service) {

    this.service = service;
    type.setText(service.getType());
    test.setText(service.getLocation());
    serviceID.setText("ID #: " + service.getServiceID());
    employeeLabel.setText(service.getEmployee());
    if (service.getType().equals("Covid Form")) {
      completeBtn.setText("Low Risk Entry");
      inProgressBtn.setText("High Risk Entry");
      if (service.getStatus() == -1) {
        status.setText("INCOMPLETE");
      } else if (service.getStatus() == 0) {
        status.setText("HIGH-RISK");
      } else if (service.getStatus() == 1) {
        status.setText("LOW-RISK");
      }
      return;
    }
    if (service.getStatus() == -1) {
      status.setText("INCOMPLETE");
    } else if (service.getStatus() == 0) {
      status.setText("IN PROGRESS");
    } else if (service.getStatus() == 1) {
      status.setText("COMPLETE");
    }
  }

  public void openRequest() {
    parent.loadRightSubPage("RequestInfoPage.fxml");
    if (parent.isDesktop) {
      parent.setCenterColumnWidth(350);
    } else {
      parent.setCenterColumnWidth(0);
    }
    Settings.getSettings().setCurrentDisplayedService(service);
  }
}
