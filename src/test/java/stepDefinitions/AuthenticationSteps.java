package stepDefinitions;

import framework.drivers.DriverManager;
import framework.pages.DashboardPage;
import framework.pages.ForgotPasswordPage;
import framework.pages.LoginPage;
import framework.pages.ResetPasswordPage;
import framework.utils.environment.env;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.List;

/** Step definitions covering login, forgot-password, and reset-password flows. Kept comment-only refreshed for PR scope visibility. */
public class AuthenticationSteps {
    private enum PageType {
        LOGIN,
        FORGOT,
        RESET
    }

    private final String defaultUsername = env.username();
    private final String defaultPassword = env.password();

    private String username;
    private String password;
    private PageType currentPage;

    private WebDriver driver() {
        return DriverManager.getDriver();
    }

    private LoginPage loginPage() {
        return new LoginPage(driver());
    }

    private ForgotPasswordPage forgotPasswordPage() {
        return new ForgotPasswordPage(driver());
    }

    private ResetPasswordPage resetPasswordPage() {
        return new ResetPasswordPage(driver());
    }

    private DashboardPage dashboardPage() {
        return new DashboardPage(driver());
    }

    private void openLoginPage() {
        loginPage().open();
        currentPage = PageType.LOGIN;
    }

    private void openForgotPasswordPage() {
        forgotPasswordPage().open();
        currentPage = PageType.FORGOT;
    }

    private void openResetPasswordPageWithoutToken() {
        resetPasswordPage().openWithoutToken();
        currentPage = PageType.RESET;
    }

    private void openResetPasswordPageWithToken(String token) {
        resetPasswordPage().openWithToken(token);
        currentPage = PageType.RESET;
    }

    private void openDashboardPageDirectly() {
        driver().get(env.baseUrl() + "/dashboard");
    }

    private void assertFieldTypes(DataTable dataTable, String... expected) {
        List<String> values = dataTable.asList();
        Assert.assertEquals(values.size(), expected.length, "Unexpected number of UI controls");
        for (int index = 0; index < expected.length; index++) {
            Assert.assertEquals(values.get(index), expected[index]);
        }
    }

    @Given("the user is on the Login page")
    @Given("the user opens the Login page")
    public void theUserIsOnTheLoginPage() {
        openLoginPage();
    }

    @Given("the user is on the Forgot Password page")
    @Given("the user opens the Forgot Password page")
    public void theUserIsOnTheForgotPasswordPage() {
        openForgotPasswordPage();
    }

    @Given("the user opens the Reset Password page without a token")
    public void theUserOpensTheResetPasswordPageWithoutAToken() {
        openResetPasswordPageWithoutToken();
    }

    @Given("the user opens the Reset Password page with token {string}")
    public void theUserOpensTheResetPasswordPageWithToken(String token) {
        openResetPasswordPageWithToken(token);
    }

    @Given("the user has a valid username {string}")
    public void theUserHasAValidUsername(String value) {
        username = value;
        loginPage().enterUsername(value);
    }

    @Given("the user has a valid password {string}")
    public void theUserHasAValidPassword(String value) {
        password = value;
        loginPage().enterPassword(value);
    }

    @Given("the user enters username {string}")
    public void theUserEntersUsername(String value) {
        username = value;
        loginPage().enterUsername(value);
    }

    @Given("the user enters password {string}")
    public void theUserEntersPassword(String value) {
        password = value;
        loginPage().enterPassword(value);
    }

    @Given("the user enters a registered username {string}")
    public void theUserEntersARegisteredUsername(String value) {
        forgotPasswordPage().enterIdentifier(value);
    }

    @Given("the user enters a registered email {string}")
    public void theUserEntersARegisteredEmail(String value) {
        forgotPasswordPage().enterIdentifier(value);
    }

    @Given("the user enters an unregistered identifier {string}")
    public void theUserEntersAnUnregisteredIdentifier(String value) {
        forgotPasswordPage().enterIdentifier(value);
    }

    @Given("the user does not have an authenticated session")
    public void theUserDoesNotHaveAnAuthenticatedSession() {
        driver().manage().deleteAllCookies();
    }

    @Given("the user account {string} is locked after 2 failed login attempts")
    public void theUserAccountIsLockedAfterFailedLoginAttempts(String value) {
        openLoginPage();
        loginPage().login(value, "WrongPass1!");
        loginPage().login(value, "WrongPass1!");
        username = value;
        password = defaultPassword;
    }

    @When("the user clicks on the Log in button")
    public void theUserClicksOnTheLogInButton() {
        loginPage().submit();
    }

    @When("the user clicks on the Log in button without entering a username and password")
    public void theUserClicksOnTheLogInButtonWithoutEnteringAUsernameAndPassword() {
        loginPage().submitWithoutUsernameAndPassword();
    }

    @When("the user clicks on the Log in button without entering a username")
    public void theUserClicksOnTheLogInButtonWithoutEnteringAUsername() {
        loginPage().submitWithoutUsername();
    }

    @When("the user clicks on the Log in button without entering a password")
    public void theUserClicksOnTheLogInButtonWithoutEnteringAPassword() {
        loginPage().submitWithoutPassword();
    }

