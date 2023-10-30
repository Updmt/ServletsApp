package org.example.config;

import org.flywaydb.core.Flyway;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

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

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
