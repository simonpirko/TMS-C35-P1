package by.tms.tmsc35p1;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.flywaydb.core.Flyway;

@WebListener
public class Listener implements ServletContextListener {
    final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    final String DB_USER = "postgres";
    final String DB_PASSWORD = "root";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Flyway flyway = Flyway.configure()
                    .dataSource(DB_URL, DB_USER, DB_PASSWORD)
                    .locations("classpath:db.migrations")
                    .load();

            flyway.migrate();
    }

}
