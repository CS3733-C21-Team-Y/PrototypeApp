package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class ServiceRequestElementController extends CenterPage {
  @FXML private Label type;
  @FXML private Label test;
  @FXML private Label serviceID;
  @FXML private Label status;
  @FXML private GridPane statusGrid;
  @FXML private JFXButton statusBtn;
  @FXML private JFXButton incompleteBtn;
  @FXML private JFXButton inProgressBtn;
  @FXML private JFXButton completeBtn;

  private Service service;

  @FXML
  private void initialize() {
    incompleteBtn.setOnAction(e -> statusBtnClicked(e));
    inProgressBtn.setOnAction(e -> statusBtnClicked(e));
    completeBtn.setOnAction(e -> statusBtnClicked(e));
    statusBtn.setOnAction(e -> toggleStatusGrid());
    statusGrid.setVisible(false);
  }

  private void toggleStatusGrid() {
    statusGrid.setVisible(!statusGrid.isVisible());
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
    StageInformation info = (StageInformation) type.getScene().getWindow().getUserData();
    info.setService(service);
    type.getScene().getWindow().setUserData(info);
  }
}
