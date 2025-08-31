package by.tms.tmsc35p1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostStorage {
    public static void save(String title, String content) {
        String sql = "INSERT INTO posts (title, content) VALUES (?, ?)";
        try (Connection conn = PostgresConnector.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, content);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
