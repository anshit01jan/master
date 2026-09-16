package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Page object for the forgot-password form and its success message. Kept comment-only refreshed for PR scope visibility. */
public class ForgotPasswordPage extends BasePage {
    private final By identifierInput = By.id("identifier");
    private final By sendResetLinkButton = By.xpath("//button[normalize-space()='Send reset link']");
    private final By backToLoginLink = By.linkText("Back to login");
    private final By successAlert = By.cssSelector(".alert-success");

    public ForgotPasswordPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        open("/forgot-password");
    }

    public void enterIdentifier(String identifier) {
        enterTextInInputBox(identifierInput, identifier);
    }

    public void submit() {
        clickOnElement(sendResetLinkButton);
    }

    public boolean isIdentifierFieldDisplayed() {
        return isDisplayed(identifierInput);
    }

    public boolean isSendResetLinkButtonDisplayed() {
        return isDisplayed(sendResetLinkButton);
    }

    public boolean isBackToLoginLinkDisplayed() {
        return isDisplayed(backToLoginLink);
    }

    public String message() {
        return getText(successAlert);
    }

    public void waitForPageLoad() {
        waitForElementToBePresent(identifierInput);
    }
}