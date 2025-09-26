package by.tms.tmsc35p1;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FollowService {
    FollowRepository followRepository;

    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    public boolean follow(int followerId, int followingId) throws SQLException {
        if (checkIfFollowing(followerId, followingId)) {
            return false;
        }

        followRepository.follow(followerId, followingId);
        return true;
    }

    public boolean unfollow(int followerId, int followingId) throws SQLException {
        if (checkIfFollowing(followerId, followingId)) {
            followRepository.unfollow(followerId, followingId);
            return true;
        }

        return false;
    }

    public int getFollowersCount(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM subscriptions WHERE following_id = ?";
        try (Connection conn = PostgresConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public int getFollowingCount(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM subscriptions WHERE follower_id = ?";
        try (Connection conn = PostgresConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public List<Integer> getFollowingUsersIds(int userId) throws SQLException {
        List<Account> following = followRepository.getFollowingUsers(userId);
        List<Integer> followingIds = new ArrayList<>();
        for (Account account : following) {
            followingIds.add(account.id());
        }


        return followingIds;
    }

    public boolean checkIfFollowing(int followerId, int followingId) throws SQLException {
        String sql = "SELECT 1 FROM subscriptions WHERE follower_id = ? AND following_id = ?";
        try (Connection conn = PostgresConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, followerId);
            stmt.setInt(2, followingId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}
