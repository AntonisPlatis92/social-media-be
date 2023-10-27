package com.socialmedia.config;

import com.zaxxer.hikari.HikariConfig;

import java.util.HashMap;
import java.util.Map;

public class HikariCPConfig {
    public static Map<String, Object> getJpaProperties() {
        // HikariCP configuration
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        config.setUsername("postgres");
        config.setPassword("password");
        config.setMaximumPoolSize(10); // Set your maximum pool size
        config.setConnectionTimeout(30000); // Set your connection timeout
        // Add more HikariCP properties as needed

        // Combine JPA and HikariCP properties
        Map<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.connection.provider_class", "org.hibernate.hikaricp.internal.HikariCPConnectionProvider");
        jpaProperties.put("hibernate.hikari.dataSource", config);

        // Additional JPA properties if needed
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
        jpaProperties.put("hibernate.show_sql", "true");

        return jpaProperties;
    }
}
