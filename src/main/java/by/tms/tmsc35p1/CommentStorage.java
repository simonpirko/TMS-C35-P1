package by.tms.tmsc35p1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentStorage {

    public static void save(Integer postId, String commentAuthor, String commentText, String avatarUrl) {
        String sql = "INSERT INTO comments (postId, commentAuthor, commentText, avatarUrl) VALUES (?, ?, ?, ?)";

        try (Connection connection = PostgresConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, postId);
            preparedStatement.setString(2, commentAuthor);
            preparedStatement.setString(3, commentText);
            preparedStatement.setString(4, avatarUrl);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Comment> getAllComments() {
        List<Comment> comments = new ArrayList<>();
        try (Connection connection = PostgresConnector.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM comments")
        ) {
            while (resultSet.next()) {
                Comment comment = new Comment(
                        resultSet.getLong("postId"),
                        resultSet.getString("commentAuthor"),
                        resultSet.getString("commentText"),
                        resultSet.getString("avatarUrl")
                );
                comments.add(comment);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return comments;
    }
}
