package by.tms.tmsc35p1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FollowRepository {

    private final String SQL_FOLLOW_STATEMENT = "INSERT INTO SUBSCRIPTIONS (follower_id, following_id) VALUES (?, ?)";
    private final String SQL_UNFOLLOW_STATEMENT = "DELETE FROM SUBSCRIPTIONS WHERE follower_id = ? AND following_id = ?";


    public void follow(int follower, int following) {
        try (Connection connection = PostgresConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FOLLOW_STATEMENT)) {

            preparedStatement.setInt(1, follower);
            preparedStatement.setInt(2, following);

            preparedStatement.execute();
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void unfollow(int follower, int following) {
        try (Connection connection = PostgresConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_UNFOLLOW_STATEMENT)) {

            preparedStatement.setInt(1, follower);
            preparedStatement.setInt(2, following);
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final String SQL_GET_ALL_FOLLOWERS = "select a.* " +
            " from accounts a " +
            " inner join subscriptions s on a.id = s.follower_id " +
            " where following_id = ? ";
    private final String SQL_GET_ALL_FOLLOWINGS = "select a.* " +
            "from accounts a " +
            "inner join subscriptions s on a.id = s.following_id " +
            "where s.follower_id = ?; ";

    //получает всех подписчиков
    public Optional<List<Account>> getAllFollowers(int id) {
        List<Account> followers = new ArrayList<>();
        try (Connection connection = PostgresConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_ALL_FOLLOWERS)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                followers.add(new Account(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3)));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(followers);
    }

    //получает всех на кого подписан
    public Optional<List<Account>> getAllFollowings(int id) {
        List<Account> followings = new ArrayList<>();
        try (Connection connection = PostgresConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_ALL_FOLLOWINGS)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                followings.add(new Account(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(followings);
    }


    public List<Account> getFollowingUsers(int userId) throws SQLException {
        List<Account> following = new ArrayList<>();
        String sql = """
            SELECT a.id, a.username, a.password 
            FROM accounts a
            INNER JOIN subscriptions s ON a.id = s.following_id
            WHERE s.follower_id = ?
            """;

        try (Connection conn = PostgresConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Account account = new Account(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password")
                    );
                    following.add(account);
                }
            }
        }
        return following;
    }

}
