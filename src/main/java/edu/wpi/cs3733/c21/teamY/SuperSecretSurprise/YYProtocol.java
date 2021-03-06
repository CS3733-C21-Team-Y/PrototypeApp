package edu.wpi.cs3733.c21.teamY.SuperSecretSurprise;

import edu.wpi.cs3733.c21.teamY.algorithms.AlgorithmCalls;
import edu.wpi.cs3733.c21.teamY.entity.Graph;
import edu.wpi.cs3733.c21.teamY.entity.Node;
import java.util.ArrayList;

public class YYProtocol {

  private static final int WAITING = 0;
  private static final int SENDNODES = 1;
  private static final int VERIFY = 2;

  private Node startNode;
  private Node endNode;
  private Graph graph;
  private int lengthOfList;

  private int state = WAITING;

  private ArrayList<Node> nodes = new ArrayList<>();

  public String processInput(String theInput, Node start, Node end, Graph graph) {
    String theOutput = null;
    this.startNode = start;
    this.endNode = end;
    this.graph = graph;
    // this is not as bad as it looks, its a state machine that sends a prompt and waits for a
    // response

    //            if (theInput.equalsIgnoreCase("Who's there?")) {
    //                theOutput = clues[currentJoke];
    //                state = SENTCLUE;
    //            } else {
    //                theOutput = "You're supposed to say \"Who's there?\"! " + "Try again. Knock!
    // Knock!";
    //            }

    switch (state) {
      case (WAITING):
        theOutput = "Ready to send?";
        state = SENDNODES;
        break;
      case (SENDNODES):
        if (theInput.equalsIgnoreCase("yes")) {
          System.out.println("getting list");
          theOutput = nodesToSendable(); // send list of nodes in format below
          state = VERIFY;
        } else {
          state = WAITING;
        }
        break;
      case (VERIFY):
        if (theInput.equalsIgnoreCase("Received nodes:" + lengthOfList)) {
          theOutput = "Verified, bye";
          state = WAITING;
        } else {
          theOutput = "Incorrect, try again";
          state = WAITING;
        }
        break;
    }

    System.out.println(theOutput);

    return theOutput;
  }
  /*
   * protocol
   * WAITING
   * s:ready to send?
   * SENDNODES
   * c:yes
   * S:~list of node~
   * VERIFY
   * c:received nodes:~#of nodes~
   * s:verified, bye
   * WAITING
   *
   *
   * [{x.x,y.y,floor}={x.x,y.y,floor}=...
   *
   *
   *
   * */

  private String nodesToSendable() {
    // gets list of nodes in the path
    nodes = AlgorithmCalls.aStar(graph, startNode.nodeID, endNode.nodeID, "STAI");
    this.lengthOfList = nodes.size();
    StringBuffer sendable = new StringBuffer();
    sendable.append("[");
    for (Node node : nodes) {
      sendable.append(
          "={" + node.getXcoord() + "," + node.getYcoord() + "," + node.getFloor() + "}");
    }
    sendable.append("]");
    // deletes leading '='
    sendable.deleteCharAt(1);
//    System.out.println("the out is:" + sendable.toString());
    return sendable.toString();
  }
}
