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
import edu.wpi.cs3733.c21.teamY.entity.Node;
import edu.wpi.cs3733.c21.teamY.entity.TableNodes;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;

public class EditNodeTableController extends SubPage {

  @FXML public JFXTreeTableView<TableNodes> treeTable;
  @FXML public JFXButton expandBtn;
  @FXML public FontAwesomeIconView expandIcon;
  @FXML public JFXButton employeeTableBtn;
  @FXML public JFXButton nodeTableBtn;
  @FXML public JFXButton exportBtn;
  @FXML public JFXButton covidFormBtn;

  public JFXTreeTableColumn<TableNodes, String> nodeIDCol;
  public JFXTreeTableColumn<TableNodes, String> nodeTypeCol;
  public JFXTreeTableColumn<TableNodes, String> xcoordCol;
  public JFXTreeTableColumn<TableNodes, String> ycoordCol;
  public JFXTreeTableColumn<TableNodes, String> floorCol;
  public JFXTreeTableColumn<TableNodes, String> buildingCol;
  public JFXTreeTableColumn<TableNodes, String> roomCol;
  public JFXTreeTableColumn<TableNodes, String> longNameCol;
  public JFXTreeTableColumn<TableNodes, String> shortNameCol;

  private boolean expanded = false;

  private ArrayList<Node> nodes = new ArrayList<Node>();

  public EditNodeTableController() {}

