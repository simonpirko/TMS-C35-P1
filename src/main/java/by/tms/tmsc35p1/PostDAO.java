package by.tms.tmsc35p1;

import jakarta.servlet.http.HttpServlet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostDAO extends HttpServlet {
    private final Connection connection;

    public PostDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Post> getAllPostsSortedByDate() throws SQLException {
        String sql = "SELECT * FROM posts ORDER BY created_at DESC";
        List<Post> posts = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Post post = new Post(rs.getInt("id"), rs.getString("title"), rs.getString("content"), rs.getInt("user_id"), rs.getTimestamp("timestamp"));
                post.setId(rs.getInt("id"));
                post.setContent(rs.getString("content"));
                post.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                posts.add(post);
            }
        }

        return posts;
    }

    public Post getPostById(int id) throws SQLException {
        String sql = "SELECT * FROM posts WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Post(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("content"),
                            rs.getInt("user_id"),
                            rs.getTimestamp("timestamp")
                    );
                }
            }
        }
        return null;
    }

    public List<Post> getAllPostsByUserIdSortedByDate(int userId) throws SQLException {
        String sql = "SELECT * FROM posts WHERE user_id = ? ORDER BY created_at DESC";
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeQuery();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return null;
    }

    public void likePost(int postId) {
        String sql = "UPDATE posts SET likes = likes + 1 WHERE id = ?";
        try (Connection connection1 = PostgresConnector.getConnection()) {
            PreparedStatement preparedStatement = connection1.prepareStatement(sql);
            preparedStatement.setInt(1, postId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void unLikePost(int postId) {
        String sql = "UPDATE posts SET likes = likes - 1 WHERE id = ?";
        try (Connection connection1 = PostgresConnector.getConnection()) {
            PreparedStatement preparedStatement = connection1.prepareStatement(sql);
            preparedStatement.setInt(1, postId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
