package edu.wpi.cs3733.c21.teamY.dataops;

import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.sql.*;
import java.util.ArrayList;

public class ServiceRequestDBops {

  /**
   * @param service a service to be saved to DB
   * @throws ClassNotFoundException
   * @throws SQLException
   * @throws NoSuchFieldException
   * @throws InstantiationException
   * @throws IllegalAccessException
   */
  public static void saveService(Service service)
      throws ClassNotFoundException, SQLException, NoSuchFieldException, InstantiationException,
          IllegalAccessException {
    JDBCUtils.insert(8, service, "Service"); // save to database
    ServiceCSV.saveServiceToCSV(service); // save to CSV file
  }

  /**
   * @param serviceType type of service to be exported. Leave as empty string if ny preference type
   * @return a list of services
   * @throws SQLException
   */
  public static ArrayList<Service> exportService(String serviceType) throws SQLException {
    ArrayList<Service> services = new ArrayList<>();
    String string = "";
    if (serviceType.equals("")) {
      string = "select * from ADMIN.Service";
    } else {
      string = "select * from ADMIN.Service where type=" + serviceType;
    }
    Connection conn = JDBCUtils.getConn();
    Statement statement = conn.createStatement();
    ResultSet resultSet = statement.executeQuery(string);
    int serviceID;
    String type;
    String description;
    String location;
    String category;
    String urgency;
    String date;
    int status;
    while (resultSet.next()) {
      serviceID = resultSet.getInt(1);
      type = resultSet.getString(2);
      description = resultSet.getString(3);
      location = resultSet.getString(4);
      category = resultSet.getString(5);
      urgency = resultSet.getString(6);
      date = resultSet.getString(7);
      status = resultSet.getInt(8);
      Service service =
          new Service(serviceID, type, description, location, category, urgency, date, status);
      services.add(service);
    }
    resultSet.close();
    JDBCUtils.close(null, null, statement, conn);
    return services;
  }

  /**
   * remove a service from database
   *
   * @param ID service ID of the service to be removed
   * @return false if no such row exist, true delete successful
   * @throws SQLException
   */
  public static boolean removeService(int ID) throws SQLException {
    String string = "delete from ADMIN.Service where ADMIN.Service.serviceID=" + ID;
    Connection conn = JDBCUtils.getConn();
    Statement statement = conn.createStatement();
    int numRows = statement.executeUpdate(string);
    JDBCUtils.close(null, null, statement, conn);
    if (numRows == 0) {
      System.out.println("no rows have been deleted");
      return false;
    } else {
      System.out.println("delete successful");
      return true;
    }
  }

  /**
   * update a serice's status code
   *
   * @param service a service to be updated
   * @param status the new status
   * @return true if update successful, false if no row is updated
   * @throws SQLException
   */
  public static boolean updateServiceStatus(Service service, int status) throws SQLException {
    String string =
        "update ADMIN.SERVICE set status="
            + status
            + "where ADMIN.SERVICE.SERVICEID="
            + service.getServiceID();
    Connection conn = JDBCUtils.getConn();
    Statement statement = conn.createStatement();
    int rowsAffected = statement.executeUpdate(string);
    JDBCUtils.close(null, null, statement, conn);
    if (rowsAffected == 0) {
      System.out.println("update failed");
      return false;
    } else {
      System.out.println("update successful");
      return true;
    }
  }

  /**
   * @param service a service to be isnerted into DB
   * @param conn the connection to use
   * @param preparedStatement prepare statement
   * @param insert the string represent the SQL query
   * @return true if insert successful
   */
  public static boolean preparedStatementInsert(
      Service service, Connection conn, PreparedStatement preparedStatement, String insert) {

    boolean hasBeenUpdated = false;

    try {

      preparedStatement.setInt(1, service.getServiceID());
      preparedStatement.setString(2, service.getType());
      preparedStatement.setString(3, service.getDescription());
      preparedStatement.setString(4, service.getLocation());
      preparedStatement.setString(5, service.getCategory());
      preparedStatement.setString(6, service.getUrgency());
      preparedStatement.setString(7, service.getDate());
      preparedStatement.setInt(8, service.getStatus());
      preparedStatement.executeUpdate();
      hasBeenUpdated = true;

    } catch (SQLException throwables) {
      // System.out.println("connection get failed ");
      // throwables.printStackTrace();
    }
    return hasBeenUpdated;
  }

  /*    public static int getStatus(int ServiceID) throws SQLException {
      Connection connection=JDBCUtils.getConn();
      Statement statement=connection.createStatement();
      String string="select ADMIN.SERVICESSTATUS.STATUS from ADMIN.SERVICESSTATUS where SERVICEID="+ServiceID;
      ResultSet resultSet=statement.executeQuery(string);
      int status=0;
      while (resultSet.next()){
          status=resultSet.getInt(1);
      }
      return status;
  }*/
}
