package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;

public class ServiceRequestNavigatorController extends SubPage {

  @FXML public ScrollPane scrollPane;
  @FXML private VBox serviceBox;
  @FXML private JFXButton export;
  @FXML private ToggleButton myRequestsBtn;
  @FXML private ToggleButton allRequestsBtn;
  @FXML private ToggleButton assignedBtn;
  @FXML private JFXButton backBtn;
  @FXML private JFXComboBox typeCombo;
  @FXML private JFXComboBox statusCombo;
  @FXML private JFXComboBox employeeCombo;
  @FXML private JFXButton clearSortBtn;

  private Settings settings;

  // tooltip instantiations
  Tooltip button2Tooltip = new Tooltip("Export Services");
  Tooltip myRequestsBtnTooltip = new Tooltip("Displays Requests I Created");
  Tooltip allRequestBtnTooltip = new Tooltip("Displays All Current Requests");
  Tooltip assignedBtnTooltip = new Tooltip("Displays Requests I Am Assigned");

  private String currentType = "";
  private int currentStatus = 2;
  private String currentEmployeeCombo = "";

  @FXML
  private void initialize() {

    settings = Settings.getSettings();

    export.setOnAction(e -> exportServices());
    export.setCursor(Cursor.HAND);
    export.setOnAction(e -> exportServices());
    clearSortBtn.setOnAction(e -> clearSorts());
    myRequestsBtn.setOnAction(e -> filterByRequester());
    myRequestsBtn.setCursor(Cursor.HAND);
    assignedBtn.setOnAction(e -> filterByEmployee());
    assignedBtn.setCursor(Cursor.HAND);
    allRequestsBtn.setOnAction(e -> loadServicesFromDB());
    allRequestsBtn.setCursor(Cursor.HAND);
    backBtn.setOnAction(e -> buttonClicked(e));
    backBtn.setCursor(Cursor.HAND);
    drawByPermissions();
    Platform.runLater(
        () -> {
          if (parent.getNavMode().equals("ASSIGNED")) {
            filterByEmployee();
            assignedBtn.setSelected(true);
            myRequestsBtn.setSelected(false);
          } else if (parent.getNavMode().equals("ALL")) {
            loadServicesFromDB();
            allRequestsBtn.setSelected(true);
            myRequestsBtn.setSelected(false);
          } else {
            filterByRequester();
          }
        });

    Tooltip.install(export, button2Tooltip);
    Tooltip.install(myRequestsBtn, myRequestsBtnTooltip);
    Tooltip.install(allRequestsBtn, allRequestBtnTooltip);
    Tooltip.install(assignedBtn, assignedBtnTooltip);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

    ToggleGroup group = new ToggleGroup();
    myRequestsBtn.setToggleGroup(group);
    assignedBtn.setToggleGroup(group);
    allRequestsBtn.setToggleGroup(group);
    // button3.setToggleGroup(group);

    typeCombo.getItems().add("Language");
    typeCombo.getItems().add("Gift Delivery");
    typeCombo.getItems().add("Laundry");
    typeCombo.getItems().add("Floral Delivery");
    typeCombo.getItems().add("Audio Visual");
    typeCombo.getItems().add("IT Services");
    typeCombo.getItems().add("Medicine");
    typeCombo.getItems().add("Sanitization");
    typeCombo.getItems().add("Security");
    typeCombo.getItems().add("Maintenance");
    typeCombo.getItems().add("Inside Hospital");
    typeCombo.getItems().add("Outside Hospital");
    typeCombo.getItems().add("Covid Form");

    statusCombo.getItems().add("Incomplete"); // -1
    statusCombo.getItems().add("In Progress"); // 0
    statusCombo.getItems().add("Complete"); // 1

    if (settings.getCurrentPermissions() == 3) {
      employeeCombo.setVisible(true);
      try {
        ArrayList<Employee> employeeList = DataOperations.getStaffList();
        for (Employee employee : employeeList) {
          employeeCombo.getItems().add(employee.getEmployeeID());
        }
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }
    } else {
      employeeCombo.setVisible(false);
    }

    typeCombo
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              this.currentType = (String) newValue;
              if (myRequestsBtn.isSelected()) {
                filterByRequester();
              } else if (assignedBtn.isSelected()) {
                filterByEmployee();
              } else {
                loadServicesFromDB();
              }
              System.out.println("new type of service value is" + newValue);
            });
    statusCombo
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue.equals("Incomplete")) {
                this.currentStatus = -1;
              } else if (newValue.equals("In Progress")) {
                this.currentStatus = 0;
              } else {
                this.currentStatus = 1;
              }
              if (myRequestsBtn.isSelected()) {
                filterByRequester();
              } else if (assignedBtn.isSelected()) {
                filterByEmployee();
              } else {
                loadServicesFromDB();
              }

              System.out.println("new status of service value is" + newValue);
            });
    employeeCombo
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              this.currentEmployeeCombo = (String) newValue;
              if (myRequestsBtn.isSelected()) {
                filterByRequester();
              } else if (assignedBtn.isSelected()) {
                filterByEmployee();
              } else {
                loadServicesFromDB();
              }
              System.out.println("new type of service value is" + newValue);
            });
  }

  private void clearSorts() {
    typeCombo.setValue(null);
    statusCombo.setValue(null);
    employeeCombo.setValue(null);
    this.currentType = "";
    this.currentStatus = 2;
    this.currentEmployeeCombo = "";
    if (myRequestsBtn.isSelected()) {
      filterByRequester();
    } else if (assignedBtn.isSelected()) {
      filterByEmployee();
    } else {
      loadServicesFromDB();
    }
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
    String username = Settings.getSettings().getCurrentUsername();
    serviceBox.getChildren().clear();
    myRequestsBtn.setStyle("-fx-background-color: #efeff9; -fx-text-fill: #5a5c94");
    allRequestsBtn.setStyle("-fx-background-color: #5a5c94; -fx-text-fill: #efeff9");
    assignedBtn.setStyle("-fx-background-color: #5a5c94;-fx-text-fill: #efeff9");
    try {
      // ArrayList<Service> serviceList = DataOperations.exportService("", "");
      ArrayList<Service> serviceList =
          DataOperations.exportSortedService(currentType, currentStatus, currentEmployeeCombo);
      for (Service service : serviceList) {
        if (service.getRequester().equals(username)) addService(service);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    parent.setNavMode("REQUESTER");
  }

  private void filterByEmployee() {
    String username = Settings.getSettings().getCurrentUsername();
    serviceBox.getChildren().clear();
    assignedBtn.setStyle("-fx-background-color: #efeff9; -fx-text-fill: #5a5c94");
    allRequestsBtn.setStyle("-fx-background-color: #5a5c94; -fx-text-fill: #efeff9");
    myRequestsBtn.setStyle("-fx-background-color: #5a5c94;-fx-text-fill: #efeff9");
    serviceBox.getChildren().clear();
    try {
      // ArrayList<Service> serviceList = DataOperations.exportService("", "");
      ArrayList<Service> serviceList =
          DataOperations.exportSortedService(currentType, currentStatus, currentEmployeeCombo);
      for (Service service : serviceList) {
        if (service.getEmployee().equals(username)) addService(service);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    parent.setNavMode("ASSIGNED");
  }

  private void loadServicesFromDB() {
    serviceBox.getChildren().clear();
    allRequestsBtn.setStyle("-fx-background-color: #efeff9; -fx-text-fill: #5a5c94");
    assignedBtn.setStyle("-fx-background-color: #5a5c94; -fx-text-fill: #efeff9");
    myRequestsBtn.setStyle("-fx-background-color: #5a5c94;-fx-text-fill: #efeff9");
    try {
      // ArrayList<Service> serviceList = DataOperations.exportService("", "");
      ArrayList<Service> serviceList =
          DataOperations.exportSortedService(currentType, currentStatus, currentEmployeeCombo);
      for (Service service : serviceList) {
        addService(service);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    parent.setNavMode("ALL");
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

  //  private void processServiceList(ArrayList<Service> services){
  //    ArrayList<Service> services1=new ArrayList<>();
  //    for (Service service : services) {
  //      if(currentEmployeeCombo.equals("")&&currentStatus.equals("")&&currentType.equals("")){
  //        return;
  //      }
  //    }
  //  }
}
