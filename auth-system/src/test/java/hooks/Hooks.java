package hooks;

import framework.drivers.DriverFactory;
import framework.drivers.DriverManager;
import framework.reports.ExtentManager;
import framework.utils.Config;
import framework.utils.Logger;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;

public class Hooks {

    private WebDriver driver;

    @Before
    public void setUp(Scenario scenario) {
        Logger.info("Setting up test for scenario: " + scenario.getName());

        String browser = Config.getBrowser();
        driver = DriverFactory.createDriver(browser);
        DriverManager.setDriver(driver);

        Logger.info("Browser launched: " + browser);
    }

    @After
    public void tearDown(Scenario scenario) {
        Logger.info("Tearing down test for scenario: " + scenario.getName());

        if (scenario.isFailed()) {
            Logger.error("Scenario failed: " + scenario.getName());
        } else {
            Logger.info("Scenario passed: " + scenario.getName());
        }

        DriverManager.quitDriver();
        Logger.info("Browser closed");
    }
}
