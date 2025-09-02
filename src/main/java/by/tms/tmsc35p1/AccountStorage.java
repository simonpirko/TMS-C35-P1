package by.tms.tmsc35p1;

import java.sql.*;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountStorage {

    public static final String ACCOUNT_CHECK_SQL = "SELECT * FROM accounts WHERE username = ?";
    public static final String ACCOUNT_ADD_SQL = "INSERT INTO accounts VALUES (default, ?, ?)";
    public static final String ACCOUNT_GET_SQL = "SELECT * FROM accounts WHERE username=? AND password=?";

    public static final String UPDATE_USERNAME_SQL = "UPDATE accounts SET username = ? WHERE id = ?";
    public static final String UPDATE_PASSWORD_SQL = "UPDATE accounts SET password = ? WHERE id = ?";
    public static final String CHECK_USERNAME_UNIQUE_SQL = "SELECT COUNT(*) FROM accounts WHERE username = ? AND id != ?";

    public boolean isUnique(Account account) {
        boolean flag = true;
        try{
            Connection conn = PostgresConnector.getConnection();

            conn.setAutoCommit(false);

            PreparedStatement stmt = conn.prepareStatement(ACCOUNT_CHECK_SQL);

            stmt.setString(1, account.username());
            stmt.execute();

            conn.commit();

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                flag = false;
            }
            conn.close();
        } catch (SQLException e) {
            Logger.getLogger(AccountStorage.class.getName()).log(Level.SEVERE, "Failed to create an account", e);
        }
        return flag;
    }

    public boolean addAccount(Account account){
        boolean result = false;
        if(!isUnique(account)){
            return result;
        }
        try {
            Connection conn = PostgresConnector.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement addStatement = conn.prepareStatement(ACCOUNT_ADD_SQL);

            addStatement.setString(1, account.username());
            addStatement.setString(2, account.password());

            addStatement.execute();
            conn.commit();
            conn.close();

            result = true;
        } catch(Exception e) {
            Logger.getLogger(AccountStorage.class.getName()).log(Level.SEVERE, "Failed to create an account", e);
        }
        return result;
    }

    public Optional<Account> getAccountByUsername(String username, String password){
        Account account = null;
        try {
            Connection conn = PostgresConnector.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement preparedStatement = conn.prepareStatement(ACCOUNT_GET_SQL);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            boolean execute = preparedStatement.execute();

            conn.commit();
            conn.close();
            DriverManager.deregisterDriver(new org.postgresql.Driver());
            if (execute) {
                ResultSet rs = preparedStatement.getResultSet();
                while (rs.next()) {
                    account = new Account(rs.getInt(1), rs.getString(2), rs.getString(3));
                }
            }
        } catch(Exception e) {
            Logger.getLogger(AccountStorage.class.getName()).log(Level.SEVERE, "Failed to log in the account", e);
        }
        return Optional.ofNullable(account);
    }


    public boolean isUsernameUnique(String username, Integer excludeAccountId) {
        try {
            Connection conn = PostgresConnector.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement stmt = conn.prepareStatement(CHECK_USERNAME_UNIQUE_SQL);
            stmt.setString(1, username);
            stmt.setInt(2, excludeAccountId);

            ResultSet rs = stmt.executeQuery();
            boolean isUnique = false;
            if (rs.next()) {
                isUnique = rs.getInt(1) == 0;
            }

            conn.commit();
            conn.close();
            return isUnique;
        } catch (SQLException e) {
            Logger.getLogger(AccountStorage.class.getName()).log(Level.SEVERE, "Failed to check username uniqueness", e);
            return false;
        }
    }

    // Обновить username
    public boolean updateUsername(Integer accountId, String newUsername) {
        try {
            Connection conn = PostgresConnector.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement stmt = conn.prepareStatement(UPDATE_USERNAME_SQL);
            stmt.setString(1, newUsername);
            stmt.setInt(2, accountId);

            int rowsAffected = stmt.executeUpdate();
            conn.commit();
            conn.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            Logger.getLogger(AccountStorage.class.getName()).log(Level.SEVERE, "Failed to update username", e);
            return false;
        }
    }

    // Обновить password
    public boolean updatePassword(Integer accountId, String newPassword) {
        try {
            Connection conn = PostgresConnector.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement stmt = conn.prepareStatement(UPDATE_PASSWORD_SQL);
            stmt.setString(1, newPassword);
            stmt.setInt(2, accountId);

            int rowsAffected = stmt.executeUpdate();
            conn.commit();
            conn.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            Logger.getLogger(AccountStorage.class.getName()).log(Level.SEVERE, "Failed to update password", e);
            return false;
        }
    }

    // Получить аккаунт по ID
    public Optional<Account> getAccountById(Integer accountId) {
        Account account = null;
        try {
            Connection conn = PostgresConnector.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM accounts WHERE id = ?");
            preparedStatement.setInt(1, accountId);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                account = new Account(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
            }

            conn.commit();
            conn.close();
        } catch(Exception e) {
            Logger.getLogger(AccountStorage.class.getName()).log(Level.SEVERE, "Failed to get account by id", e);
        }
        return Optional.ofNullable(account);
    }
}

