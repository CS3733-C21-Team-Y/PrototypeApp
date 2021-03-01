package edu.wpi.cs3733.c21.teamY.pages;

public class CenterPage {
  public MainPageController getParent() {
    return parent;
  }

  public void setParent(MainPageController parent) {
    this.parent = parent;
  }

  protected MainPageController parent;
}
