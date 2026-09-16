package framework.utils.environment;

import framework.utils.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Env {
    private static Properties envProperties;
    private static String activeEnvironment;
    private static final String ENV_FILE_PATH = "src/test/resources/env.properties";

    static {
        try {
            envProperties = new Properties();
            FileInputStream fis = new FileInputStream(ENV_FILE_PATH);
            envProperties.load(fis);
            fis.close();

            // Get active environment from system property or use default
            activeEnvironment = System.getProperty("env", envProperties.getProperty("default.env", "qa"));
            Logger.info("Active environment: " + activeEnvironment);
        } catch (IOException e) {
            Logger.error("Failed to load env.properties: " + e.getMessage());
            throw new RuntimeException("Environment file not found: " + ENV_FILE_PATH);
        }
    }

    public static String getActiveEnvironment() {
        return activeEnvironment;
    }

    public static String getEnvProperty(String key) {
        String fullKey = "env." + activeEnvironment + "." + key;
        String value = envProperties.getProperty(fullKey);
        if (value == null) {
            Logger.warn("Property not found for key: " + fullKey);
        }
        return value;
    }

    public static String getBaseUrl() {
        String baseUrl = getEnvProperty("baseUrl");
        return baseUrl != null ? baseUrl : "http://localhost:8080";
    }

    public static String getApiUrl() {
        String apiUrl = getEnvProperty("apiUrl");
        return apiUrl != null ? apiUrl : "http://localhost:8080/api";
    }
}
