package edu.wpi.cs3733.c21.teamY.dataops;

import edu.wpi.cs3733.c21.teamY.entity.ActiveGraph;
import edu.wpi.cs3733.c21.teamY.entity.Edge;
import edu.wpi.cs3733.c21.teamY.entity.Employee;
import edu.wpi.cs3733.c21.teamY.entity.Node;
import edu.wpi.cs3733.c21.teamY.entity.Service;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import org.apache.derby.iapi.sql.ResultSet;

public class DataOperations {

  public static Connection getConn() throws SQLException {
    return JDBCUtils.getConn();
  }

  public static void close(
      PreparedStatement ps, ResultSet rs, Statement stmt, Connection connection) {
    JDBCUtils.close(ps, rs, stmt, connection);
  }

  public static void insert(int numArgs, Object object, String tableName)
      throws SQLException, IllegalAccessException {
    JDBCUtils.insert(numArgs, object, tableName);
  }

  public static void insert(Employee employee) throws SQLException {
    JDBCUtils.insert(employee);
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

  public static void deleteNode(String nodeID) throws SQLException {
    JDBCUtils.deleteNode(nodeID);
  }

  public static void deleteEdge(String edgeID) throws SQLException {
    JDBCUtils.deleteEdge(edgeID);
  }

  public static void deleteEmployee(Employee employee) throws SQLException {
    JDBCUtils.deleteEmployee(employee);
  }

  public static void deleteEmployee(String employeeID) throws SQLException {
    JDBCUtils.deleteEmployee(employeeID);
  }

  public static void updateNodeCoordsOnly(String nodeID, double xcoord, double ycoord) {
    JDBCUtils.updateNodeCoordsOnly(nodeID, xcoord, ycoord);
  }

  public static void saveService(Service service) throws SQLException, IllegalAccessException {
    JDBCUtils.saveService(service);
  }

  public static ArrayList<Service> exportService(String serviceType) throws SQLException {
    return JDBCUtils.exportService(serviceType);
  }

  public static void removeService(int ID) throws SQLException {
    JDBCUtils.removeService(ID);
  }

  public static void updateServiceStatus(Service service, int status) throws SQLException {
    JDBCUtils.updateServiceStatus(service, status);
  }

  public static ArrayList<Edge> getEdgesCSV() throws IOException, IllegalAccessException {
    return CSV.getEdgesCSV();
  }

  public static ArrayList<Node> getNodesCSV() throws IOException, IllegalAccessException {
    return CSV.getNodesCSV();
  }

  public static void BDtoCSV(String mode) throws SQLException {
    CSV.DBtoCSV(mode);
  }

  public static ArrayList<Edge> getListOfEdge() throws SQLException {
    return CSV.getListOfEdge();
  }

  public static ArrayList<Edge> getListOfEdgeNoStairs() throws SQLException {

    return CSV.getListOfEdge(ActiveGraph.FilterMapElements.NoStairs);
  }

  public static ArrayList<Node> getListOfNodes() throws SQLException {
    return CSV.getListOfNodes();
  }

  public static ArrayList<Node> getListOfNodeNoStairs() throws SQLException {

    return CSV.getListOfNodes(ActiveGraph.FilterMapElements.NoStairs);
  }

  public static boolean findUser(String username, String password) throws SQLException {
    return JDBCUtils.findUser(username, password);
  }

  public static ArrayList<Employee> getListOfEmployeeFromDB() throws SQLException {
    return JDBCUtils.exportListOfEmployee();
  }

  public static void loadEmployeeCSV() throws IOException, SQLException {
    CSV.loadCSVtoDBEmployee();
  }
}
