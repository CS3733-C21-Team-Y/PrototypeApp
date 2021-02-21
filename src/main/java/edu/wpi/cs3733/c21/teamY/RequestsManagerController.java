package edu.wpi.cs3733.c21.teamY;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

  public RequestsManagerController() {
    super();
  }

  @FXML
  private void initialize() {
    exitBtn.setOnAction(e -> exitButtonClicked());
    backBtn.setOnAction(e -> buttonClicked(e));
    addServiceBtn.setOnAction(e -> addServiceToGrid(new Service("Laundry")));
    rowCount = 0;
  }

  private void addServiceToGrid(Service service) {
    rowCount++;
    Label serviceType = new Label(service.getType());
    serviceGrid.addColumn(rowCount);
    serviceGrid.add(serviceType, 0, rowCount);
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
