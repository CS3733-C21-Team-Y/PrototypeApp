package edu.wpi.cs3733.c21.teamY.SuperSecretSurprise;

import edu.wpi.cs3733.c21.teamY.entity.Node;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class KnockKnockServer implements Runnable {
  private Thread thread;
  private String threadName;
  private ArrayList<Node> nodeList;

  public KnockKnockServer(String threadName, ArrayList<Node> nodeList) {
    this.threadName = threadName;
    this.nodeList = nodeList;
    System.out.println("Creating Thread: " + this.threadName);
  }

  @Override
  public void run() {
    System.out.println("Running Thread: " + this.threadName);

    try {
      runServer(new String[] {"4444"}, this.nodeList);
    } catch (Exception e) {
      System.out.println("Thread Interrupted: " + this.threadName);
    }

    System.out.println("Exiting Thread: " + this.threadName);
  }

  public void start() {
    System.out.println("Starting Thread: " + this.threadName);
    if (thread == null) {
      thread = new Thread(this, this.threadName);
      thread.start();
    }
  }

  public void runServer(String[] args, ArrayList<Node> nodeList) {

    System.out.println("started");

    if (args.length != 1) {
      System.err.println("Usage: java KnockKnockServer <port number>");
      System.exit(1);
    }

    int portNumber = Integer.parseInt(args[0]);

    try (ServerSocket serverSocket = new ServerSocket(portNumber);
        Socket clientSocket = serverSocket.accept();
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in =
            new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); ) {

      String inputLine, outputLine;

      // Initiate conversation with client
      //            KnockKnockProtocol kkp = new KnockKnockProtocol();
      YYProtocol yyp = new YYProtocol();
      outputLine = yyp.processInput(null, null);
      out.println(outputLine);

      // we wait in this until we get a response
      while ((inputLine = in.readLine()) != null) {
        System.out.println(inputLine);
        outputLine = yyp.processInput(inputLine, nodeList);
        out.println(outputLine);
        if (outputLine.equals("Bye.")) break;
      }

    } catch (IOException e) {
      System.out.println(
          "Exception caught when trying to listen on port "
              + portNumber
              + " or listening for a connection");
      System.out.println(e.getMessage());
    }
  }
}
