package edu.wpi.cs3733.c21.teamY.dataops;

import edu.wpi.cs3733.c21.teamY.algorithms.AStarI;
import edu.wpi.cs3733.c21.teamY.algorithms.AlgoContext;

public class Settings {

  private static Settings settings = new Settings();
  private String currentUsername;
  private int currentPermissions;
  private AlgoContext AlgorithmSelection = new AlgoContext();

  private Settings() {
    this.currentUsername = "guest";
    this.currentPermissions = 0;
    AlgorithmSelection.setContext(new AStarI());
  }

  public String getCurrentUsername() {
    return currentUsername;
  }

  public int getCurrentPermissions() {
    return currentPermissions;
  }

  public AlgoContext getAlgorithmSelection() {
    return AlgorithmSelection;
  }

  public void setAlgorithmSelection(AlgoContext algorithmSelection) {
    AlgorithmSelection = algorithmSelection;
  }

  public static Settings getSettings() {
    return settings;
  }

  public void loginSuccess(String username, int permissions) {
    this.currentUsername = username;
    this.currentPermissions = permissions;
  }

  public void logout() {
    this.currentUsername = null;
    this.currentPermissions = -1;
  }
}
