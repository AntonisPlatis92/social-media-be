package com.socialmedia.config;

import com.github.dockerjava.api.model.PortBinding;
import org.flywaydb.core.Flyway;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class TestContainerSetup {

    private static final int FIXED_PORT = 5433;

    @Container
    public static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test")
            .withCreateContainerCmdModifier(cmd -> cmd.withPortBindings(PortBinding.parse(FIXED_PORT + ":" + PostgreSQLContainer.POSTGRESQL_PORT)));

    public static void setUp() {
        Flyway flyway = Flyway.configure()
                .dataSource(
                        PropertiesManager.getProperty("jdbc.url"),
                        PropertiesManager.getProperty("jdbc.username"),
                        PropertiesManager.getProperty("jdbc.password"))
                .locations("classpath:/testcontainers")
                .load();
        flyway.migrate();
    }

}
