package edu.wpi.cs3733.c21.teamY.pages;

public class StageInformation {

  private String centerPaneFXML;
  private String rightPaneFXML;

  public StageInformation(String centerPaneFXML, String rightPaneFXML) {
    this.centerPaneFXML = centerPaneFXML;
    this.rightPaneFXML = rightPaneFXML;
  }

  public String getCenterPaneFXML() {
    return centerPaneFXML;
  }

  public void setCenterPaneFXML(String centerPaneFXML) {
    this.centerPaneFXML = centerPaneFXML;
  }

  public String getRightPaneFXML() {
    return rightPaneFXML;
  }

  public void setRightPaneFXML(String rightPaneFXML) {
    this.rightPaneFXML = rightPaneFXML;
  }
}
