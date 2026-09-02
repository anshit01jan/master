package framework.pages;

import framework.base.BasePage;
import framework.utils.Config;
import framework.utils.environment.Env;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    // Locators
    private By emailInput = By.id("email");
    private By passwordInput = By.id("password");
    private By loginButton = By.xpath("//button[@type='submit']");
    private By forgotPasswordLink = By.linkText("Forgot Password?");
    private By errorMessage = By.cssSelector(".alert-danger, .error-message");
    private By successMessage = By.cssSelector(".alert-success, .success-message");
    private By validationMessage = By.cssSelector(".invalid-feedback, .field-error");
    private By emailValidationMessage = By.xpath("//input[@id='email']/following-sibling::div[contains(@class,'invalid-feedback')]");
    private By passwordValidationMessage = By.xpath("//input[@id='password']/following-sibling::div[contains(@class,'invalid-feedback')]");
    private By pageTitle = By.tagName("h2");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToLoginPage() {
        String baseUrl = Env.getBaseUrl();
        String loginPath = Config.getLoginPath();
        navigateToUrl(baseUrl + loginPath);
    }

    public void enterEmail(String email) {
        enterTextInInputBox(emailInput, email);
    }

    public void enterPassword(String password) {
        enterTextInInputBox(passwordInput, password);
    }

    public void clickLoginButton() {
        clickOnElement(loginButton);
    }

    public void clickForgotPasswordLink() {
        clickOnElement(forgotPasswordLink);
    }

    public void performLogin(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
    }

    public String getErrorMessage() {
        return getElementText(errorMessage);
    }

    public String getSuccessMessage() {
        return getElementText(successMessage);
    }

    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(errorMessage);
    }

    public boolean isSuccessMessageDisplayed() {
        return isElementDisplayed(successMessage);
    }

    public String getEmailValidationMessage() {
        return getElementText(emailValidationMessage);
    }

    public String getPasswordValidationMessage() {
        return getElementText(passwordValidationMessage);
    }

    public boolean isEmailValidationDisplayed() {
        return isElementDisplayed(emailValidationMessage);
    }

    public boolean isPasswordValidationDisplayed() {
        return isElementDisplayed(passwordValidationMessage);
    }

    public boolean isLoginButtonEnabled() {
        return isElementEnabled(loginButton);
    }

    public String getPageTitle() {
        return getElementText(pageTitle);
    }
}
