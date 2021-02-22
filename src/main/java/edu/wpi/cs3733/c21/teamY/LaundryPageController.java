package edu.wpi.cs3733.c21.teamY;

import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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

  // unused constructor
  public LaundryPageController() {
    super();
  }

  // this runs once the FXML loads in to attach functions to components
  @FXML
  private void initialize() {
    // attaches a handler to the button with a lambda expression
    cancelBtn.setOnAction(e -> serviceButtonClicked(e, "LaundryPage.fxml"));
    clearBtn.setOnAction(e -> serviceButtonClicked(e, "LaundryPage.fxml"));
    backBtn.setOnAction(e -> serviceButtonClicked(e, "LaundryPage.fxml"));
    submitBtn.setOnAction(e -> submitBtnClicked());
    exitBtn.setOnAction(e -> exitButtonClicked());
  }

  @FXML
  private void submitBtnClicked() {
    // put code for submitting a service request here
    Service service = new Service(this.IDCount, "Laundry");
    this.IDCount++;
    service.setCategory((String) category.getValue());
    service.setLocation(locationField.getText());
    service.setDescription(description.getText());
    try {
      ServiceRequestDBops.saveService(service);
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
    }
  }
}
