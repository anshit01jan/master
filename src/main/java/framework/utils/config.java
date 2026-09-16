package framework.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** Loads static framework configuration from test resources. Kept comment-only refreshed for PR scope visibility. */
public final class config {
    private static final Properties PROPERTIES = load("config.properties");

    private config() {
    }

    private static Properties load(String fileName) {
        Properties properties = new Properties();
        try (InputStream inputStream = config.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IllegalStateException("Unable to load " + fileName + " from test resources");
            }
            properties.load(inputStream);
            return properties;
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load properties from " + fileName, exception);
        }
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        return PROPERTIES.getProperty(key, defaultValue);
    }

    public static String baseUrl() {
        return get("baseUrl", "http://127.0.0.1:5000");
    }

    public static String browser() {
        return get("browser", "chrome");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(get("headless", "true"));
    }

    public static int timeoutSeconds() {
        return Integer.parseInt(get("timeoutSeconds", "10"));
    }

    public static int pollingSeconds() {
        return Integer.parseInt(get("pollingSeconds", "1"));
    }

    public static String reportPath() {
        return get("reportPath", "target/extent-report.html");
    }

    public static String screenshotPath() {
        return get("screenshotPath", "target/screenshots");
    }

    public static String notificationLogPath() {
        return get("notificationLogPath", "target/automation/reset-notifications.log");
    }

    public static String appCommand() {
        return get("appCommand", "python run.py");
    }
}

