package edu.wpi.cs3733.c21.teamY.pages;

import edu.wpi.cs3733.c21.teamY.dataops.JDBCUtils;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LaundryPageController extends GenericServiceFormPage {

  // connects the scenebuilder button to a code button
  // add buttons to other scenes here
  @FXML private Button cancelBtn;
  @FXML private Button clearBtn;
  @FXML private Button exitBtn;
  @FXML private Button backBtn;
  @FXML private Button submitBtn;
  @FXML private ComboBox category;
  @FXML private TextArea description;
  @FXML private TextField locationField;

  private ArrayList<String> categories;

  // unused constructor
  public LaundryPageController() {
    super();
  }

  // this runs once the FXML loads in to attach functions to components
  @FXML
  private void initialize() {

    categories = new ArrayList<String>();
    categories.add("Bedding");
    categories.add("Dry Cleaning");
    categories.add("Clothing");
    // attaches a handler to the button with a lambda expression
    cancelBtn.setOnAction(e -> serviceButtonClicked(e, "LaundryPage.fxml"));
    clearBtn.setOnAction(e -> serviceButtonClicked(e, "LaundryPage.fxml"));
    backBtn.setOnAction(e -> serviceButtonClicked(e, "LaundryPage.fxml"));
    submitBtn.setOnAction(e -> submitBtnClicked());
    exitBtn.setOnAction(e -> exitButtonClicked());

    for (String c : categories) category.getItems().add(c);
  }

  @FXML
  private void submitBtnClicked() {
    // put code for submitting a service request here
    Stage stage = null;
    Service service = new Service(this.IDCount, "Laundry");
    this.IDCount++;
    service.setCategory((String) category.getValue());
    service.setLocation(locationField.getText());
    service.setDescription(description.getText());
    try {
      stage = (Stage) submitBtn.getScene().getWindow();
      JDBCUtils.saveService(service);
      stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("LaundryPage.fxml"))));
      stage.show();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
