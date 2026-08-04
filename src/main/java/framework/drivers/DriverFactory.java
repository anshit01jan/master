package framework.drivers;

import framework.utils.config;
import framework.utils.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/** Creates browser instances for the configured automation run. Kept comment-only refreshed for PR scope visibility. */
public final class DriverFactory {
    public WebDriver createDriver(String browserName) {
        String browser = browserName == null || browserName.isBlank() ? config.browser() : browserName.trim();
        Logger.info("Creating browser instance: " + browser);
        WebDriver driver;
        if ("firefox".equalsIgnoreCase(browser)) {
            FirefoxOptions options = new FirefoxOptions();
            if (config.headless()) {
                options.addArguments("-headless");
            }
            driver = new FirefoxDriver(options);
        } else {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--window-size=1440,1080");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            if (config.headless()) {
                options.addArguments("--headless=new");
            }
            driver = new ChromeDriver(options);
        }
        DriverManager.setDriver(driver);
        return driver;
    }
}