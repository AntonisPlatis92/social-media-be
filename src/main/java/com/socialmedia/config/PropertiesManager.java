package com.socialmedia.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesManager {
    private static final Properties properties = new Properties();

    public static String getProperty(String key) {
        String environment = System.getProperty("app.environment", "prod");

        String configFileName = "prod".equals(environment) ? "application.properties" : "application-" + environment + ".properties";

        InputStream inputStream = PropertiesManager.class.getClassLoader().getResourceAsStream(configFileName);

        if (inputStream != null) {
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return properties.getProperty(key);
    }
}