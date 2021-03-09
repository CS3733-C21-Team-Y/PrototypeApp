package edu.wpi.cs3733.c21.teamY.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import edu.wpi.cs3733.c21.teamY.dataops.DataOperations;
import edu.wpi.cs3733.c21.teamY.entity.Employee;
import edu.wpi.cs3733.c21.teamY.entity.TableEmployee;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;

public class EditEmployeeTableController extends SubPage {
  @FXML public JFXTreeTableView<TableEmployee> treeTable;
  @FXML public JFXButton expandBtn;
  @FXML public FontAwesomeIconView expandIcon;
  @FXML public JFXButton employeeTableBtn;
  @FXML public JFXButton nodeTableBtn;
  @FXML public JFXButton exportBtn;

  public JFXTreeTableColumn<TableEmployee, String> firstnameCol;
  public JFXTreeTableColumn<TableEmployee, String> lastnameCol;
  public JFXTreeTableColumn<TableEmployee, String> employeeIDCol;
  public JFXTreeTableColumn<TableEmployee, String> passwordCol;
  public JFXTreeTableColumn<TableEmployee, String> emailCol;
  public JFXTreeTableColumn<TableEmployee, String> accessLevelCol;
  public JFXTreeTableColumn<TableEmployee, String> primaryWorkspaceCol;
  public JFXTreeTableColumn<TableEmployee, String> saltCol;

  private boolean expanded = false;

  private ArrayList<Employee> employees = new ArrayList<>();

  public EditEmployeeTableController() {}

