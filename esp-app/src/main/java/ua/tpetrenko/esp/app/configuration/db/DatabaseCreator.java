package ua.tpetrenko.esp.app.configuration.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

/**
 * @author Roman Zdoronok
 */
@Slf4j
@RequiredArgsConstructor
public class DatabaseCreator {

    private final DataSourceProperties dataSourceProperties;

    public void createDatabase() {
        String databaseUrl = getDatabaseUrl();
        String databaseName = getDatabaseName();
        // При подключении к субд без явного указания базы, подключение будет осуществляться к базе с названием роли.
        // Соответственно если для роли esp нет базы esp (а для такого случая и написан данный класс) - будет брошено исключение
        // Поэтому подключаемся к заведомо существующей базе postgres
        try(Connection connection = DriverManager.getConnection(databaseUrl + "postgres", dataSourceProperties.getUsername(), dataSourceProperties.getPassword())) {
            try(Statement statement = connection.createStatement()) {
                try(ResultSet resultSet =
                    statement.executeQuery("SELECT count(*) FROM pg_database "
                                               + "WHERE datistemplate = false AND datname = '" + databaseName + "'")) {
                    if (resultSet.next()) {
                        int result = resultSet.getInt(1);
                        if (result < 1) {
                            log.info("Database \'{}\' does not exist! Creating...", databaseName);
                            statement.executeUpdate("CREATE DATABASE " + databaseName);
                            log.info("Database \'{}\' successfully created", databaseName);
                        }
                        else {
                            log.info("Database \'{}\' already exists", databaseName);
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            log.error("Failed to create database \'{}\': {}", databaseName, e.getMessage());
        }
    }

    private String getDatabaseUrl() {
        String url = dataSourceProperties.getUrl();
        return url.substring(0, url.lastIndexOf('/') + 1);
    }

    private String getDatabaseName() {
        String url = dataSourceProperties.getUrl();
        return url.substring(url.lastIndexOf('/') + 1);
    }
}
