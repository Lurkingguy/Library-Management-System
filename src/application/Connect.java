package application;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=Library;user=sa;password=123456789;encrypt=false;";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void printDatabaseInfo() {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                DatabaseMetaData dm = conn.getMetaData();
                System.out.println("Driver name: " + dm.getDriverName());
                System.out.println("Driver version: " + dm.getDriverVersion());
                System.out.println("Product name: " + dm.getDatabaseProductName());
                System.out.println("Product version: " + dm.getDatabaseProductVersion());
            }
        } catch (SQLException ex) {
            System.out.println("error connections " + ex);
        }
    }
}
