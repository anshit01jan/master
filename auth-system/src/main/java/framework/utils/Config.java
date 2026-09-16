package framework.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";

    static {
        try {
            properties = new Properties();
            FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH);
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            Logger.error("Failed to load config.properties: " + e.getMessage());
            throw new RuntimeException("Config file not found: " + CONFIG_FILE_PATH);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getBaseUrl() {
        return properties.getProperty("baseUrl");
    }

    public static String getBrowser() {
        return properties.getProperty("browser", "chrome");
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(properties.getProperty("headless", "false"));
    }

    public static int getImplicitWait() {
        return Integer.parseInt(properties.getProperty("implicitWait", "10"));
    }

    public static int getExplicitWait() {
        return Integer.parseInt(properties.getProperty("explicitWait", "20"));
    }

    public static int getPageLoadTimeout() {
        return Integer.parseInt(properties.getProperty("pageLoadTimeout", "30"));
    }

    public static int getPollingInterval() {
        return Integer.parseInt(properties.getProperty("pollingInterval", "500"));
    }

    public static String getExtentReportPath() {
        return properties.getProperty("extentReportPath", "target/extent-reports/extent-report.html");
    }

    public static String getScreenshotPath() {
        return properties.getProperty("screenshotPath", "target/screenshots");
    }

    public static String getLoginPath() {
        return properties.getProperty("loginPath", "/login");
    }

    public static String getDashboardPath() {
        return properties.getProperty("dashboardPath", "/dashboard");
    }

    public static String getForgotPasswordPath() {
        return properties.getProperty("forgotPasswordPath", "/forgot-password");
    }

    public static String getResetPasswordPath() {
        return properties.getProperty("resetPasswordPath", "/reset-password");
    }

    public static String getTestDataPath() {
        return properties.getProperty("testDataPath", "src/test/resources/testdata.xlsx");
    }

    public static String getEnvironment() {
        return System.getProperty("env", "qa");
    }
}
