package hooks;

import framework.drivers.DriverFactory;
import framework.drivers.DriverManager;
import framework.utils.AppProcessManager;
import framework.reports.ExtentManager;
import framework.utils.config;
import framework.utils.Logger;
import framework.utils.ScreenshotUtil;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;

/** Cucumber hooks for application lifecycle, driver setup, and evidence capture. Kept comment-only refreshed for PR scope visibility. */
public class Hooks {
    @BeforeAll
    public static void beforeAll() {
        ExtentManager.initReport();
        AppProcessManager.startApplication();
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        AppProcessManager.resetApplicationState();
        WebDriver driver = new DriverFactory().createDriver(config.browser());
        driver.manage().window().maximize();
        DriverManager.setDriver(driver);
        ExtentManager.startTest(scenario.getName());
        if (!scenario.getSourceTagNames().isEmpty()) {
            ExtentManager.getTest().assignCategory(scenario.getSourceTagNames().toArray(new String[0]));
        }
        Logger.info("Starting scenario: " + scenario.getName());
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed() && DriverManager.getDriver() != null) {
            String screenshotPath = ScreenshotUtil.capture(DriverManager.getDriver(), scenario.getName());
            ExtentManager.getTest().fail("Scenario failed: " + scenario.getName());
            try {
                ExtentManager.getTest().addScreenCaptureFromPath(screenshotPath);
            } catch (Exception ignored) {
                Logger.warn("Unable to attach screenshot to report for " + scenario.getName());
            }
        } else if (ExtentManager.getTest() != null) {
            ExtentManager.getTest().pass("Scenario completed successfully");
        }
        DriverManager.quitDriver();
    }

    @AfterAll
    public static void afterAll() {
        ExtentManager.flush();
        AppProcessManager.stopApplication();
    }
}