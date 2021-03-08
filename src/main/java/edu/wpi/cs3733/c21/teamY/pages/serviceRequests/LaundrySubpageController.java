package edu.wpi.cs3733.c21.teamY.pages.serviceRequests;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.dataops.Settings;
import edu.wpi.cs3733.c21.teamY.entity.Employee;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import edu.wpi.cs3733.c21.teamY.pages.GenericServiceFormPage;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class LaundrySubpageController extends GenericServiceFormPage {
  // connects the scenebuilder button to a code button
  // add buttons to other scenes here
  @FXML private JFXButton clearBtn;
  @FXML private JFXButton backBtn;
  @FXML private JFXButton submitBtn;
  @FXML private JFXComboBox category;
  @FXML private JFXTextArea description;
  @FXML private JFXComboBox locationField;
  @FXML private StackPane stackPane;
  @FXML private JFXComboBox employeeComboBox;

  private Settings settings;

  private ArrayList<String> categories;

  // unused constructor
  public LaundrySubpageController() {}

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

    if (settings.getCurrentPermissions() == 3) {
      employeeComboBox.setVisible(true);
      try {
        ArrayList<Employee> employeeList = DataOperations.getStaffList();
        for (Employee employee : employeeList) {
          employeeComboBox.getItems().add(employee.getEmployeeID());
        }
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }
    } else {
      employeeComboBox.setVisible(false);
    }
  }

  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == backBtn) parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
  }

  private void clearButton() {
    category.setValue(null);
    description.setText("");
    locationField.setValue(null);
    employeeComboBox.getSelectionModel().clearSelection();
  }

  @FXML
  private void submitBtnClicked() {
    // put code for submitting a service request here

    clearIncomplete(category);
    clearIncomplete(description);
    clearIncomplete(locationField);
    clearIncomplete(employeeComboBox);

    if (category.getValue() == null
        || description.getText().equals("")
        || locationField.getValue() == null|| employeeComboBox.getValue()==null) {
      if (category.getValue() == null) {
        incomplete(category);
      }
      if (description.getText().equals("")) {
        incomplete(description);
      }
      if (locationField.getValue() == null) {
        incomplete(locationField);
      }
      if(employeeComboBox.getValue() == null){incomplete(employeeComboBox);}
      nonCompleteForm(stackPane);
    }

    else {

      Service service = new Service(this.IDCount, "Laundry");
      this.IDCount++;
      service.setCategory((String) category.getValue());
      service.setLocation((String) locationField.getValue());
      service.setDescription(description.getText());
      System.out.println(settings.getCurrentUsername());
      service.setRequester(settings.getCurrentUsername());
      if (settings.getCurrentPermissions() == 3) {
        service.setEmployee((String) employeeComboBox.getValue());
      } else {
        service.setEmployee("admin");
      }

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
