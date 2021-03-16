package edu.wpi.cs3733.c21.teamY.pages;

import javafx.application.Platform;

public class SubsetEmployeeTableController extends EditEmployeeTableController {

  boolean expanded = false;

  public SubsetEmployeeTableController() {}

  public void initialize() {
    treeTable.setFixedCellSize(30);
    expandBtn.setOnAction(e -> expandTable());
    employeeTableBtn.setOnAction(e -> parent.loadRightSubPage("EditEmployeeTable.fxml"));
    nodeTableBtn.setOnAction(e -> parent.loadRightSubPage("EditNodeTable.fxml"));
    covidFormBtn.setOnAction(e -> parent.loadRightSubPage("EmployeeSubsetTable.fxml"));
    exportBtn.setOnAction(e -> exportToCSV());

    populateColumns();

    treeTable.setShowRoot(false);
    treeTable.setEditable(true);
    subsetColumns();
    setList();
    setColumns();
    Platform.runLater(
        () -> {
          treeTable.maxHeightProperty().bind(treeTable.getScene().heightProperty());
          if (parent.tableExpanded != expanded) {
            expandTable();
          }
        });
  }
}
