package stepDefinitions;

import framework.drivers.DriverManager;
import framework.pages.DashboardPage;
import framework.pages.LoginPage;
import framework.utils.Logger;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class LoginSteps {

    private WebDriver driver;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    public LoginSteps() {
        this.driver = DriverManager.getDriver();
        this.loginPage = new LoginPage(driver);
        this.dashboardPage = new DashboardPage(driver);
    }

    @Given("the user is on the login page")
    public void theUserIsOnTheLoginPage() {
        Logger.info("Navigating to login page");
        loginPage.navigateToLoginPage();
        Assert.assertTrue(loginPage.isLoginFormDisplayed(), "Login form should be displayed");
    }

    @When("the user enters valid username {string} and password {string}")
    public void theUserEntersValidUsernameAndPassword(String username, String password) {
        Logger.info("Entering valid credentials: " + username);
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
    }

    @When("the user enters username {string} and password {string}")
    public void theUserEntersUsernameAndPassword(String username, String password) {
        Logger.info("Entering credentials - Username: " + username);
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
    }

    @When("the user clicks the login button")
    public void theUserClicksTheLoginButton() {
        Logger.info("Clicking login button");
        loginPage.clickLoginButton();
    }

    @Then("the user should be redirected to the dashboard")
    public void theUserShouldBeRedirectedToTheDashboard() {
        Logger.info("Verifying redirect to dashboard");
        Assert.assertTrue(dashboardPage.isDashboardPage(), "User should be redirected to dashboard");
    }

    @Then("the user should see the welcome message")
    public void theUserShouldSeeTheWelcomeMessage() {
        Logger.info("Verifying welcome message is displayed");
        Assert.assertTrue(dashboardPage.isWelcomeMessageDisplayed(), "Welcome message should be displayed");
    }

    @Then("the user should see an error message {string}")
    public void theUserShouldSeeAnErrorMessage(String expectedMessage) {
        Logger.info("Verifying error message: " + expectedMessage);
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
        String actualMessage = loginPage.getErrorMessage();
        Assert.assertTrue(actualMessage.contains(expectedMessage),
            "Expected: " + expectedMessage + ", but got: " + actualMessage);
    }

    @Then("the user should remain on the login page")
    public void theUserShouldRemainOnTheLoginPage() {
        Logger.info("Verifying user remains on login page");
        Assert.assertTrue(loginPage.isLoginFormDisplayed(), "User should remain on login page");
    }

    @When("the user leaves username field empty and enters password {string}")
    public void theUserLeavesUsernameFieldEmptyAndEntersPassword(String password) {
        Logger.info("Leaving username empty, entering password");
        loginPage.enterPassword(password);
    }

    @Then("the user should see validation message {string}")
    public void theUserShouldSeeValidationMessage(String expectedMessage) {
        Logger.info("Verifying validation message: " + expectedMessage);
        if (expectedMessage.contains("Username")) {
            Assert.assertTrue(loginPage.isUsernameValidationDisplayed(), "Username validation should be displayed");
            String actualMessage = loginPage.getUsernameValidationMessage();
            Assert.assertTrue(actualMessage.contains(expectedMessage),
                "Expected: " + expectedMessage + ", but got: " + actualMessage);
        } else if (expectedMessage.contains("Password")) {
            Assert.assertTrue(loginPage.isPasswordValidationDisplayed(), "Password validation should be displayed");
            String actualMessage = loginPage.getPasswordValidationMessage();
            Assert.assertTrue(actualMessage.contains(expectedMessage),
                "Expected: " + expectedMessage + ", but got: " + actualMessage);
        }
    }

    @Then("the login button should be disabled")
    public void theLoginButtonShouldBeDisabled() {
        Logger.info("Verifying login button is disabled");
        Assert.assertFalse(loginPage.isLoginButtonEnabled(), "Login button should be disabled");
    }

    @When("the user enters username {string} and leaves password field empty")
    public void theUserEntersUsernameAndLeavesPasswordFieldEmpty(String username) {
        Logger.info("Entering username, leaving password empty");
        loginPage.enterUsername(username);
    }

    @When("the user leaves both username and password fields empty")
    public void theUserLeavesBothUsernameAndPasswordFieldsEmpty() {
        Logger.info("Leaving both username and password fields empty");
        // Fields are already empty on page load
    }

    @When("the user enters password {string}")
    public void theUserEntersPassword(String password) {
        Logger.info("Entering password");
        loginPage.enterPassword(password);
    }

    @Then("the password field should display masked characters")
    public void thePasswordFieldShouldDisplayMaskedCharacters() {
        Logger.info("Verifying password field is masked");
        Assert.assertTrue(loginPage.isPasswordMasked(), "Password field should display masked characters");
    }

    @And("the user should see validation message {string}")
    public void andTheUserShouldSeeValidationMessage(String expectedMessage) {
        theUserShouldSeeValidationMessage(expectedMessage);
    }
}
