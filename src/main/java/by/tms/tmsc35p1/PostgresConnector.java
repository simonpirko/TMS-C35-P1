package by.tms.tmsc35p1;

import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnector {
    public static java.sql.Connection getConnection() throws SQLException {
        java.sql.Connection conn;
        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "root");
        return conn;
    }

}
