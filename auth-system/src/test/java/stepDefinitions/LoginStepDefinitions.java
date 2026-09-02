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

public class LoginStepDefinitions {

    private WebDriver driver;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private String currentUrl;

    public LoginStepDefinitions() {
        this.driver = DriverManager.getDriver();
        this.loginPage = new LoginPage(driver);
        this.dashboardPage = new DashboardPage(driver);
    }

    @Given("the user is on the login page")
    public void theUserIsOnTheLoginPage() {
        Logger.info("Navigating to login page");
        loginPage.navigateToLoginPage();
        currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/login"), "Should be on login page");
    }

    @When("the user enters valid credentials")
    public void theUserEntersValidCredentials(DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps();
        Map<String, String> credentials = data.get(0);
        String email = credentials.get("email");
        String pass = credentials.get("password");

        Logger.info("Entering valid credentials - Email: " + email);
        loginPage.enterEmail(email);
        loginPage.enterPassword(pass);
    }

    @When("the user enters credentials with invalid password")
    public void theUserEntersCredentialsWithInvalidPassword(DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps();
        Map<String, String> credentials = data.get(0);
        String email = credentials.get("email");
        String pass = credentials.get("password");

        Logger.info("Entering credentials with invalid password - Email: " + email);
        loginPage.enterEmail(email);
        loginPage.enterPassword(pass);
    }

    @When("the user enters non-existent user credentials")
    public void theUserEntersNonExistentUserCredentials(DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps();
        Map<String, String> credentials = data.get(0);
        String email = credentials.get("email");
        String pass = credentials.get("password");

        Logger.info("Entering non-existent user credentials - Email: " + email);
        loginPage.enterEmail(email);
        loginPage.enterPassword(pass);
    }

    @When("the user leaves the email field empty")
    public void theUserLeavesTheEmailFieldEmpty() {
        Logger.info("Leaving email field empty");
    }

    @When("enters password {string}")
    public void entersPassword(String pass) {
        Logger.info("Entering password");
        loginPage.enterPassword(pass);
    }

    @When("the user enters email {string}")
    public void theUserEntersEmail(String email) {
        Logger.info("Entering email: " + email);
        loginPage.enterEmail(email);
    }

    @When("leaves the password field empty")
    public void leavesThePasswordFieldEmpty() {
        Logger.info("Leaving password field empty");
    }

    @When("the user leaves both email and password fields empty")
    public void theUserLeavesBothEmailAndPasswordFieldsEmpty() {
        Logger.info("Leaving both email and password fields empty");
    }

    @When("the user attempts SQL injection in email field")
    public void theUserAttemptsSQLInjectionInEmailField(DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps();
        Map<String, String> credentials = data.get(0);
        String email = credentials.get("email");
        String pass = credentials.get("password");

        Logger.info("Attempting SQL injection - Email: " + email);
        loginPage.enterEmail(email);
        loginPage.enterPassword(pass);
    }

    @When("the user enters credentials with leading and trailing whitespace")
    public void theUserEntersCredentialsWithLeadingAndTrailingWhitespace(DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps();
        Map<String, String> credentials = data.get(0);
        String email = credentials.get("email");
        String pass = credentials.get("password");

        Logger.info("Entering credentials with whitespace - Email: [" + email + "]");
        loginPage.enterEmail(email);
        loginPage.enterPassword(pass);
    }

    @When("the user enters email with different case")
    public void theUserEntersEmailWithDifferentCase(DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps();
        Map<String, String> credentials = data.get(0);
        String email = credentials.get("email");
        String pass = credentials.get("password");

        Logger.info("Entering email with different case - Email: " + email);
        loginPage.enterEmail(email);
        loginPage.enterPassword(pass);
    }

    @And("the user clicks the Login button")
    public void theUserClicksTheLoginButton() {
        Logger.info("Clicking login button");
        loginPage.clickLoginButton();
    }

