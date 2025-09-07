package by.tms.tmsc35p1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CommentStorage {
    public static void save(String commentAuthor, Integer postId, String commentText) {
        String sql = "INSERT INTO comments (id ,commentAuthor, postId, commentText) VALUES (default ,?, ?, ?)";

        try(Connection connection = PostgresConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, commentAuthor);
            preparedStatement.setInt(2, postId);
            preparedStatement.setString(3, commentText);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
