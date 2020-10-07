package ua.tpetrenko.esp.app.configuration.db;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Roman Zdoronok
 */
@Configuration
@RequiredArgsConstructor
public class DatabaseConfig {

    private final DataSourceProperties dataSourceProperties;

    @Bean
    public DatabaseCreator databaseCreator() {
        return new DatabaseCreator(dataSourceProperties);
    }

    @Bean
    public FlywayMigrationStrategy beforeMigrationStrategy(DatabaseCreator databaseCreator) {
        return flyway -> {
            databaseCreator.createDatabase();
            flyway.migrate();
        };
    }
}
