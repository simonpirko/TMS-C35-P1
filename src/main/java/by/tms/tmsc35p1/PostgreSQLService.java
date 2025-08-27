package by.tms.tmsc35p1;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostgreSQLService {



    public String addAccount(Account account){

        String message = "success";

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "root");
            conn.setAutoCommit(false);

            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO accounts VALUES (?, ?)");


            preparedStatement.setString(1, account.username());
            preparedStatement.setString(2, account.password());

            preparedStatement.execute();
            conn.commit();
            conn.close();
        } catch(Exception e) {
            message = "error";
            Logger.getLogger(PostgreSQLService.class.getName()).log(Level.SEVERE, "Failed to do an account", e);
        }
        return message;
    }

}

