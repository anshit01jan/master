package framework.utils.environment;

import framework.utils.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** Resolves environment-scoped test data and endpoints for automation runs. Kept comment-only refreshed for PR scope visibility. */
public final class env {
    private static final Properties PROPERTIES = load("env.properties");

    private env() {
    }

    private static Properties load(String fileName) {
        Properties properties = new Properties();
        try (InputStream inputStream = env.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IllegalStateException("Unable to load " + fileName + " from test resources");
            }
            properties.load(inputStream);
            return properties;
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load properties from " + fileName, exception);
        }
    }

    private static String envName() {
        return System.getProperty("env", "qa");
    }

    public static String get(String keySuffix, String defaultValue) {
        return PROPERTIES.getProperty(envName() + "." + keySuffix, defaultValue);
    }

    public static String baseUrl() {
        return get("baseUrl", config.baseUrl());
    }

    public static String apiUrl() {
        return get("apiUrl", config.baseUrl());
    }

    public static String username() {
        return get("username", "scrum50");
    }

    public static String password() {
        return get("password", "Password1!");
    }

    public static String resetPassword() {
        return get("resetPassword", "BetterPass1!");
    }

    public static String email() {
        return get("email", "scrum50@example.com");
    }
}
