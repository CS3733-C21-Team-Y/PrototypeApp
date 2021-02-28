package edu.wpi.cs3733.c21.teamY.entity.UndoRedo;

import edu.wpi.cs3733.c21.teamY.entity.Entity;

public class Backup {
  Entity content;
  Entity prevContent;

  public Backup(Entity prevContent, Entity content) {
    this.content = content;
    this.prevContent = prevContent;
  }
}
