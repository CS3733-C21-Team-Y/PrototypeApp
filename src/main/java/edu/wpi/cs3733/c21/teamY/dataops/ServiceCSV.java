package edu.wpi.cs3733.c21.teamY.dataops;

import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ServiceCSV {
  public static String serviceCSVpath = "src/main/resources/edu/wpi/cs3733/c21/teamY/Services.csv";
  public static BufferedReader brService;
  public static BufferedWriter bwService;
  public static final String splitBy = ",";

  /** load the bufferReader for service */
  static {
    try {
      System.out.println("Working Directory = " + System.getProperty("user.dir"));
      // parsing a CSV file into BufferedReader class constructor

      try {
        brService = new BufferedReader(new FileReader(serviceCSVpath));
        System.out.println("Service BufferedReader initialized successful!");
      } catch (FileNotFoundException e) {
        System.out.println("Service BufferedReader initialized failed!");

        e.printStackTrace();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  /** load the bufferWriterr for service */
  static {
    try {
      bwService = new BufferedWriter(new FileWriter(serviceCSVpath, true));
      System.out.println("Service BufferedWriter initialized successful!");
    } catch (FileNotFoundException e) {
      System.out.println("Service BufferedWriter initialized failed!");

      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * close the writer
   *
   * @param bw the bufferedWriter
   */
  public static void closeWriter(BufferedWriter bw) {
    try {
      bw.flush();
      bw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * close the reader
   *
   * @param br the bufferedReader
   */
  public static void closeReader(BufferedReader br) {
    try {
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * save the service to CSV
   *
   * @param service a service a be saved
   * @return true if write successfully, false otherwise
   */
  @Deprecated
  public static boolean saveServiceToCSV(Service service) {
    boolean hasBeenWritten = false;

    try {
      bwService.write(service.toString());
      System.out.println(service.toString());
      bwService.newLine();
      hasBeenWritten = true;
      bwService.flush();
      // bwService.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println("service Write successfully");
    return hasBeenWritten;
  }

  /**
   * load the CSV file to the DB
   *
   * @throws IOException
   * @throws SQLException
   */
  public static void loadCSVtoDB() throws IOException, SQLException {
    boolean loadSuccess = false;
    String line = "";
    Connection connection = null;
    connection = JDBCUtils.getConn();
    System.out.println("start loading service CSV to database");
    String insert = "insert into ADMIN.SERVICE values(?,?,?,?,?,?,?,?)";
    PreparedStatement preparedStatement = connection.prepareStatement(insert);
    brService.readLine(); // Reads first line to clear the attributes line
    while ((line = brService.readLine()) != null) {
      String[] strService = line.split(splitBy);
      int serviceID = Integer.parseInt(strService[0]);
      String type = strService[1];
      String description = strService[2];
      String location = strService[3];
      String category = strService[4];
      String urgency = strService[5];
      String date = strService[6];
      int status = Integer.parseInt(strService[7]);
      Service service =
          new Service(serviceID, type, description, location, category, urgency, date, status);
      ServiceRequestDBops.preparedStatementInsert(service, connection, preparedStatement, insert);
    }
    System.out.println("Loading successful");
    JDBCUtils.close(preparedStatement, null, null, connection);
    closeReader(brService);
  }
}
