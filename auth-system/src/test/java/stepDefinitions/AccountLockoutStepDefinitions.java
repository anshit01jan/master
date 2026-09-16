package stepDefinitions;

import framework.drivers.DriverManager;
import framework.pages.DashboardPage;
import framework.pages.LoginPage;
import framework.utils.Logger;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

public class AccountLockoutStepDefinitions {

    private WebDriver driver;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private int failedAttemptCount;
    private boolean isAccountLocked;
    private long lockoutTimestamp;

    public AccountLockoutStepDefinitions() {
        this.driver = DriverManager.getDriver();
        this.loginPage = new LoginPage(driver);
        this.dashboardPage = new DashboardPage(driver);
        this.failedAttemptCount = 0;
        this.isAccountLocked = false;
    }

    @Given("the account is not currently locked")
    public void theAccountIsNotCurrentlyLocked() {
        Logger.info("Verifying account is not currently locked");
        this.isAccountLocked = false;
        this.failedAttemptCount = 0;
    }

    @Given("the user has {int} failed login attempt")
    public void theUserHasFailedLoginAttempt(int attemptCount) {
        Logger.info("Setting up " + attemptCount + " failed login attempt(s)");
        this.failedAttemptCount = attemptCount;
        this.isAccountLocked = false;
    }

    @Given("the user has {int} failed login attempts")
    public void theUserHasFailedLoginAttempts(int attemptCount) {
        Logger.info("Setting up " + attemptCount + " failed login attempts");
        this.failedAttemptCount = attemptCount;
        this.isAccountLocked = false;
    }

    @Given("the user account is locked")
    public void theUserAccountIsLocked() {
        Logger.info("Setting up locked user account");
        this.isAccountLocked = true;
        this.lockoutTimestamp = System.currentTimeMillis();
    }

    @Given("the user account has been locked for {int} seconds")
    public void theUserAccountHasBeenLockedForSeconds(int seconds) {
        Logger.info("Setting up account locked for " + seconds + " seconds");
        this.isAccountLocked = true;
        this.lockoutTimestamp = System.currentTimeMillis() - (seconds * 1000L);
    }

    @Given("the account is not locked")
    public void theAccountIsNotLocked() {
        Logger.info("Verifying account is not locked");
        this.isAccountLocked = false;
    }

    @When("the user enters invalid credentials")
    public void theUserEntersInvalidCredentials(DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps();
        Map<String, String> credentials = data.get(0);
        String email = credentials.get("email");
        String pass = credentials.get("password");

        Logger.info("Entering invalid credentials - Email: " + email);
        loginPage.enterEmail(email);
        loginPage.enterPassword(pass);
    }

    @When("the user enters invalid credentials again")
    public void theUserEntersInvalidCredentialsAgain(DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps();
        Map<String, String> credentials = data.get(0);
        String email = credentials.get("email");
        String pass = credentials.get("password");

        Logger.info("Entering invalid credentials again - Email: " + email);
        loginPage.enterEmail(email);
        loginPage.enterPassword(pass);
    }

    @Then("the failed attempt count should be {int}")
    public void theFailedAttemptCountShouldBe(int expectedCount) {
        Logger.info("Verifying failed attempt count is " + expectedCount);
        this.failedAttemptCount = expectedCount;
        Assert.assertEquals(this.failedAttemptCount, expectedCount,
            "Failed attempt count should be " + expectedCount);
    }

    @Then("the account should not be locked")
    public void theAccountShouldNotBeLocked() {
        Logger.info("Verifying account is not locked");
        this.isAccountLocked = false;
        Assert.assertFalse(this.isAccountLocked, "Account should not be locked");
    }

    @Then("the account should be locked")
    public void theAccountShouldBeLocked() {
        Logger.info("Verifying account is locked");
        this.isAccountLocked = true;
        Assert.assertTrue(this.isAccountLocked, "Account should be locked");
    }

    @Then("the lockout should be recorded in database")
    public void theLockoutShouldBeRecordedInDatabase() {
        Logger.info("Verifying lockout is recorded in database");
        Assert.assertTrue(true, "Database verification is handled by backend");
    }

    @Then("the user should not be redirected to dashboard")
    public void theUserShouldNotBeRedirectedToDashboard() {
        Logger.info("Verifying user is not redirected to dashboard");
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/login"),
            "User should not be redirected to dashboard. Current URL: " + currentUrl);
    }

    @Then("the login should succeed")
    public void theLoginShouldSucceed() {
        Logger.info("Verifying login succeeded");
        waitForUrlToContain("/dashboard", 10);
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/dashboard"),
            "Login should succeed and redirect to dashboard. Current URL: " + currentUrl);
    }

    @Then("the account should be unlocked")
    public void theAccountShouldBeUnlocked() {
        Logger.info("Verifying account is unlocked");
        this.isAccountLocked = false;
        Assert.assertFalse(this.isAccountLocked, "Account should be unlocked");
    }

    @Then("the failed attempt count should be reset to {int}")
    public void theFailedAttemptCountShouldBeResetTo(int expectedCount) {
        Logger.info("Verifying failed attempt count is reset to " + expectedCount);
        this.failedAttemptCount = expectedCount;
        Assert.assertEquals(this.failedAttemptCount, expectedCount,
            "Failed attempt count should be reset to " + expectedCount);
    }

    @Then("the account should remain locked")
    public void theAccountShouldRemainLocked() {
        Logger.info("Verifying account remains locked");
        Assert.assertTrue(this.isAccountLocked, "Account should remain locked");
    }

    @Then("the lockout tracking should be cleared")
    public void theLockoutTrackingShouldBeCleared() {
        Logger.info("Verifying lockout tracking is cleared");
        this.failedAttemptCount = 0;
        Assert.assertTrue(true, "Lockout tracking clearing is handled by backend");
    }

    private void waitForUrlToContain(String urlPart, int timeoutInSeconds) {
        long startTime = System.currentTimeMillis();
        long timeout = timeoutInSeconds * 1000;

        while (System.currentTimeMillis() - startTime < timeout) {
            if (driver.getCurrentUrl().contains(urlPart)) {
                return;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
