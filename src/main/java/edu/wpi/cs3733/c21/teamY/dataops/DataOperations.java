package edu.wpi.cs3733.c21.teamY.dataops;

import edu.wpi.cs3733.c21.teamY.entity.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import org.apache.derby.iapi.sql.ResultSet;

public class DataOperations {

  public static void initDB() {
    JDBCUtils.initDB();
  }

  public static void initCSV() {
    CSV.initCSV();
  }

  public static Connection getConn() throws SQLException {
    return JDBCUtils.getConn();
  }

  public static void close(
      PreparedStatement ps, ResultSet rs, Statement stmt, Connection connection) {
    try {
      JDBCUtils.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void insert(int numArgs, Object object, String tableName)
      throws SQLException, IllegalAccessException {
    JDBCUtils.insert(numArgs, object, tableName);
  }

  public static void insert(Employee employee) throws SQLException {
    JDBCUtils.insert(employee);
  }

  public static void insert(Node node) throws SQLException, IllegalAccessException {
    JDBCUtils.insert(9, node, "Node");
  }

  public static void insertArrayListNode(ArrayList<Node> nodes)
      throws SQLException, IllegalAccessException {
    JDBCUtils.insertArrayListNode(nodes);
  }

  public static void insertArrayListEdge(ArrayList<Edge> edges)
      throws SQLException, IllegalAccessException {
    JDBCUtils.insertArrayListEdge(edges);
  }

  public static void fillTablesFromCSV() throws IllegalAccessException, SQLException, IOException {
    JDBCUtils.fillTablesFromCSV();
  }

  public static void update(Node node) throws SQLException {
    JDBCUtils.update(node);
  }

  public static void update(Edge edge) throws SQLException {
    JDBCUtils.update(edge);
  }

  public static void update(Employee employee) throws SQLException {
    JDBCUtils.update(employee);
  }

  public static void delete(String nodeID) throws SQLException {
    JDBCUtils.deleteNode(nodeID);
  }

  public static void deleteEdge(String edgeID) throws SQLException {
    JDBCUtils.deleteEdge(edgeID);
  }

  public static void deleteEmployee(Employee employee) throws SQLException {
    JDBCUtils.delete(employee);
  }

  public static void deleteEmployee(String employeeID) throws SQLException {
    JDBCUtils.deleteEmployee(employeeID);
  }

  public static void updateNodeCoordsOnly(String nodeID, double xcoord, double ycoord) {
    JDBCUtils.updateNodeCoordsOnly(nodeID, xcoord, ycoord);
  }

  public static void saveService(Service service) throws SQLException, IllegalAccessException {
    JDBCUtils.insert(service);
  }

  public static ArrayList<Service> exportService(String serviceType, String requester)
      throws SQLException {
    return JDBCUtils.exportService(serviceType, requester);
  }

  public static ArrayList<Service> exportSortedService(
      String serviceType, int serviceStatus, String serviceEmployee) throws SQLException {
    return JDBCUtils.exportService(serviceType, serviceStatus, serviceEmployee);
  }

  public static void removeService(String ID) throws SQLException {
    JDBCUtils.delete(ID);
  }

  public static void updateServiceStatus(Service service, int status) throws SQLException {
    JDBCUtils.updateServiceStatus(service, status);
  }

  public static void updateServiceAdditionalInfoOnly(String serviceID, String newInfo)
      throws SQLException {
    JDBCUtils.updateServiceAdditionalInfoOnly(serviceID, newInfo);
  }

  public static void updateServiceAssignedEmployee(Service service, String employee)
      throws SQLException {
    JDBCUtils.updateServiceAssignedEmployee(service, employee);
  }

  public static ArrayList<Edge> getEdgesCSV() throws IOException, IllegalAccessException {
    return CSV.getEdgesCSV();
  }

  public static ArrayList<Node> getNodesCSV() throws IOException, IllegalAccessException {
    return CSV.getNodesCSV();
  }

  public static void DBtoCSV(String mode) throws SQLException {
    CSV.DBtoCSV(mode);
  }

  public static ArrayList<Edge> getListOfEdge() throws SQLException {
    return CSV.getListOfEdge();
  }

  public static ArrayList<Node> getListOfNodes() throws SQLException {
    return CSV.getListOfNodes();
  }

  public static boolean setUserSettings(String username) throws SQLException {
    return JDBCUtils.findUser(username);
  }

  public static String findUserByEmail(String email) throws SQLException {
    return JDBCUtils.findUserByEmail(email);
  }

  public static ArrayList<Employee> getListOfEmployeeFromDB() throws SQLException {
    return JDBCUtils.exportListOfEmployee();
  }

  public static ArrayList<Employee> getStaffList() throws SQLException {
    return JDBCUtils.getStaffList();
  }

  public static void loadEmployeeCSV() throws IOException, SQLException {
    CSV.loadCSVtoDBEmployee();
  }

  public static boolean updateUserPassword(String newPassword, String email) throws SQLException {
    return JDBCUtils.updateUserPassword(newPassword, email);
  }

  public static boolean updateUserSalt(String email, String salt) throws SQLException {
    return JDBCUtils.updateUserSalt(email, salt);
  }

  public static int checkSurveyStatus(String userID) {
    return JDBCUtils.checkPatientStatus(userID);
  }

  public static boolean checkForCompletedCovidSurvey(String userID) {
    return JDBCUtils.checkForCompletedCovidSurvey(userID);
  }

  public static boolean assignEmpoyeeToService(String employeeID, String serviceID)
      throws SQLException {
    return JDBCUtils.assignEmployeeToRequest(employeeID, serviceID);
  }

  public static boolean createAccount(Employee employee) throws SQLException {
    return JDBCUtils.createUserAccount(employee);
  }

  public static boolean removeAccount(String employID) throws SQLException {
    return JDBCUtils.removeAccount(employID);
  }

  public static String findCarLocation(String ID) throws SQLException {
    return JDBCUtils.findCarLocation(ID);
  }

  public static boolean saveParkingSpot(String nodeID, String userID) throws SQLException {
    return JDBCUtils.saveParkingSpot(nodeID, userID);
  }

  public static boolean updateParkingSpot(String nodeID, String userID) throws SQLException {
    return JDBCUtils.updateParkingSpot(nodeID, userID);
  }

  public static String findUserSecurePassword(String username) {
    return JDBCUtils.findUserSecurePassword(username);
  }

  public static String findUserSalt(String username) {
    return JDBCUtils.findUserSalt(username);
  }

  public static String generateUniqueID(String type) {
    Random rng = new Random(System.currentTimeMillis());

    StringBuilder sb = new StringBuilder();
    int charType;
    int nextInt;
    for (int i = 0; i < 4; i++) {
      charType = rng.nextInt(3);
      nextInt = rng.nextInt(26);
      switch (charType) {
        case 0: // number
          int num = nextInt % 10;
          sb.append(num);
          break;
        case 1: // capital letter
          char c = (char) (nextInt + 65);
          sb.append(c);
          break;
        case 2: // lowercase letter
          char d = (char) (nextInt + 97);
          sb.append(d);
          break;
      }
    }

    String ID = type + '-' + sb.toString();

    if (type.equals("NODE") && JDBCUtils.nodeIDExists(ID)) {
      ID = generateUniqueID(type);
    }

    // need to check to see if this ID exists in the DB already
    else if (JDBCUtils.serviceIDExists(ID)) {
      ID = generateUniqueID(type);
    }

    return ID;
  }

  public static ArrayList<EmployeeClearanceInfo> getClearanceList() {
    return JDBCUtils.getClearanceList();
  }

  public static void markAsCleared(String employeeID) {
    JDBCUtils.markAsCleared(employeeID);
  }

  public static void markAsNotCleared(String employeeID) {
    JDBCUtils.markAsNotCleared(employeeID);
  }
}
