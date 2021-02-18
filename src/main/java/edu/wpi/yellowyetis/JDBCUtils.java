package edu.wpi.yellowyetis;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;

import org.apache.derby.iapi.error.StandardException;
import org.apache.derby.iapi.sql.ResultSet;

public class JDBCUtils {
    private static Connection conn;

    static {
        // credentials
        String user = "admin";
        String password = "admin";

        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        String connectionURL = "jdbc:derby:DB";

        // Checking for Driver
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Derby JDBC Driver?");

            e.printStackTrace();
        }

        // Attempting Connection
        try {
            conn = DriverManager.getConnection(connectionURL + ";create=true;", user, password);
            Statement stmt = conn.createStatement();
        } catch (Exception e) {
            // catching failed connection
            System.out.println("Connection Failed! Check output console");

            e.printStackTrace();
        }

        try {
            Statement stmt = conn.createStatement();
            String sqlNode =
                    "create table Node(nodeID varchar(20) PRIMARY KEY ,\n"
                            + "nodeType varchar(8) not null ,\n"
                            + "xcoord varchar(8) not null ,\n"
                            + "ycoord varchar(8) not null ,\n"
                            + "floor varchar(2) not null ,\n"
                            + "building varchar(20) not null ,\n"
                            + "room varchar(15) not null ,\n"
                            + "longName varchar(40) not null ,\n"
                            + "shortName varchar(10) not null ,\n"
                            + "teamAssigned char not null )";
            stmt.executeUpdate(sqlNode);

            String sqlEdge =
                    "create table Edge(edgeID varchar(40) PRIMARY KEY NOT NULL ,\n"
                            + "startNode varchar(30) not null ,\n"
                            + "endNode varchar(30) not null )";

            stmt.executeUpdate(sqlEdge);
        } catch (SQLException ignored) {

        }
    }

    /*
    get connection
     */
    public static Connection getConn() throws SQLException {
        return DriverManager.getConnection("jdbc:derby:DB;");
    }
  /*
  release resources and close connection in case there is no result set
   */

    // parameter as null if there is no such thing to close
    public static void close(PreparedStatement ps, ResultSet rs, Statement stmt, Connection conn) {
        if (ps != null) {

            try {
                ps.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (StandardException e) {
                e.printStackTrace();
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates a prepared statement for either an "insert into" or "update" query type
     *
     * @param numArguments
     * @param object
     * @param tableName
     * @param queryType
     * @return
     * @throws SQLException
     * @throws NoSuchFieldException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static PreparedStatement createPreparedStatement(
            int numArguments, Object object, String tableName, String queryType)
            throws SQLException, IllegalAccessException {
        // Building the portion of the query statement that handles the (?), (?), ... for the values
        // being inserted
        StringBuilder arguments = new StringBuilder();

        // appending ' (?), ' onto the string builder accounting for all but the last
        for (int i = 0; i < numArguments - 1; i++) {
            arguments.append("(?), ");
        }
        // adding the last without the comma
        arguments.append("(?)");

        // creates the prepared statement inserting with tableName and the arguments stringbuilder
        PreparedStatement psInsert =
                JDBCUtils.conn.prepareStatement(
                        queryType + " ADMIN." + tableName + " values(" + arguments.toString() + ")");
        Field[] fields = object.getClass().getDeclaredFields();
        int parameterCounter = 0;

        // This portion of the code will fill the (?) with the field values retrieved from the object
        for (Field field : fields) {
            String fieldName = field.getName();
            parameterCounter++;
            // for some reason the first "field" of node is neighbors lmao
            if (fieldName.equals("neighbors") || fieldName.equals("$jacocoData")) {
                parameterCounter--;
                continue;
            }

            field.setAccessible(true);

            String param = String.valueOf(field.get(object));
            psInsert.setString(parameterCounter, param);
        }

        return psInsert;
    }

    /**
     * Inserts into specified table the inputted object
     *
     * @param numArgs
     * @param object
     * @param tableName
     * @throws SQLException           if there is a duplicate key and updates instead
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    public static void insert(int numArgs, Object object, String tableName)
            throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException,
            NoSuchFieldException {
        PreparedStatement statement =
                createPreparedStatement(numArgs, object, tableName, "insert into");
        try {
            statement.execute();

        } catch (SQLException e) {

            //updates given object value in table if the PK already exists w/in it
            if (e.getErrorCode() == 30000) {
                JDBCUtils.update(numArgs, object, tableName);
            }

        }
        close(statement, null, null, null);
    }

    /**
     * Takes in an arrayList objects and uses to reflection in the insert statement to insert
     * the object into its respective table.
     * @param numArgs
     * @param objects
     * @param tableName
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws NoSuchFieldException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static void insertArrayListNode(int numArgs, ArrayList<Object> objects, String tableName)
            throws SQLException, ClassNotFoundException, NoSuchFieldException, InstantiationException,
            IllegalAccessException {

        int size = objects.size();
        for (Object object : objects) {
            JDBCUtils.insert(numArgs, object, "Node");
        }

    }

    /**
     * Updates the specified object with its matching ID in the specified table
     *
     * @param numArgs
     * @param object
     * @param tableName
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    public static void update(int numArgs, Object object, String tableName)
            throws SQLException, IllegalAccessException {
        PreparedStatement statement = createPreparedStatement(numArgs, object, tableName, "update");
        statement.execute();
    }

    public static void selectQuery(String tableName) throws SQLException {

        String sql = "SELECT * FROM ADMIN." + tableName;
        Statement stmt = null;
        stmt = conn.createStatement();
        stmt.executeQuery(sql);
    }
}
