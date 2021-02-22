package edu.wpi.cs3733.c21.teamY;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ServiceRequestInfoPageController extends GenericPage {

  @FXML private Button completeBtn;
  @FXML private Button exitBtn;
  @FXML private GridPane dataGrid;
  @FXML private Button loadBtn;

  private Stage stage;
  //  @FXML private Label dateInfo;
  //  @FXML private Label requestInfo;

  public ServiceRequestInfoPageController() {}

  @FXML
  private void initialize() {
    exitBtn.setOnAction(e -> exitButtonClicked());
    completeBtn.setOnAction(e -> buttonClicked(e));
    loadBtn.setOnAction(e -> loadInformation());
  }

  private void loadInformation() {
    stage = (Stage) completeBtn.getScene().getWindow();
    Service service = (Service) stage.getUserData();

    int rowCount = 0;
    // dataGrid.addColumn(1);
    // display Service ID
    Label serviceIDLabel = new Label("Service ID: ");
    dataGrid.add(serviceIDLabel, 0, rowCount);
    Label serviceID = new Label("" + service.getServiceID());
    dataGrid.add(serviceID, 1, rowCount);

    // display service type
    rowCount++;
    dataGrid.addRow(rowCount);
    Label serviceTypeLabel = new Label("Service Type: ");
    dataGrid.add(serviceTypeLabel, 0, rowCount);
    Label serviceType = new Label(service.getType());
    dataGrid.add(serviceType, 1, rowCount);

    // display location
    rowCount++;
    dataGrid.addRow(rowCount);
    Label locationLabel = new Label("Location: ");
    dataGrid.add(locationLabel, 0, rowCount);
    Label location = new Label(service.getLocation());
    dataGrid.add(location, 1, rowCount);
  }

  @FXML
  public void buttonClicked(ActionEvent e) {
    if (e.getSource() == completeBtn) {}
  }
}
