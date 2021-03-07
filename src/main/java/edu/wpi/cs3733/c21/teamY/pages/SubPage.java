package edu.wpi.cs3733.c21.teamY.pages;

import edu.wpi.cs3733.c21.teamY.dataops.JDBCUtils;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.sql.SQLException;
import java.util.ArrayList;

abstract class SubPage {

  public int IDCount;

  public SubPage() {
    try {
      ArrayList<Service> services = JDBCUtils.exportService("", "");
      IDCount = services.size();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  protected MainPageController parent;

  public MainPageController getParent() {
    return parent;
  }

  public void setParent(MainPageController parent) {
    this.parent = parent;
  }

  public void loadNavigationBar() {
    parent.setCenterColumnWidth(0);
  };

  public void drawByPlatform() {};
}
