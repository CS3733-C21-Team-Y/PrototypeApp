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
          System.out.println("_getting list_");
          theOutput = nodesToSendable(); // send list of nodes in format below
          state = VERIFY;
        } else {
          state = WAITING;
        }
        break;
      case (VERIFY):
        if (theInput.equalsIgnoreCase(lengthOfList + "")) {
          theOutput = "Bye.";
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

    return sendable.toString();
  }

  //Removes intermediate nodes in a straight path
  private ArrayList<Node> removeStraight(ArrayList<Node> path){
    ArrayList<Node> newPath = (ArrayList<Node>) path.clone();
    for (int i = 0; i < newPath.size() - 2; i++) {
      double angle = AlgorithmCalls.directionOfPoint(path.get(i), path.get(i + 1), path.get(i + 2));
      if(angle==0){
        newPath.remove(i + 1);
        i--;
      }
    }
    return newPath;
  }

  private ArrayList<Node> scaleNodes(ArrayList<Node> path){
    ArrayList<Node> newPath = (ArrayList<Node>) path.clone();

    //Following scale factor converts to cm
    //Example distance is 145 on map to 96.015cm in CAD
    double scaleFactor=0.6621724; //This is based on the physical model we built

    for (int i = 0; i < newPath.size() - 1; i++) {
      newPath.get(i).xcoord = newPath.get(i).xcoord*scaleFactor;
      newPath.get(i).ycoord = newPath.get(i).ycoord*scaleFactor;
    }
    return newPath;
  }
}
