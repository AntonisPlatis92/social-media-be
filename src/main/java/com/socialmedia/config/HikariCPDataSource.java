package com.socialmedia.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariCPDataSource {

    private static final String JDBC_URL = PropertiesManager.getProperty("jdbc.url");
    private static final String DB_USER = PropertiesManager.getProperty("jdbc.username");
    private static final String DB_PASSWORD = PropertiesManager.getProperty("jdbc.password");

    public static HikariDataSource getDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(JDBC_URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);
        config.setMaximumPoolSize(10);

        return new HikariDataSource(config);
    }
}
