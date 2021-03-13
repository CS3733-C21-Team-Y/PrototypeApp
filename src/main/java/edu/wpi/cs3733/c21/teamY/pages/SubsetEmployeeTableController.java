package edu.wpi.cs3733.c21.teamY.pages;

import javafx.application.Platform;

public class SubsetEmployeeTableController extends EditEmployeeTableController {

  public SubsetEmployeeTableController() {}

  public void initialize() {
    treeTable.setFixedCellSize(30);

    populateColumns();

    treeTable.setShowRoot(false);
    treeTable.setEditable(true);
    subsetColumns();
    setList();
    setColumns();
    Platform.runLater(
        () -> {
          treeTable.maxHeightProperty().bind(treeTable.getScene().heightProperty());
        });
  }
}
