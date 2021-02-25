package edu.wpi.cs3733.c21.teamY.pages;

import edu.wpi.cs3733.c21.teamY.dataops.ServiceRequestDBops;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class MaintenancePageController extends GenericServiceFormPage {

  // connects the scenebuilder button to a code button
  // add buttons to other scenes here
  @FXML private Button cancelBtn;
  @FXML private Button clearBtn;
  @FXML private Button exitBtn;
  @FXML private Button backBtn;
  @FXML private Button submitBtn;
  @FXML private ComboBox category;
  @FXML private TextArea description;
  @FXML private ComboBox urgency;
  @FXML private DatePicker date;
  @FXML private TextField locationField;

  private ArrayList<String> categories;
  private ArrayList<String> urgencies;

  // unused constructor
  public MaintenancePageController() {
    super();
  }

  // this runs once the FXML loads in to attach functions to components
  @FXML
  private void initialize() {
    categories = new ArrayList<String>();
    categories.add("Room Damage");
    categories.add("Electrical");
    categories.add("Water/Plumbing");
    categories.add("Janitorial");
    categories.add("Property Damage");
    urgencies = new ArrayList<String>();
    urgencies.add("Low");
    urgencies.add("Medium");
    urgencies.add("High");
    // attaches a handler to the button with a lambda expression
    cancelBtn.setOnAction(e -> serviceButtonClicked(e, "MaintenancePage.fxml"));
    clearBtn.setOnAction(e -> serviceButtonClicked(e, "MaintenancePage.fxml"));
    backBtn.setOnAction(e -> serviceButtonClicked(e, "MaintenancePage.fxml"));
    submitBtn.setOnAction(e -> submitBtnClicked());
    exitBtn.setOnAction(e -> exitButtonClicked());

    for (String c : categories) category.getItems().add(c);
    for (String c : urgencies) urgency.getItems().add(c);
  }

  @FXML
  private void submitBtnClicked() {
    Stage stage = null;
    // put code for submitting a service request here
    Service service = new Service(this.IDCount, "Maintenance");
    this.IDCount++;
    System.out.println(this.IDCount);
    service.setCategory((String) category.getValue());
    service.setLocation(locationField.getText());
    service.setDescription(description.getText());
    service.setUrgency((String) urgency.getValue());
    service.setDate(date.getValue().toString());
    Stage popUp = new Stage();
    try {
      ServiceRequestDBops.saveService(service);
      stage = (Stage) submitBtn.getScene().getWindow();
      stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("MaintenancePage.fxml"))));
      stage.show();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
