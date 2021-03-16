package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import edu.wpi.cs3733.c21.teamY.entity.ActiveGraph;
import edu.wpi.cs3733.c21.teamY.entity.Employee;
import edu.wpi.cs3733.c21.teamY.entity.Graph;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class RequestInfoPageController extends SubPage {
  @FXML private Label title;
  @FXML private Label type;
  @FXML private Label locationLabel;
  @FXML private Label category;
  @FXML private Label urgency;
  @FXML private Label date;
  @FXML private Label requester;
  @FXML private Label requestID;
  @FXML private Label employee;
  @FXML private Label description;
  @FXML private Label typeLabel;
  @FXML private Label categoryLabel;
  @FXML private Label urgencyLabel;
  @FXML private Label dateLabel;
  @FXML private Label requesterLabel;
  @FXML private Label requestIDLabel;
  @FXML private Label employeeLabel;

  @FXML private JFXButton collapseMapBtn;
  @FXML private RowConstraints row1;
  @FXML private RowConstraints contentRow;
  @FXML private RowConstraints row2;
  @FXML private RowConstraints row3;
  @FXML private RowConstraints row4;
  @FXML private RowConstraints row5;
  @FXML private RowConstraints row6;
  @FXML private RowConstraints row7;
  @FXML private RowConstraints row8;
  @FXML private VBox mapVBox;
  //  //  @FXML private VBox leftBox;
  //  //  @FXML private VBox centerBox;
  //  //  @FXML private VBox rightBox;
  @FXML private JFXButton submitBtn;
  @FXML private JFXComboBox employeeComboBox;
  @FXML private JFXButton backBtn;
  @FXML private FontAwesomeIconView expandIcon;
  // @FXML private AnchorPane annoyingVbox;

  Scene scene;

  private boolean expanded = false;
  // private boolean desktop;

  private Service service;

  JFXTextField leftArea;
  JFXTextField centerArea;
  JFXButton saveBtn;

  @FXML
  private void initialize() {
    Platform.runLater(
        () -> {
          loadInformation();
          configSeeLocationButton();
        });
    leftArea = new JFXTextField();
    centerArea = new JFXTextField();
    saveBtn = new JFXButton();
    saveBtn.setOnAction(e -> buttonClicked(e));
    collapseMapBtn.setOnAction(e -> expandMap());
    saveBtn.setCursor(Cursor.HAND);
    submitBtn.setOnAction(e -> submitEmployee());
    backBtn.setOnAction(e -> back());
    submitBtn.setCursor(Cursor.HAND);
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

  private void expandMap() {
    if (!expanded) {
      // expand the map
      animateMap(600);
      expandIcon.setGlyphName("ANGLE_DOUBLE_UP");
    } else {
      // collapse the map;
      animateMap(0);
      expandIcon.setGlyphName("ANGLE_DOUBLE_DOWN");
    }
    expanded = !expanded;
  }

  private void animateMap(int height) {
    if (expanded) {
      hideLabels(!expanded, height);
    } else {
      row1.setMaxHeight(height);
    }

    Timeline timeline = new Timeline();

    ArrayList<KeyValue> values = new ArrayList<KeyValue>();

    KeyValue kv2 = new KeyValue(row1.prefHeightProperty(), height, Interpolator.EASE_IN);

    // KeyFrame kf = new KeyFrame(Duration.seconds(1), kv2);
    KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv2);
    // KeyFrame kf = new KeyFrame(Duration.seconds(1), kv2);
    timeline.getKeyFrames().add(kf);
    timeline.setOnFinished(event -> hideLabels(expanded, height));

    // centerPane.setMinWidth(width);
    timeline.play();
  }

  private void hideLabels(boolean visible, double height) {
    typeLabel.setVisible(!visible);
    urgencyLabel.setVisible(!visible);
    dateLabel.setVisible(!visible);
    requesterLabel.setVisible(!visible);
    requestIDLabel.setVisible(!visible);
    employeeLabel.setVisible(!visible);
    categoryLabel.setVisible(!visible);
    row1.setMaxHeight(height);
  }

  private void submitEmployee() {
    //    System.out.println((String) employeeComboBox.getValue());
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

    category.setText(service.getCategory());
    description.setText(service.getDescription());
    System.out.println(service.getLocation());
    locationLabel.setText(service.getLocation());
    urgency.setText(service.getUrgency());
    date.setText(service.getDate());
    requester.setText(service.getRequester());
    requestID.setText(service.getServiceID());
    employee.setText(service.getEmployee());
    type.setText(service.getType());

    //    if (service.getType().equals("Laundry")) {
    //
    //      //      saveBtn.setText("Save");
    //      //      saveBtn.setPrefWidth(9999);
    //      //      saveBtn.setFont(new Font("Calibri", 15));
    //      //      saveBtn.setStyle("-fx-background-color: #efeff9; ");
    //      //
    //      //      leftArea.setPromptText("Loads of laundry done:");
    //      //      centerArea.setPromptText("Number of garments discarded:");
    //      //      leftArea.setLabelFloat(true);
    //      //      centerArea.setLabelFloat(true);
    //      //      leftArea.setFocusColor(Color.web("#5a5c94"));
    //
    //      //      leftBox.getChildren().add(leftArea);
    //      //      centerBox.getChildren().add(centerArea);
    //      //      rightBox.getChildren().add(saveBtn);
    //    }
  }

  private void back() {
    parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
  }

  private void buttonClicked(ActionEvent e) {

    //    if (!leftArea.getText().equals("") && !leftArea.getText().equals("")) {
    //      // # of loads
    //      String numLoads =
    //          "# loads done: " + leftArea.getText() + "  # discarded garments: " +
    // centerArea.getText();
    //
    //      StageInformation info = (StageInformation)
    // title.getScene().getWindow().getUserData();
    //      Service service = info.getService();
    //      service.setAdditionalInfo(numLoads);
    //      try {
    //        DataOperations.updateServiceAdditionalInfoOnly(service.getServiceID(), numLoads);
    //      } catch (SQLException throwables) {
    //        throwables.printStackTrace();
    //      }
    //      parent.loadRightSubPage("RequestInfoPage.fxml");
    //    }
  }

  @Override
  public void loadNavigationBar() {
    parent.setCenterColumnWidth(350);
  }

  //  private ServiceRequestInfoElementController createInfoBox(String title, String data) {
  //    if (parent.isDesktop) {
  //      infoBox.setPadding(new Insets(2, 5, 4, 50));
  //    } else {
  //      infoBox.setPadding(new Insets(2, 100, 4, 50));
  //    }
  //    scene = infoBox.getScene();
  //    FXMLLoader fxmlLoader = new FXMLLoader();
  //    ServiceRequestInfoElementController controller = null;
  //    try {
  //      Node node =
  //
  // fxmlLoader.load(getClass().getResource("ServiceRequestInfoElement.fxml").openStream());
  //      controller = (ServiceRequestInfoElementController) fxmlLoader.getController();
  //      controller.populateInformation(title, data, parent.isDesktop);
  //      infoBox.getChildren().add(node);
  //    } catch (IOException e) {
  //      e.printStackTrace();
  //    }
  //    return controller;
  //  }

  private void configSeeLocationButton() {
    MapController mapInsertController = null;
    FXMLLoader fxmlLoader = new FXMLLoader();
    try {
      Node node = fxmlLoader.load(getClass().getResource("MapUserControl.fxml").openStream());
      mapInsertController = (MapController) fxmlLoader.getController();
      mapInsertController.setParent(parent);
      mapInsertController.setAdminPage(false);
      mapInsertController.setLocationPopUp(true);
      mapVBox.getChildren().add(node);
      // call method before page load
    } catch (IOException exception) {
      exception.printStackTrace();
    }
    if (mapInsertController == null) {
      return;
    }

    // popupAnchor.setClip(popupAnchor);

    Graph graph = null;
    try {
      graph = ActiveGraph.getActiveGraph();
    } catch (Exception exception) {
    }
    if (graph == null) {
      return;
    }

    String longname = service.getLocation();
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
    mapInsertController.changeMapImage(mapInsertController.getMapOrder().get(floor), false);
    MapController.CircleEx nodeCircle = mapInsertController.addNodeCircle(node);
    if (nodeCircle == null) {
      System.out.println("OOF");
    }
    mapInsertController.selectCircle(nodeCircle);
    mapInsertController.hideFloorMenu();
  }
}
