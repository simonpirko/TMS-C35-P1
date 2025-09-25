package by.tms.tmsc35p1;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostStorage {
    private final DataSource dataSource;

    public PostStorage(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Post> findByUserId(int userId) throws SQLException {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT id, title, content, user_id, created_at , likes FROM posts WHERE user_id = ? ORDER BY created_at DESC";
        try (Connection conn = PostgresConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    posts.add(new Post(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("content"),
                            rs.getInt("user_id"),
                            rs.getTimestamp("created_at"),
                            rs.getInt("likes")

                    ));
                }
            }
        }
        return posts;
    }

    public static void save(String title, String content, Integer user_id) {
        String sql = "INSERT INTO posts (title, content, user_id) VALUES (?, ?, ?)";
        try (Connection conn = PostgresConnector.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, content);
            ps.setInt(3, user_id);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
