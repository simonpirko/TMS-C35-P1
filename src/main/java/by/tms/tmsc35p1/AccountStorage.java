package by.tms.tmsc35p1;

import java.sql.*;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountStorage {

    public static final String ACCOUNT_CHECK_SQL = "SELECT * FROM accounts WHERE username = ?";
    public static final String ACCOUNT_ADD_SQL = "INSERT INTO accounts VALUES (default, ?, ?)";
    public static final String ACCOUNT_GET_SQL = "SELECT * FROM accounts WHERE username=? AND password=?";

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

}

