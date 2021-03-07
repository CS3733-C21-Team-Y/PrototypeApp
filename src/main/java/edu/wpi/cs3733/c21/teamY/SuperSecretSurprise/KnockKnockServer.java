package edu.wpi.cs3733.c21.teamY.SuperSecretSurprise;

import edu.wpi.cs3733.c21.teamY.entity.ActiveGraph;
import edu.wpi.cs3733.c21.teamY.entity.Node;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class KnockKnockServer {

  public void runServer(String[] args, Node start, Node end) {
    ActiveGraph ag = new ActiveGraph();
    try {
      ag.initialize();
    } catch (Exception e) {
      e.printStackTrace();
    }

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
      outputLine = yyp.processInput(null, null, null, null);
      out.println(outputLine);

      // we wait in this until we get a response
      while ((inputLine = in.readLine()) != null) {
        System.out.println(inputLine);
        outputLine = yyp.processInput(inputLine, start, end, ag.getActiveGraph());
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
