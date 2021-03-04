package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class LaundrySubPageController extends GenericServiceFormPage {
  // connects the scenebuilder button to a code button
  // add buttons to other scenes here
  @FXML private JFXButton clearBtn;
  @FXML private JFXButton backBtn;
  @FXML private JFXButton submitBtn;
  @FXML private JFXComboBox category;
  @FXML private JFXTextArea description;
  @FXML private JFXComboBox locationField;
  @FXML private StackPane stackPane;

  private Settings settings;

  private ArrayList<String> categories;

  // unused constructor
  public LaundrySubPageController() {}

  // this runs once the FXML loads in to attach functions to components
  @FXML
  private void initialize() {

    settings = Settings.getSettings();

    categories = new ArrayList<String>();
    categories.add("Bedding");
    categories.add("Dry Cleaning");
    categories.add("Clothing");
    // attaches a handler to the button with a lambda expression

    backBtn.setOnAction(e -> buttonClicked(e));
    submitBtn.setOnAction(e -> submitBtnClicked());
    clearBtn.setOnAction(e -> clearButton());

    for (String c : categories) category.getItems().add(c);
    locationField.getItems().add("cafeteria");
  }

  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == backBtn) parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
  }

  private void clearButton() {
    category.setValue(null);
    description.setText("");
    locationField.setValue(null);
  }

  @FXML
  private void submitBtnClicked() {
    // put code for submitting a service request here

    if (category.getValue().equals(null)
        || description.getText().equals("")
        || locationField.getValue().equals(null)) {
      nonCompleteForm(stackPane);
    } else {

      Service service = new Service(this.IDCount, "Laundry");
      this.IDCount++;
      service.setCategory((String) category.getValue());
      service.setLocation((String) locationField.getValue());
      service.setDescription(description.getText());
      System.out.println(settings.getCurrentUsername());
      service.setRequester(settings.getCurrentUsername());

      try {
        DataOperations.saveService(service);
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }

      submittedPopUp(stackPane);

      clearButton();
    }
  }
}
