package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import edu.wpi.cs3733.c21.teamY.entity.Employee;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class RequestInfoPageController<label> extends SubPage {
  @FXML private Label title;
  @FXML private VBox infoBox;
  @FXML private VBox leftBox;
  @FXML private VBox centerBox;
  @FXML private VBox rightBox;
  @FXML private JFXButton submitBtn;
  @FXML private JFXComboBox employeeComboBox;
  Scene scene;

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
  }

  private void loadInformation() {
    service = Settings.getSettings().getCurrentDisplayedService();

    createInfoBox("Type: ", service.getType());
    if (service.getDescription().length() > 0)
      createInfoBox("Description: ", service.getDescription());
    if (service.getLocation().length() > 0) createInfoBox("Location: ", service.getLocation());
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

  private void createInfoBox(String title, String data) {
    scene = infoBox.getScene();
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