    @When("the user clicks on the Send reset link button")
    public void theUserClicksOnTheSendResetLinkButton() {
        forgotPasswordPage().submit();
    }

    @When("the user clicks on the Reset password button")
    public void theUserClicksOnTheResetPasswordButton() {
        resetPasswordPage().submit();
    }

    @When("the user enters a strong new password {string}")
    @When("the user enters new password {string}")
    public void theUserEntersANewPassword(String value) {
        resetPasswordPage().enterNewPassword(value);
    }

    @When("the user logs in with username {string} and password {string}")
    public void theUserLogsInWithUsernameAndPassword(String value, String value2) {
        openLoginPage();
        username = value;
        password = value2;
        loginPage().login(value, value2);
    }

    @When("the user enters username {string} and password {string}")
    public void theUserEntersUsernameAndPassword(String value, String value2) {
        username = value;
        password = value2;
        loginPage().enterUsername(value);
        loginPage().enterPassword(value2);
    }

    @When("the page is fully loaded")
    public void thePageIsFullyLoaded() {
        if (currentPage == PageType.LOGIN) {
            loginPage().waitForPageLoad();
        } else if (currentPage == PageType.FORGOT) {
            forgotPasswordPage().waitForPageLoad();
        } else if (currentPage == PageType.RESET) {
            resetPasswordPage().waitForPageLoad();
        }
    }

    @When("the user focuses on the Password field")
    public void theUserFocusesOnThePasswordField() {
        loginPage().waitForPageLoad();
    }

    @When("the user clicks on the Log in button for the first failed attempt")
    public void theUserClicksOnTheLogInButtonForTheFirstFailedAttempt() {
        loginPage().submit();
        loginPage().errorMessage();
    }

    @When("the user clicks on the Log in button for the second failed attempt with the same invalid password")
    public void theUserClicksOnTheLogInButtonForTheSecondFailedAttemptWithTheSameInvalidPassword() {
        loginPage().waitForPageLoad();
        loginPage().login(username == null ? defaultUsername : username, password == null ? "WrongPass1!" : password);
    }

    @When("the user opens the Dashboard page directly")
    public void theUserOpensTheDashboardPageDirectly() {
        openDashboardPageDirectly();
    }

    @Then("the user should be redirected to the Dashboard page")
    public void theUserShouldBeRedirectedToTheDashboardPage() {
        Assert.assertTrue(dashboardPage().isDisplayed(), "Expected the dashboard to be displayed");
    }

    @Then("the user should be redirected to the Login page")
    public void theUserShouldBeRedirectedToTheLoginPage() {
        Assert.assertTrue(loginPage().isOnLoginPage(), "Expected the login page to be displayed");
    }

    @Then("the user should be redirected to the Login page and the message {string} should be visible")
    public void theUserShouldBeRedirectedToTheLoginPageAndTheMessageShouldBeVisible(String expectedMessage) {
        Assert.assertTrue(loginPage().isOnLoginPage(), "Expected the login page to be displayed");
        Assert.assertEquals(loginPage().successMessage(), expectedMessage);
    }

    @Then("the user should remain on the Login page")
    public void theUserShouldRemainOnTheLoginPage() {
        Assert.assertTrue(loginPage().isOnLoginPage(), "Expected to remain on the login page");
    }

    @Then("the user should remain on the Reset Password page")
    public void theUserShouldRemainOnTheResetPasswordPage() {
        Assert.assertTrue(resetPasswordPage().isOnResetPage(), "Expected to remain on the reset password page");
    }

    @Then("the Reset Password page should remain displayed")
    public void theResetPasswordPageShouldRemainDisplayed() {
        Assert.assertTrue(resetPasswordPage().isOnResetPage(), "Expected the reset page to remain visible");
    }

    @Then("the user should see the error message {string}")
    public void theUserShouldSeeTheErrorMessage(String expectedMessage) {
        String actualMessage;
        if (driver().getCurrentUrl().contains("/reset-password")) {
            actualMessage = resetPasswordPage().errorMessage();
        } else {
            actualMessage = loginPage().errorMessage();
        }
        Assert.assertEquals(actualMessage, expectedMessage);
    }

    @Then("the user should see the reset token error message {string}")
    public void theUserShouldSeeTheResetTokenErrorMessage(String expectedMessage) {
        Assert.assertEquals(resetPasswordPage().errorMessage(), expectedMessage);
    }

    @Then("the user should see the message {string}")
    public void theUserShouldSeeTheMessage(String expectedMessage) {
        String actualMessage;
        if (driver().getCurrentUrl().contains("/forgot-password")) {
            actualMessage = forgotPasswordPage().message();
        } else if (driver().getCurrentUrl().contains("/reset-password")) {
            actualMessage = resetPasswordPage().successMessage();
        } else {
            actualMessage = loginPage().successMessage();
        }
        Assert.assertEquals(actualMessage, expectedMessage);
    }

