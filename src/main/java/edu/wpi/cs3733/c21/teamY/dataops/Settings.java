package edu.wpi.cs3733.c21.teamY.dataops;

public class Settings {

  private static Settings settings = new Settings();
  private String currentUsername;
  private int currentPermissions;

  private Settings() {
    this.currentUsername = "guest";
    this.currentPermissions = 0;
  }

  public String getCurrentUsername() {
    return currentUsername;
  }

  public int getCurrentPermissions() {
    return currentPermissions;
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
