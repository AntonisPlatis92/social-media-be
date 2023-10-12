package com.socialmedia.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesManager {
    private static final Properties properties = new Properties();

    static {
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
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}