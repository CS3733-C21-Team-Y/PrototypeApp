package edu.wpi.cs3733.c21.teamY.pages;

import edu.wpi.cs3733.c21.teamY.algorithms.AlgoContext;
import edu.wpi.cs3733.c21.teamY.entity.Service;

public class StageInformation {

  private String centerPaneFXML;
  private String rightPaneFXML;

  private double width;
  private double height;

  public AlgoContext getAlgorithmSelection() {
    return AlgorithmSelection;
  }

  public void setAlgorithmSelection(AlgoContext algorithmSelection) {
    AlgorithmSelection = algorithmSelection;
  }

  private AlgoContext AlgorithmSelection = new AlgoContext();

  private Service service;

  public StageInformation(String centerPaneFXML, String rightPaneFXML) {
    this.centerPaneFXML = centerPaneFXML;
    this.rightPaneFXML = rightPaneFXML;
    width = 1000;
    height = 600;
  }

  public StageInformation(
      String centerPaneFXML, String rightPaneFXML, double width, double height) {
    this.centerPaneFXML = centerPaneFXML;
    this.rightPaneFXML = rightPaneFXML;
    this.width = width;
    this.height = height;
  }

  public String getCenterPaneFXML() {
    return centerPaneFXML;
  }

  public void setCenterPaneFXML(String centerPaneFXML) {
    this.centerPaneFXML = centerPaneFXML;
  }

  public Service getService() {
    return service;
  }

  public void setService(Service service) {
    this.service = service;
  }

  public String getRightPaneFXML() {
    return rightPaneFXML;
  }

  public void setRightPaneFXML(String rightPaneFXML) {
    this.rightPaneFXML = rightPaneFXML;
  }

  public double getWidth() {
    return width;
  }

  public void setWidth(double width) {
    this.width = width;
  }

  public double getHeight() {
    return height;
  }

  public void setHeight(double height) {
    this.height = height;
  }
}
