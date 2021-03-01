package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.c21.teamY.dataops.JDBCUtils;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.swing.*;

public class LaundrySubPageController extends GenericServiceFormPage {
  // connects the scenebuilder button to a code button
  // add buttons to other scenes here
  @FXML private JFXButton clearBtn;
  @FXML private JFXButton backBtn;
  @FXML private JFXButton submitBtn;
  @FXML private JFXComboBox category;
  @FXML private JFXTextArea description;
  @FXML private JFXComboBox locationField;

  private ArrayList<String> categories;

  // unused constructor
  public LaundrySubPageController() {}

  // this runs once the FXML loads in to attach functions to components
  @FXML
  private void initialize() {

    categories = new ArrayList<String>();
    categories.add("Bedding");
    categories.add("Dry Cleaning");
    categories.add("Clothing");
    // attaches a handler to the button with a lambda expression

    backBtn.setOnAction(e -> buttonClicked(e));
    // submitBtn.setOnAction(e -> submitBtnClicked());

    for (String c : categories) category.getItems().add(c);
  }

  private void buttonClicked(ActionEvent e) {
    if (e.getSource() == backBtn) parent.loadRightSubPage("ServiceRequestManagerSubpage.fxml");
  }

  @FXML
  private void submitBtnClicked() {
    // put code for submitting a service request here
    Stage stage = null;
    Service service = new Service(this.IDCount, "Laundry");
    // this.IDCount++;
    service.setCategory((String) category.getValue());
    service.setLocation((String) locationField.getValue());
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
