package org.example.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.flywaydb.core.Flyway;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:mysql://localhost:3306/user_file_storage", "root", "root")
                .locations("classpath:db/migration")
                .load();
        flyway.migrate();
    }
}
