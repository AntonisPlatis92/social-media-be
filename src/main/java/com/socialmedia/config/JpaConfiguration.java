package com.socialmedia.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.ttddyy.dsproxy.listener.logging.Log4jLogLevel;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.jpa.HibernatePersistenceProvider;

import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

import javax.sql.DataSource;
import java.util.Properties;

public class JpaConfiguration {
    public static Properties getJpaProperties() {
        Properties properties = new Properties();

        properties.put(AvailableSettings.DATASOURCE, getDataSource());
        properties.put(AvailableSettings.DIALECT, PostgreSQLDialect.class.getName());
        properties.put(AvailableSettings.JPA_METAMODEL_POPULATION, "disabled");

        return properties;
    }

    public static DataSource getDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(PropertiesManager.getProperty("jdbc.url"));
        config.setUsername(PropertiesManager.getProperty("jdbc.username"));
        config.setPassword(PropertiesManager.getProperty("jdbc.password"));
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);

        // Create the HikariCP data source
        DataSource hikariDataSource = new HikariDataSource(config);

        // Proxy datasource-proxy
        return ProxyDataSourceBuilder.create(hikariDataSource)
                .name("ProxyDataSource")
                .multiline()
                .logQueryByLog4j(Log4jLogLevel.INFO, "net.ttddyy.dsproxy.proxy")
                .build();
    }

    public static HibernatePersistenceProvider getPersistenceProvider() {
        return new HibernatePersistenceProvider();
    }
}
