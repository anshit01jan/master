package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Page object for reset-password token validation and password entry flows. Kept comment-only refreshed for PR scope visibility. */
public class ResetPasswordPage extends BasePage {
    private final By newPasswordInput = By.id("new_password");
    private final By resetPasswordButton = By.xpath("//button[normalize-space()='Reset password']");
    private final By errorAlert = By.cssSelector(".alert-danger");
    private final By successAlert = By.cssSelector(".alert-success");

    public ResetPasswordPage(WebDriver driver) {
        super(driver);
    }

    public void openWithoutToken() {
        open("/reset-password");
    }

    public void openWithToken(String token) {
        open("/reset-password?token=" + token);
    }

    public void enterNewPassword(String password) {
        enterTextInInputBox(newPasswordInput, password);
    }

    public void submit() {
        clickOnElement(resetPasswordButton);
    }

    public boolean isNewPasswordFieldDisplayed() {
        return isDisplayed(newPasswordInput);
    }

    public boolean isResetPasswordButtonDisplayed() {
        return isDisplayed(resetPasswordButton);
    }

    public boolean isPasswordMasked() {
        return "password".equalsIgnoreCase(attribute(newPasswordInput, "type"));
    }

    public String errorMessage() {
        return getText(errorAlert);
    }

    public String successMessage() {
        return getText(successAlert);
    }

    public boolean isOnResetPage() {
        return driver.getCurrentUrl().contains("/reset-password");
    }

    public void waitForPageLoad() {
        waitForElementToBePresent(newPasswordInput);
    }
}