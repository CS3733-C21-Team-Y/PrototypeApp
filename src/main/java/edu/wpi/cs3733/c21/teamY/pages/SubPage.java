package edu.wpi.cs3733.c21.teamY.pages;

abstract class SubPage {

  public SubPage() {}

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
