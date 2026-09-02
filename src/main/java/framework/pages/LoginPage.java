package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Page object for login form interactions and page-level validation. Kept comment-only refreshed for PR scope visibility. */
public class LoginPage extends BasePage {
    private final By usernameInput = By.id("username");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.xpath("//button[normalize-space()='Log in']");
    private final By forgotPasswordLink = By.linkText("Forgot password?");
    private final By errorAlert = By.cssSelector(".alert-danger");
    private final By successAlert = By.cssSelector(".alert-success");
    private final By dashboardHeading = By.xpath("//h2[normalize-space()='Dashboard']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        open("/login");
    }

    public void enterUsername(String username) {
        enterTextInInputBox(usernameInput, username);
    }

    public void enterPassword(String password) {
        enterTextInInputBox(passwordInput, password);
    }

    public void submit() {
        clickOnElement(loginButton);
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        submit();
    }

    public void submitWithoutUsernameAndPassword() {
        submit();
    }

    public void submitWithoutUsername() {
        submit();
    }

    public void submitWithoutPassword() {
        submit();
    }

    public boolean isUsernameFieldDisplayed() {
        return isDisplayed(usernameInput);
    }

    public boolean isPasswordFieldDisplayed() {
        return isDisplayed(passwordInput);
    }

    public boolean isLoginButtonDisplayed() {
        return isDisplayed(loginButton);
    }

    public boolean isForgotPasswordLinkDisplayed() {
        return isDisplayed(forgotPasswordLink);
    }

    public boolean isPasswordMasked() {
        return "password".equalsIgnoreCase(attribute(passwordInput, "type"));
    }

    public String errorMessage() {
        return getText(errorAlert);
    }

    public String successMessage() {
        return getText(successAlert);
    }

    public boolean isOnLoginPage() {
        return driver.getCurrentUrl().contains("/login");
    }

    public boolean isOnDashboardPage() {
        return driver.getCurrentUrl().contains("/dashboard") && isDisplayed(dashboardHeading);
    }

    public void waitForPageLoad() {
        waitForElementToBePresent(usernameInput);
    }
}