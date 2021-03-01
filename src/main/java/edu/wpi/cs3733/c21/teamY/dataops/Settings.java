package edu.wpi.cs3733.c21.teamY.dataops;

public class Settings {

  private static Settings settings = new Settings();
  private String currentUsername;
  private String currentPermissions;

  private Settings() {
    this.currentUsername = null;
    this.currentPermissions = null;
  }

  public String getCurrentUsername() {
    return currentUsername;
  }

  public String getCurrentPermissions() {
    return currentPermissions;
  }

  public static Settings getSettings() {
    return settings;
  }

  public void loginSuccess(String username, String permissions) {
    this.currentUsername = username;
    this.currentPermissions = permissions;
  }

  public void logout() {
    this.currentUsername = null;
    this.currentPermissions = null;
  }
}