  public void initialize() {
    //    treeTable.setPrefHeight(300);
    treeTable.setFixedCellSize(30);
    expandBtn.setOnAction(e -> expandTable());
    expandBtn.setCursor(Cursor.HAND);
    employeeTableBtn.setOnAction(e -> parent.loadRightSubPage("EditEmployeeTable.fxml"));
    employeeTableBtn.setCursor(Cursor.HAND);
    nodeTableBtn.setOnAction(e -> parent.loadRightSubPage("EditNodeTable.fxml"));
    nodeTableBtn.setCursor(Cursor.HAND);
    exportBtn.setOnAction(e -> exportToCSV());
    exportBtn.setCursor(Cursor.HAND);

    // tree table stuff

    nodeIDCol = new JFXTreeTableColumn<>("Node ID");
    nodeIDCol.setPrefWidth(80);
    nodeIDCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<TableNodes, String> param) -> {
          if (nodeIDCol.validateValue(param)) {
            return param.getValue().getValue().getNodeID();
          } else {
            return nodeIDCol.getComputedValue(param);
          }
        });
    nodeTypeCol = new JFXTreeTableColumn<>("Node Type");
    nodeTypeCol.setPrefWidth(80);
    nodeTypeCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<TableNodes, String> param) -> {
          if (nodeTypeCol.validateValue(param)) {
            return param.getValue().getValue().getNodeType();
          } else {
            return nodeTypeCol.getComputedValue(param);
          }
        });
    xcoordCol = new JFXTreeTableColumn<>("X Coord");
    xcoordCol.setPrefWidth(80);
    xcoordCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<TableNodes, String> param) -> {
          if (xcoordCol.validateValue(param)) {
            return param.getValue().getValue().getXcoord();
          } else {
            return xcoordCol.getComputedValue(param);
          }
        });
    ycoordCol = new JFXTreeTableColumn<>("Y Coord");
    ycoordCol.setPrefWidth(80);
    ycoordCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<TableNodes, String> param) -> {
          if (ycoordCol.validateValue(param)) {
            return param.getValue().getValue().getYcoord();
          } else {
            return ycoordCol.getComputedValue(param);
          }
        });
    floorCol = new JFXTreeTableColumn<>("Floor");
    floorCol.setPrefWidth(80);
    floorCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<TableNodes, String> param) -> {
          if (floorCol.validateValue(param)) {
            return param.getValue().getValue().getFloor();
          } else {
            return floorCol.getComputedValue(param);
          }
        });
    buildingCol = new JFXTreeTableColumn<>("Building");
    buildingCol.setPrefWidth(80);
    buildingCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<TableNodes, String> param) -> {
          if (buildingCol.validateValue(param)) {
            return param.getValue().getValue().getBuilding();
          } else {
            return buildingCol.getComputedValue(param);
          }
        });
    roomCol = new JFXTreeTableColumn<>("Room");
    roomCol.setPrefWidth(80);
    roomCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<TableNodes, String> param) -> {
          if (roomCol.validateValue(param)) {
            return param.getValue().getValue().getRoom();
          } else {
            return roomCol.getComputedValue(param);
          }
        });
    longNameCol = new JFXTreeTableColumn<>("Long Name");
    longNameCol.setPrefWidth(80);
    longNameCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<TableNodes, String> param) -> {
          if (longNameCol.validateValue(param)) {
            return param.getValue().getValue().getLongName();
          } else {
            return longNameCol.getComputedValue(param);
          }
        });
    shortNameCol = new JFXTreeTableColumn<>("Short Name");
    shortNameCol.setPrefWidth(80);
    shortNameCol.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<TableNodes, String> param) -> {
          if (shortNameCol.validateValue(param)) {
            return param.getValue().getValue().getShortName();
          } else {
            return shortNameCol.getComputedValue(param);
          }
        });

    nodeIDCol.setCellFactory(
        (TreeTableColumn<TableNodes, String> param) ->
            new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
    nodeIDCol.setOnEditCommit(
        (TreeTableColumn.CellEditEvent<TableNodes, String> t) -> {
          try {
            DataOperations.delete(
                t.getTreeTableView()
                    .getTreeItem(t.getTreeTablePosition().getRow())
                    .getValue()
                    .getNodeID()
                    .getValue());
          } catch (SQLException throwables) {
            throwables.printStackTrace();
          }
          t.getTreeTableView()
              .getTreeItem(t.getTreeTablePosition().getRow())
              .getValue()
              .getNodeID()
              .set(t.getNewValue());
          try {
            DataOperations.insert(
                new Node(
                    t.getTreeTableView()
                        .getTreeItem(t.getTreeTablePosition().getRow())
                        .getValue()));
          } catch (Exception throwables) {
            throwables.printStackTrace();
          }
        });
    nodeTypeCol.setCellFactory(
        (TreeTableColumn<TableNodes, String> param) ->
            new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
    nodeTypeCol.setOnEditCommit(
        (TreeTableColumn.CellEditEvent<TableNodes, String> t) -> {
          t.getTreeTableView()
              .getTreeItem(t.getTreeTablePosition().getRow())
              .getValue()
              .getNodeType()
              .set(t.getNewValue());
          try {
            DataOperations.update(
                new Node(
                    t.getTreeTableView()
                        .getTreeItem(t.getTreeTablePosition().getRow())
                        .getValue()));
          } catch (SQLException throwables) {
            throwables.printStackTrace();
          }
        });
    xcoordCol.setCellFactory(
        (TreeTableColumn<TableNodes, String> param) ->
            new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
    xcoordCol.setOnEditCommit(
        (TreeTableColumn.CellEditEvent<TableNodes, String> t) -> {
          t.getTreeTableView()
              .getTreeItem(t.getTreeTablePosition().getRow())
              .getValue()
              .getXcoord()
              .set(t.getNewValue());
          try {
            DataOperations.update(
                new Node(
                    t.getTreeTableView()
                        .getTreeItem(t.getTreeTablePosition().getRow())
                        .getValue()));
          } catch (SQLException throwables) {
            throwables.printStackTrace();
          }
        });
    ycoordCol.setCellFactory(
        (TreeTableColumn<TableNodes, String> param) ->
            new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
    ycoordCol.setOnEditCommit(
        (TreeTableColumn.CellEditEvent<TableNodes, String> t) -> {
          t.getTreeTableView()
              .getTreeItem(t.getTreeTablePosition().getRow())
              .getValue()
              .getYcoord()
              .set(t.getNewValue());
          try {
            DataOperations.update(
                new Node(
                    t.getTreeTableView()
                        .getTreeItem(t.getTreeTablePosition().getRow())
                        .getValue()));
          } catch (SQLException throwables) {
            throwables.printStackTrace();
          }
        });
    floorCol.setCellFactory(
        (TreeTableColumn<TableNodes, String> param) ->
            new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
    floorCol.setOnEditCommit(
        (TreeTableColumn.CellEditEvent<TableNodes, String> t) -> {
          t.getTreeTableView()
              .getTreeItem(t.getTreeTablePosition().getRow())
              .getValue()
              .getFloor()
              .set(t.getNewValue());
          try {
            DataOperations.update(
                new Node(
                    t.getTreeTableView()
                        .getTreeItem(t.getTreeTablePosition().getRow())
                        .getValue()));
          } catch (SQLException throwables) {
            throwables.printStackTrace();
          }
        });
    buildingCol.setCellFactory(
        (TreeTableColumn<TableNodes, String> param) ->
            new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
    buildingCol.setOnEditCommit(
        (TreeTableColumn.CellEditEvent<TableNodes, String> t) -> {
          t.getTreeTableView()
              .getTreeItem(t.getTreeTablePosition().getRow())
              .getValue()
              .getBuilding()
              .set(t.getNewValue());
          try {
            DataOperations.update(
                new Node(
                    t.getTreeTableView()
                        .getTreeItem(t.getTreeTablePosition().getRow())
                        .getValue()));
          } catch (SQLException throwables) {
            throwables.printStackTrace();
          }
        });
    roomCol.setCellFactory(
        (TreeTableColumn<TableNodes, String> param) ->
            new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
    roomCol.setOnEditCommit(
        (TreeTableColumn.CellEditEvent<TableNodes, String> t) -> {
          t.getTreeTableView()
              .getTreeItem(t.getTreeTablePosition().getRow())
              .getValue()
              .getRoom()
              .set(t.getNewValue());
          try {
            DataOperations.update(
                new Node(
                    t.getTreeTableView()
                        .getTreeItem(t.getTreeTablePosition().getRow())
                        .getValue()));
          } catch (SQLException throwables) {
            throwables.printStackTrace();
          }
        });
    longNameCol.setCellFactory(
        (TreeTableColumn<TableNodes, String> param) ->
            new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
    longNameCol.setOnEditCommit(
        (TreeTableColumn.CellEditEvent<TableNodes, String> t) -> {
          t.getTreeTableView()
              .getTreeItem(t.getTreeTablePosition().getRow())
              .getValue()
              .getLongName()
              .set(t.getNewValue());
          try {
            DataOperations.update(
                new Node(
                    t.getTreeTableView()
                        .getTreeItem(t.getTreeTablePosition().getRow())
                        .getValue()));
          } catch (SQLException throwables) {
            throwables.printStackTrace();
          }
        });
    shortNameCol.setCellFactory(
        (TreeTableColumn<TableNodes, String> param) ->
            new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
    shortNameCol.setOnEditCommit(
        (TreeTableColumn.CellEditEvent<TableNodes, String> t) -> {
          t.getTreeTableView()
              .getTreeItem(t.getTreeTablePosition().getRow())
              .getValue()
              .getShortName()
              .set(t.getNewValue());
          try {
            DataOperations.update(
                new Node(
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
            nodeIDCol,
            nodeTypeCol,
            xcoordCol,
            ycoordCol,
            floorCol,
            buildingCol,
            roomCol,
            longNameCol,
            shortNameCol);

    setList();
    setColumns();
    Platform.runLater(
        () -> {
          treeTable.maxHeightProperty().bind(treeTable.getScene().heightProperty());
          if (expanded != parent.tableExpanded) {
            expandTable();
          }
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

  private void expandTable() {
    if (!expanded) {
      parent.animateRightColumnWidth(800);
      expandIcon.setGlyphName("ANGLE_DOUBLE_RIGHT");
    } else {
      parent.animateRightColumnWidth(30);
      expandIcon.setGlyphName("ANGLE_DOUBLE_LEFT");
    }
    expanded = !expanded;
    parent.tableExpanded = expanded;
  }

  public void setColumns() {
    ObservableList<TableNodes> tn = FXCollections.observableArrayList();
    for (Node node : nodes) {
      tn.add(new TableNodes(node));
    }
    final TreeItem<TableNodes> root = new RecursiveTreeItem<>(tn, RecursiveTreeObject::getChildren);

    treeTable.setRoot(root);
  }

  public void setList() {
    // get list of nodes from database
    try {
      this.nodes = DataOperations.getListOfNodes();
    } catch (Exception e) {

    }
  }

  public void selectRow(String nodeID) {
    int i = 0;
    while (nodeIDCol.getCellData(i) != null) {
      if (nodeIDCol.getCellData(i).equals(nodeID)) {
        treeTable.getSelectionModel().select(i);
        treeTable.scrollTo(i);
        return;
      }
      i++;
    }
  }

  public void deselectRow(String nodeID) {
    int i = 0;
    while (nodeIDCol.getCellData(i) != null) {
      if (nodeIDCol.getCellData(i).equals(nodeID)) {
        treeTable.getSelectionModel().select(i);
        return;
      }
      i++;
    }
  }

  public void clearSelection() {
    treeTable.getSelectionModel().clearSelection();
  }

  public EditNodeTableController getEditNodeTableController() {
    return this;
  }
}
