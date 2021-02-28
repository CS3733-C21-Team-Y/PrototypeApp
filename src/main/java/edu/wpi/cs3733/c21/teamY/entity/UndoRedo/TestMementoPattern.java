package edu.wpi.cs3733.c21.teamY.entity.UndoRedo;

import edu.wpi.cs3733.c21.teamY.entity.Edge;
import edu.wpi.cs3733.c21.teamY.entity.Node;

public class TestMementoPattern {
  public static void main(String[] args) {
    HistoryStack history = new HistoryStack();
    Document Document = new Document();
    Document.change(
        new Node(30.4, 40.5, "Floor 1", "nodeID1"), new Node(3.4, 4.5, "Floor 1", "nodeID1"));
    history.add(Document.save());

    Document.change(null, new Edge("Last_First", "nodeID3", "nodeID5"));
    history.add(Document.save());
    Document.change(new Node(40.5, 50.7, "Floor 1", "nodeID1"), null);

    Document.resume(history.getLastVersion());
    System.out.println(Document);

    Document.resume(history.getLastVersion());
    System.out.println(Document);
  }
}

/**
 * add node (prevChange = null, change = NODE)
 *
 * Delete node (prevChange = NODe, change = null)
 *
 * change node (prevChange = previousNode, change = Node)
 */