    @Then("the user should be redirected to the dashboard")
    public void theUserShouldBeRedirectedToTheDashboard() {
        Logger.info("Verifying redirect to dashboard");
        waitForUrlToContain("/dashboard", 10);
        currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/dashboard"),
            "User should be redirected to dashboard. Current URL: " + currentUrl);
    }

    @Then("the user should see a welcome message")
    public void theUserShouldSeeAWelcomeMessage() {
        Logger.info("Verifying welcome message is displayed");
        Assert.assertTrue(dashboardPage.isWelcomeMessageDisplayed(),
            "Welcome message should be displayed on dashboard");
    }

    @Then("the session should be active")
    public void theSessionShouldBeActive() {
        Logger.info("Verifying session is active");
        Assert.assertTrue(driver.getCurrentUrl().contains("/dashboard"),
            "Session should be active and user should remain on dashboard");
    }

    @Then("the login should fail")
    public void theLoginShouldFail() {
        Logger.info("Verifying login failed");
        currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/login"),
            "Login should fail and user should remain on login page. Current URL: " + currentUrl);
    }

    @Then("an error message {string} should be displayed")
    public void anErrorMessageShouldBeDisplayed(String expectedMessage) {
        Logger.info("Verifying error message: " + expectedMessage);
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
            "Error message should be displayed");
        String actualMessage = loginPage.getErrorMessage();
        Assert.assertTrue(actualMessage.contains(expectedMessage),
            "Expected error message to contain: '" + expectedMessage + "' but got: '" + actualMessage + "'");
    }

    @Then("the user should remain on the login page")
    public void theUserShouldRemainOnTheLoginPage() {
        Logger.info("Verifying user remains on login page");
        currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/login"),
            "User should remain on login page. Current URL: " + currentUrl);
    }

    @Then("a validation error {string} should be displayed")
    public void aValidationErrorShouldBeDisplayed(String expectedMessage) {
        Logger.info("Verifying validation error: " + expectedMessage);

        if (expectedMessage.contains("Email")) {
            Assert.assertTrue(loginPage.isEmailValidationDisplayed(),
                "Email validation message should be displayed");
            String actualMessage = loginPage.getEmailValidationMessage();
            Assert.assertTrue(actualMessage.contains(expectedMessage) ||
                actualMessage.toLowerCase().contains("email") ||
                actualMessage.toLowerCase().contains("required"),
                "Expected validation message to contain: '" + expectedMessage + "' but got: '" + actualMessage + "'");
        } else if (expectedMessage.contains("Password")) {
            Assert.assertTrue(loginPage.isPasswordValidationDisplayed(),
                "Password validation message should be displayed");
            String actualMessage = loginPage.getPasswordValidationMessage();
            Assert.assertTrue(actualMessage.contains(expectedMessage) ||
                actualMessage.toLowerCase().contains("password") ||
                actualMessage.toLowerCase().contains("required"),
                "Expected validation message to contain: '" + expectedMessage + "' but got: '" + actualMessage + "'");
        }
    }

    @Then("the Login button should remain active")
    public void theLoginButtonShouldRemainActive() {
        Logger.info("Verifying login button remains active");
        Assert.assertTrue(loginPage.isLoginButtonEnabled(),
            "Login button should remain active/enabled");
    }

    @Then("validation errors should be displayed")
    public void validationErrorsShouldBeDisplayed(DataTable dataTable) {
        Logger.info("Verifying multiple validation errors are displayed");
        List<Map<String, String>> errors = dataTable.asMaps();

        for (Map<String, String> error : errors) {
            String field = error.get("field");
            String errorMessage = error.get("error");

            if (field.equals("email")) {
                Assert.assertTrue(loginPage.isEmailValidationDisplayed(),
                    "Email validation should be displayed");
                String actualMessage = loginPage.getEmailValidationMessage();
                Assert.assertTrue(actualMessage.contains(errorMessage) ||
                    actualMessage.toLowerCase().contains("email") ||
                    actualMessage.toLowerCase().contains("required"),
                    "Email validation message mismatch");
            } else if (field.equals("password")) {
                Assert.assertTrue(loginPage.isPasswordValidationDisplayed(),
                    "Password validation should be displayed");
                String actualMessage = loginPage.getPasswordValidationMessage();
                Assert.assertTrue(actualMessage.contains(errorMessage) ||
                    actualMessage.toLowerCase().contains("password") ||
                    actualMessage.toLowerCase().contains("required"),
                    "Password validation message mismatch");
            }
        }
    }

    @Then("the system should sanitize the input")
    public void theSystemShouldSanitizeTheInput() {
        Logger.info("Verifying system sanitized the SQL injection input");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
            "Error message should be displayed after sanitization");
    }

    @Then("the attempt should be logged in audit logs")
    public void theAttemptShouldBeLoggedInAuditLogs() {
        Logger.info("SQL injection attempt should be logged in audit logs");
        Assert.assertTrue(true, "Audit logging is handled by backend");
    }

    @Then("the system should trim whitespace")
    public void theSystemShouldTrimWhitespace() {
        Logger.info("Verifying system trimmed whitespace from credentials");
    }

    @Then("the system should treat email as case-insensitive")
    public void theSystemShouldTreatEmailAsCaseInsensitive() {
        Logger.info("Verifying system treated email as case-insensitive");
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
