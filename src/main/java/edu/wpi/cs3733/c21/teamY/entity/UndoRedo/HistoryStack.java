package edu.wpi.cs3733.c21.teamY.entity.UndoRedo;

import java.util.LinkedList;

public class HistoryStack {
  LinkedList<Backup> backupStack = new LinkedList<>();

  public void add(Backup backup) {
    backupStack.add(backup);
  }

  public Backup getLastVersion() {
    return backupStack.pop();
  }
}
