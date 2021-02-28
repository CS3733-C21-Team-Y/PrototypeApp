package edu.wpi.cs3733.c21.teamY.entity.UndoRedo;

import edu.wpi.cs3733.c21.teamY.entity.Entity;

public class Document {
  private Entity content;
  private Entity prevContent;

  public Backup save() {
    return new Backup(prevContent, content);
  }

  public void resume(Backup backup) {
    content = backup.content;
    prevContent = backup.prevContent;
  }

  public void change(Entity prevChange, Entity change) {
    this.content = change;
    this.prevContent = prevChange;
  }

  @Override
  public String toString() {
    return "Document{"
        + "content= '"
        + content
        + "'\n"
        + "prevContent= '"
        + prevContent
        + '\''
        + '}';
  }
}