  public void initialize() {
    treeTable.setFixedCellSize(30);
    expandBtn.setOnAction(e -> expandTable(e));
    employeeTableBtn.setOnAction(e -> parent.loadRightSubPage("EditEmployeeTable.fxml"));
    nodeTableBtn.setOnAction(e -> parent.loadRightSubPage("EditNodeTable.fxml"));
    exportBtn.setOnAction(e -> exportToCSV());

    firstnameCol = new JFXTreeTableColumn<>("First Name");
    firstnameCol.setPrefWidth(80);
    firstnameCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<TableEmployee, String> param) -> {
          if (firstnameCol.validateValue(param)) {
            return param.getValue().getValue().getFirstName();
          } else {
            return firstnameCol.getComputedValue(param);
          }
        });
    lastnameCol = new JFXTreeTableColumn<>("Last Name");
    lastnameCol.setPrefWidth(80);
    lastnameCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<TableEmployee, String> param) -> {
          if (lastnameCol.validateValue(param)) {
            return param.getValue().getValue().getLastName();
          } else {
            return lastnameCol.getComputedValue(param);
          }
        });
    employeeIDCol = new JFXTreeTableColumn<>("Employee ID");
    employeeIDCol.setPrefWidth(80);
    employeeIDCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<TableEmployee, String> param) -> {
          if (employeeIDCol.validateValue(param)) {
            return param.getValue().getValue().getEmployeeID();
          } else {
            return employeeIDCol.getComputedValue(param);
          }
        });
    passwordCol = new JFXTreeTableColumn<>("Password");
    passwordCol.setPrefWidth(80);
    passwordCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<TableEmployee, String> param) -> {
          if (passwordCol.validateValue(param)) {
            return param.getValue().getValue().getPassword();
          } else {
            return passwordCol.getComputedValue(param);
          }
        });
    emailCol = new JFXTreeTableColumn<>("Email");
    emailCol.setPrefWidth(80);
    emailCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<TableEmployee, String> param) -> {
          if (emailCol.validateValue(param)) {
            return param.getValue().getValue().getEmail();
          } else {
            return emailCol.getComputedValue(param);
          }
        });
    accessLevelCol = new JFXTreeTableColumn<>("Access Level");
    accessLevelCol.setPrefWidth(80);
    accessLevelCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<TableEmployee, String> param) -> {
          if (accessLevelCol.validateValue(param)) {
            return param.getValue().getValue().getAccessLevel();
          } else {
            return accessLevelCol.getComputedValue(param);
          }
        });
    primaryWorkspaceCol = new JFXTreeTableColumn<>("Primary Workspace");
    primaryWorkspaceCol.setPrefWidth(80);
    primaryWorkspaceCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<TableEmployee, String> param) -> {
          if (primaryWorkspaceCol.validateValue(param)) {
            return param.getValue().getValue().getPrimaryWorkspace();
          } else {
            return primaryWorkspaceCol.getComputedValue(param);
          }
        });
    saltCol = new JFXTreeTableColumn<>("Salt");
    saltCol.setPrefWidth(80);
    saltCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<TableEmployee, String> param) -> {
          if (saltCol.validateValue(param)) {
            return param.getValue().getValue().getSalt();
          } else {
            return saltCol.getComputedValue(param);
          }
        });

    firstnameCol.setCellFactory(
        (TreeTableColumn<TableEmployee, String> param) ->
            new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
    firstnameCol.setOnEditCommit(
        (TreeTableColumn.CellEditEvent<TableEmployee, String> t) -> {
          t.getTreeTableView()
              .getTreeItem(t.getTreeTablePosition().getRow())
              .getValue()
              .getFirstName()
              .set(t.getNewValue());
          try {
            DataOperations.update(
                new Employee(
                    t.getTreeTableView()
                        .getTreeItem(t.getTreeTablePosition().getRow())
                        .getValue()));
          } catch (SQLException throwables) {
            throwables.printStackTrace();
          }
        });
    lastnameCol.setCellFactory(
        (TreeTableColumn<TableEmployee, String> param) ->
            new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
    lastnameCol.setOnEditCommit(
        (TreeTableColumn.CellEditEvent<TableEmployee, String> t) -> {
          t.getTreeTableView()
              .getTreeItem(t.getTreeTablePosition().getRow())
              .getValue()
              .getLastName()
              .set(t.getNewValue());
          try {
            DataOperations.update(
                new Employee(
                    t.getTreeTableView()
                        .getTreeItem(t.getTreeTablePosition().getRow())
                        .getValue()));
          } catch (SQLException throwables) {
            throwables.printStackTrace();
          }
        });
    employeeIDCol.setCellFactory(
        (TreeTableColumn<TableEmployee, String> param) ->
            new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
    employeeIDCol.setOnEditCommit(
        (TreeTableColumn.CellEditEvent<TableEmployee, String> t) -> {
          t.getTreeTableView()
              .getTreeItem(t.getTreeTablePosition().getRow())
              .getValue()
              .getEmployeeID()
              .set(t.getNewValue());
          try {
            DataOperations.update(
                new Employee(
                    t.getTreeTableView()
                        .getTreeItem(t.getTreeTablePosition().getRow())
                        .getValue()));
          } catch (SQLException throwables) {
            throwables.printStackTrace();
          }
        });
    passwordCol.setCellFactory(
        (TreeTableColumn<TableEmployee, String> param) ->
            new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
    passwordCol.setOnEditCommit(
        (TreeTableColumn.CellEditEvent<TableEmployee, String> t) -> {
          t.getTreeTableView()
              .getTreeItem(t.getTreeTablePosition().getRow())
              .getValue()
              .getPassword()
              .set(t.getNewValue());
          try {
            DataOperations.update(
                new Employee(
                    t.getTreeTableView()
                        .getTreeItem(t.getTreeTablePosition().getRow())
                        .getValue()));
          } catch (SQLException throwables) {
            throwables.printStackTrace();
          }
        });
    emailCol.setCellFactory(
        (TreeTableColumn<TableEmployee, String> param) ->
            new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
    emailCol.setOnEditCommit(
        (TreeTableColumn.CellEditEvent<TableEmployee, String> t) -> {
          t.getTreeTableView()
              .getTreeItem(t.getTreeTablePosition().getRow())
              .getValue()
              .getEmail()
              .set(t.getNewValue());
          try {
            DataOperations.update(
                new Employee(
                    t.getTreeTableView()
                        .getTreeItem(t.getTreeTablePosition().getRow())
                        .getValue()));
          } catch (SQLException throwables) {
            throwables.printStackTrace();
          }
        });
    accessLevelCol.setCellFactory(
        (TreeTableColumn<TableEmployee, String> param) ->
            new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
    accessLevelCol.setOnEditCommit(
        (TreeTableColumn.CellEditEvent<TableEmployee, String> t) -> {
          t.getTreeTableView()
              .getTreeItem(t.getTreeTablePosition().getRow())
              .getValue()
              .getAccessLevel()
              .set(t.getNewValue());
          try {
            DataOperations.update(
                new Employee(
                    t.getTreeTableView()
                        .getTreeItem(t.getTreeTablePosition().getRow())
                        .getValue()));
          } catch (SQLException throwables) {
            throwables.printStackTrace();
          }
        });
    primaryWorkspaceCol.setCellFactory(
        (TreeTableColumn<TableEmployee, String> param) ->
            new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
    primaryWorkspaceCol.setOnEditCommit(
        (TreeTableColumn.CellEditEvent<TableEmployee, String> t) -> {
          t.getTreeTableView()
              .getTreeItem(t.getTreeTablePosition().getRow())
              .getValue()
              .getPrimaryWorkspace()
              .set(t.getNewValue());
          try {
            DataOperations.update(
                new Employee(
                    t.getTreeTableView()
                        .getTreeItem(t.getTreeTablePosition().getRow())
                        .getValue()));
          } catch (SQLException throwables) {
            throwables.printStackTrace();
          }
        });
    saltCol.setCellFactory(
        (TreeTableColumn<TableEmployee, String> param) ->
            new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
    saltCol.setOnEditCommit(
        (TreeTableColumn.CellEditEvent<TableEmployee, String> t) -> {
          t.getTreeTableView()
              .getTreeItem(t.getTreeTablePosition().getRow())
              .getValue()
              .getSalt()
              .set(t.getNewValue());
          try {
            DataOperations.update(
                new Employee(
                    t.getTreeTableView()
                        .getTreeItem(t.getTreeTablePosition().getRow())
                        .getValue()));
          } catch (SQLException throwables) {
            throwables.printStackTrace();
          }
        });

    treeTable.setShowRoot(false);
    treeTable.setEditable(true);
    treeTable
        .getColumns()
        .setAll(
            firstnameCol,
            lastnameCol,
            employeeIDCol,
            passwordCol,
            emailCol,
            accessLevelCol,
            primaryWorkspaceCol,
            saltCol);
    setList();
    setColumns();
    Platform.runLater(
        () -> {
          treeTable.maxHeightProperty().bind(treeTable.getScene().heightProperty());
        });
  }

  @Override
  public void loadNavigationBar() {
    parent.animateRightColumnWidth(30);
  }

  private void exportToCSV() {
    try {
      DataOperations.DBtoCSV("NODE");
      DataOperations.DBtoCSV("EDGE");
      DataOperations.DBtoCSV("EMPLOYEE");
      DataOperations.DBtoCSV("SERVICE");
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  private void expandTable(ActionEvent e) {
    if (!expanded) {
      parent.animateRightColumnWidth(800);
      expandIcon.setGlyphName("ANGLE_DOUBLE_RIGHT");
    } else {
      parent.animateRightColumnWidth(30);
      expandIcon.setGlyphName("ANGLE_DOUBLE_LEFT");
    }
    expanded = !expanded;
  }

  public void setColumns() {
    ObservableList<TableEmployee> tn = FXCollections.observableArrayList();
    for (Employee employee : employees) {
      tn.add(new TableEmployee(employee));
    }
    final TreeItem<TableEmployee> root =
        new RecursiveTreeItem<>(tn, RecursiveTreeObject::getChildren);

    treeTable.setRoot(root);
  }

  public void setList() {
    // get list of nodes from database
    try {
      this.employees = DataOperations.getListOfEmployeeFromDB();
    } catch (Exception e) {

    }
  }
}
