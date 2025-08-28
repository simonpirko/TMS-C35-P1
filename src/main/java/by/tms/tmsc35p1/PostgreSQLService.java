package by.tms.tmsc35p1;

import java.sql.*;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostgreSQLService {

    public boolean isUnique(Account account) {
        boolean flag = true;
        try{Class.forName("org.postgresql.Driver");
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "root");
        conn.setAutoCommit(false);
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM accounts WHERE username = ?");
            String username = account.username();
            stmt.setString(1, username);
            stmt.execute();
            conn.commit();
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                flag = false;
            }
            conn.close();
        } catch (Exception e) {
            Logger.getLogger(PostgreSQLService.class.getName()).log(Level.SEVERE, "Failed to create an account", e);
        }
        return flag;
    }

    public boolean addAccount(Account account){
        boolean result = false;
        if(!isUnique(account)){
            return result;
        }
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "root");
            conn.setAutoCommit(false);

            PreparedStatement addStatement = conn.prepareStatement("INSERT INTO accounts VALUES (?, ?)");



            addStatement.setString(1, account.username());
            addStatement.setString(2, account.password());

            addStatement.execute();
            conn.commit();
            conn.close();
            DriverManager.deregisterDriver(new org.postgresql.Driver());
            result = true;
        } catch(Exception e) {
            Logger.getLogger(PostgreSQLService.class.getName()).log(Level.SEVERE, "Failed to create an account", e);
        }
        return result;
    }

    public Optional<Account> getAccount(String username, String password){
        Account account = null;
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "root");
            conn.setAutoCommit(false);

            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM accounts WHERE username=? AND password=?");

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            boolean execute = preparedStatement.execute();

            conn.commit();
            conn.close();
            DriverManager.deregisterDriver(new org.postgresql.Driver());
            if (execute) {
                ResultSet rs = preparedStatement.getResultSet();
                while (rs.next()) {
                    account = new Account(rs.getString(1), rs.getString(2));
                }
            }
        } catch(Exception e) {
            Logger.getLogger(PostgreSQLService.class.getName()).log(Level.SEVERE, "Failed to log in the account", e);
        }
        return Optional.ofNullable(account);
    }

}

