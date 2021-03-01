package edu.wpi.cs3733.c21.teamY.pages;

import edu.wpi.cs3733.c21.teamY.dataops.JDBCUtils;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class ServiceRequestInfoPageController extends GenericPage {

  @FXML private Button completeBtn;
  @FXML private Button incompleteBtn;
  @FXML private Button notStartedBtn;
  @FXML private Button exitBtn;
  @FXML private GridPane dataGrid;
  @FXML private Button loadBtn;
  @FXML private Button backBtn;
  @FXML private AnchorPane anchor;

  private Service service;

  private Stage stage;
  //  @FXML private Label dateInfo;
  //  @FXML private Label requestInfo;

  public ServiceRequestInfoPageController() {}

  @FXML
  private void initialize() {
    completeBtn.setOnAction(e -> buttonClicked(e));
    loadBtn.setOnAction(e -> loadInformation());
    backBtn.setOnAction(e -> buttonClicked(e));
    completeBtn.setOnAction(e -> buttonClicked(e));
    incompleteBtn.setOnAction(e -> buttonClicked(e));
    notStartedBtn.setOnAction(e -> buttonClicked(e));
    Platform.runLater(() -> loadInformation());
  }

  private void loadInformation() {
    stage = (Stage) completeBtn.getScene().getWindow();
    service = (Service) stage.getUserData();

    int rowCount = 0;
    // dataGrid.addColumn(1);
    // display Service ID
    Label serviceIDLabel = new Label("Service ID: ");
    dataGrid.add(serviceIDLabel, 1, rowCount);
    Label serviceID = new Label("" + service.getServiceID());
    dataGrid.add(serviceID, 2, rowCount);
    RowConstraints constraints = new RowConstraints();
    constraints.setPercentHeight(100);
    dataGrid.getRowConstraints().add(constraints);

    // display service type
    rowCount++;
    dataGrid.addRow(rowCount);
    Label serviceTypeLabel = new Label("Service Type: ");
    dataGrid.add(serviceTypeLabel, 1, rowCount);
    Label serviceType = new Label(service.getType());
    dataGrid.add(serviceType, 2, rowCount);
    dataGrid.getRowConstraints().add(constraints);

    // display location
    rowCount++;
    dataGrid.addRow(rowCount);
    Label locationLabel = new Label("Location: ");
    dataGrid.add(locationLabel, 1, rowCount);
    Label location = new Label(service.getLocation());
    dataGrid.add(location, 2, rowCount);
    dataGrid.getRowConstraints().add(constraints);

    // display category
    rowCount++;
    dataGrid.addRow(rowCount);
    Label categoryLabel = new Label("Category: ");
    dataGrid.add(categoryLabel, 1, rowCount);
    Label category = new Label(service.getCategory());
    dataGrid.add(category, 2, rowCount);
    dataGrid.getRowConstraints().add(constraints);

    // display urgency
    if (!service.getUrgency().equals("")) {
      rowCount++;
      dataGrid.addRow(rowCount);
      Label urgencyLabel = new Label("Urgency: ");
      dataGrid.add(urgencyLabel, 1, rowCount);
      Label urgency = new Label(service.getUrgency());
      dataGrid.add(urgency, 2, rowCount);
      dataGrid.getRowConstraints().add(constraints);
    }
    // display date
    if (!service.getDate().equals("")) {
      rowCount++;
      dataGrid.addRow(rowCount);
      Label dateLabel = new Label("Date: ");
      dataGrid.add(dateLabel, 1, rowCount);
      Label date = new Label(service.getDate());
      dataGrid.add(date, 2, rowCount);
      dataGrid.getRowConstraints().add(constraints);
    }
    // display description
    if (!service.getDescription().equals("")) {
      rowCount++;
      dataGrid.addRow(rowCount);
      Label descriptionLabel = new Label("Description: ");
      dataGrid.add(descriptionLabel, 1, rowCount);
      Label description = new Label(service.getDescription());
      dataGrid.add(description, 2, rowCount);
      dataGrid.getRowConstraints().add(constraints);
    }
  }

  @FXML
  protected void buttonClicked(ActionEvent e) {
    // error handling for FXMLLoader.load
    try {
      // initializing stage
      Stage stage = null;
      stage = (Stage) ((Button) e.getSource()).getScene().getWindow();
      if ((e.getSource()) == backBtn) {
        // sets the new scene to the alex page
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("RequestsManager.fxml"))));
      } else if ((e.getSource()) == completeBtn) {
        JDBCUtils.updateServiceStatus(service, 1);
      } else if ((e.getSource()) == incompleteBtn) {
        JDBCUtils.updateServiceStatus(service, 0);
      } else if ((e.getSource()) == notStartedBtn) {
        JDBCUtils.updateServiceStatus(service, -1);
      }
      // display new stage
      stage.show();
    } catch (Exception exp) {
    }
  }
}