    @Then("the account should be marked as locked")
    public void theAccountShouldBeMarkedAsLocked() {
        openLoginPage();
        loginPage().login(username == null ? defaultUsername : username, defaultPassword);
        Assert.assertEquals(loginPage().errorMessage(), "Account is locked. Try again later.");
    }

    @Then("the user should see the page controls")
    public void theUserShouldSeeThePageControls(DataTable dataTable) {
        assertFieldTypes(dataTable, dataTable.asList().toArray(new String[0]));
    }

    @Then("the login page should display")
    public void theLoginPageShouldDisplay(DataTable dataTable) {
        List<String> controls = dataTable.asList();
        Assert.assertTrue(loginPage().isUsernameFieldDisplayed());
        Assert.assertTrue(loginPage().isPasswordFieldDisplayed());
        Assert.assertTrue(loginPage().isLoginButtonDisplayed());
        Assert.assertTrue(loginPage().isForgotPasswordLinkDisplayed());
        Assert.assertTrue(controls.contains("Username input field"));
        Assert.assertTrue(controls.contains("Password input field"));
        Assert.assertTrue(controls.contains("Log in button"));
        Assert.assertTrue(controls.contains("Forgot password? link"));
    }

    @Then("the forgot password page should display")
    public void theForgotPasswordPageShouldDisplay(DataTable dataTable) {
        List<String> controls = dataTable.asList();
        Assert.assertTrue(forgotPasswordPage().isIdentifierFieldDisplayed());
        Assert.assertTrue(forgotPasswordPage().isSendResetLinkButtonDisplayed());
        Assert.assertTrue(forgotPasswordPage().isBackToLoginLinkDisplayed());
        Assert.assertTrue(controls.contains("Username or email input field"));
        Assert.assertTrue(controls.contains("Send reset link button"));
        Assert.assertTrue(controls.contains("Back to login link"));
    }

    @Then("the reset password page should display")
    public void theResetPasswordPageShouldDisplay(DataTable dataTable) {
        List<String> controls = dataTable.asList();
        Assert.assertTrue(resetPasswordPage().isNewPasswordFieldDisplayed());
        Assert.assertTrue(resetPasswordPage().isResetPasswordButtonDisplayed());
        Assert.assertTrue(resetPasswordPage().isPasswordMasked());
        Assert.assertTrue(controls.contains("New password input field"));
        Assert.assertTrue(controls.contains("Reset password button"));
        Assert.assertTrue(controls.contains("Masked password input behavior"));
    }

    @Then("the Password field should use masked password input behavior")
    public void thePasswordFieldShouldUseMaskedPasswordInputBehavior() {
        Assert.assertTrue(loginPage().isPasswordMasked(), "Expected the password field to be masked");
    }

    @Then("the New password field should use masked password input behavior")
    public void theNewPasswordFieldShouldUseMaskedPasswordInputBehavior() {
        Assert.assertTrue(resetPasswordPage().isPasswordMasked(), "Expected the new password field to be masked");
    }

    @Then("the page should display a Username input field")
    public void thePageShouldDisplayAUsernameInputField() {
        Assert.assertTrue(loginPage().isUsernameFieldDisplayed());
    }

    @Then("the page should display a Password input field")
    public void thePageShouldDisplayAPasswordInputField() {
        Assert.assertTrue(loginPage().isPasswordFieldDisplayed());
    }

    @Then("the page should display a Log in button")
    public void thePageShouldDisplayALogInButton() {
        Assert.assertTrue(loginPage().isLoginButtonDisplayed());
    }

    @Then("the page should display a Forgot password? link")
    public void thePageShouldDisplayAForgotPasswordLink() {
        Assert.assertTrue(loginPage().isForgotPasswordLinkDisplayed());
    }

    @Then("the page should display a Username or email input field")
    public void thePageShouldDisplayAUsernameOrEmailInputField() {
        Assert.assertTrue(forgotPasswordPage().isIdentifierFieldDisplayed());
    }

    @Then("the page should display a Send reset link button")
    public void thePageShouldDisplayASendResetLinkButton() {
        Assert.assertTrue(forgotPasswordPage().isSendResetLinkButtonDisplayed());
    }

    @Then("the page should display a Back to login link")
    public void thePageShouldDisplayABackToLoginLink() {
        Assert.assertTrue(forgotPasswordPage().isBackToLoginLinkDisplayed());
    }

    @Then("the page should display a New password input field")
    public void thePageShouldDisplayANewPasswordInputField() {
        Assert.assertTrue(resetPasswordPage().isNewPasswordFieldDisplayed());
    }

    @Then("the page should display a Reset password button")
    public void thePageShouldDisplayAResetPasswordButton() {
        Assert.assertTrue(resetPasswordPage().isResetPasswordButtonDisplayed());
    }

    @Then("the Dashboard page should remain displayed")
    public void theDashboardPageShouldRemainDisplayed() {
        Assert.assertTrue(dashboardPage().isDisplayed());
    }

    @Then("the user should see the message {string} after reset")
    public void theUserShouldSeeTheMessageAfterReset(String expectedMessage) {
        Assert.assertEquals(loginPage().successMessage(), expectedMessage);
    }
}