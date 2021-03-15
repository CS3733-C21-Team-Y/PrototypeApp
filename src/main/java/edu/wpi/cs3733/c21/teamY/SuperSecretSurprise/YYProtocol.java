package edu.wpi.cs3733.c21.teamY.SuperSecretSurprise;

import edu.wpi.cs3733.c21.teamY.algorithms.AlgorithmCalls;
import edu.wpi.cs3733.c21.teamY.entity.Node;
import java.util.ArrayList;

public class YYProtocol {

  private static final int WAITING = 0;
  private static final int SENDNODES = 1;
  private static final int VERIFY = 2;
  private static final int WAITINGFORBOT = 3;

  private ArrayList<Node> nodeList;
  private int lengthOfList;

  private int state = WAITING;

  public String processInput(String theInput, ArrayList<Node> nodeList) {
    String theOutput = null;
    this.nodeList = nodeList;
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
          System.out.println("_getting list_");
          theOutput = nodesToSendable(); // send list of nodes in format below
          state = VERIFY;
        } else {
          state = WAITING;
        }
        break;
      case (VERIFY):
        if (theInput.equalsIgnoreCase(lengthOfList + "")) {

          // change here so it waits for a path complete confirmation
          theOutput = "waiting for robot";
          state = WAITINGFORBOT;
        } else {
          theOutput = "Incorrect, Ready to send?";
          state = SENDNODES;
        }

        // =========================================================
        break;
      case (WAITINGFORBOT):
        if (theInput.equalsIgnoreCase("finished")) {
          theOutput = "Bye.";
          state = WAITING;
        } else {

        }
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

    // probably going to need to scale and size everything here

    // gets list of nodes in the path
    ArrayList<Node> send = nodeList;
    this.lengthOfList = send.size();
    StringBuffer sendable = new StringBuffer();
    sendable.append("[");
    for (Node node : send) {
      sendable.append(
          "={" + node.getXcoord() + "," + node.getYcoord() + "," + node.getFloor() + "}");
    }
    sendable.append("]");
    // deletes leading '='
    sendable.deleteCharAt(1);

    return sendable.toString();
  }

  // Removes intermediate nodes in a straight path
  public static ArrayList<Node> removeStraight(ArrayList<Node> path) {
    ArrayList<Node> newPath = (ArrayList<Node>) path.clone();
    for (int i = 0; i < newPath.size() - 2; i++) {
      double angle =
          AlgorithmCalls.directionOfPoint(newPath.get(i), newPath.get(i + 1), newPath.get(i + 2));
      if (Math.abs(angle) < 25) {
        newPath.remove(i + 1);
        i--;
      }
    }
    return newPath;
  }

  public static ArrayList<Node> scaleNodes(ArrayList<Node> path) {
    ArrayList<Node> newPath = (ArrayList<Node>) path.clone();

    // Following scale factor converts to cm
    // Example distance is 145 on map to 96.015cm in CAD
    double scaleFactor = 0.6621724; // This is based on the physical model we built

    for (int i = 0; i < path.size(); i++) {
      Node newNode = path.get(i);
      newNode.xcoord = path.get(i).xcoord * scaleFactor;
      newNode.ycoord = path.get(i).ycoord * scaleFactor;
      newPath.add(newNode);
    }
    return newPath;
  }
}
