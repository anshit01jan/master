package framework.pages;

import framework.base.BasePage;
import framework.utils.Config;
import framework.utils.environment.Env;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ForgotPasswordPage extends BasePage {

    // Locators
    private By emailInput = By.id("email");
    private By submitButton = By.xpath("//button[@type='submit']");
    private By backToLoginLink = By.linkText("Back to Login");
    private By errorMessage = By.cssSelector(".alert-danger, .error-message");
    private By successMessage = By.cssSelector(".alert-success, .success-message");
    private By emailValidationMessage = By.xpath("//input[@id='email']/following-sibling::div[contains(@class,'invalid-feedback')]");
    private By pageTitle = By.tagName("h2");

    public ForgotPasswordPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToForgotPasswordPage() {
        String baseUrl = Env.getBaseUrl();
        String forgotPasswordPath = Config.getForgotPasswordPath();
        navigateToUrl(baseUrl + forgotPasswordPath);
    }

    public void enterEmail(String email) {
        enterTextInInputBox(emailInput, email);
    }

    public void clickSubmitButton() {
        clickOnElement(submitButton);
    }

    public void clickBackToLoginLink() {
        clickOnElement(backToLoginLink);
    }

    public void submitForgotPasswordRequest(String email) {
        enterEmail(email);
        clickSubmitButton();
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

    public boolean isEmailValidationDisplayed() {
        return isElementDisplayed(emailValidationMessage);
    }

    public boolean isSubmitButtonEnabled() {
        return isElementEnabled(submitButton);
    }

    public String getPageTitle() {
        return getElementText(pageTitle);
    }

    public String getCurrentPageUrl() {
        return getCurrentUrl();
    }
}
