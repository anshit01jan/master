package framework.pages;

import framework.base.BasePage;
import framework.utils.Config;
import framework.utils.environment.Env;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PasswordResetPage extends BasePage {

    // Locators
    private By newPasswordInput = By.id("newPassword");
    private By confirmPasswordInput = By.id("confirmPassword");
    private By submitButton = By.xpath("//button[@type='submit']");
    private By errorMessage = By.cssSelector(".alert-danger, .error-message");
    private By successMessage = By.cssSelector(".alert-success, .success-message");
    private By newPasswordValidationMessage = By.xpath("//input[@id='newPassword']/following-sibling::div[contains(@class,'invalid-feedback')]");
    private By confirmPasswordValidationMessage = By.xpath("//input[@id='confirmPassword']/following-sibling::div[contains(@class,'invalid-feedback')]");
    private By pageTitle = By.tagName("h2");
    private By tokenErrorMessage = By.cssSelector(".token-error, .expired-token");

    public PasswordResetPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToPasswordResetPage(String token) {
        String baseUrl = Env.getBaseUrl();
        String resetPasswordPath = Config.getResetPasswordPath();
        navigateToUrl(baseUrl + resetPasswordPath + "?token=" + token);
    }

    public void enterNewPassword(String newPassword) {
        enterTextInInputBox(newPasswordInput, newPassword);
    }

    public void enterConfirmPassword(String confirmPassword) {
        enterTextInInputBox(confirmPasswordInput, confirmPassword);
    }

    public void clickSubmitButton() {
        clickOnElement(submitButton);
    }

    public void performPasswordReset(String newPassword, String confirmPassword) {
        enterNewPassword(newPassword);
        enterConfirmPassword(confirmPassword);
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

    public String getNewPasswordValidationMessage() {
        return getElementText(newPasswordValidationMessage);
    }

    public String getConfirmPasswordValidationMessage() {
        return getElementText(confirmPasswordValidationMessage);
    }

    public boolean isNewPasswordValidationDisplayed() {
        return isElementDisplayed(newPasswordValidationMessage);
    }

    public boolean isConfirmPasswordValidationDisplayed() {
        return isElementDisplayed(confirmPasswordValidationMessage);
    }

    public boolean isTokenErrorDisplayed() {
        return isElementDisplayed(tokenErrorMessage);
    }

    public String getTokenErrorMessage() {
        return getElementText(tokenErrorMessage);
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
