package edu.wpi.cs3733.c21.teamY.pages;

import edu.wpi.cs3733.c21.teamY.dataops.JDBCUtils;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.commons.lang3.RandomStringUtils;

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

  public String generateIDString() {
    String id = RandomStringUtils.random(4);
    return id;
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
