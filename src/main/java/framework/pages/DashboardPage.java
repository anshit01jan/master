package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;

/** Page object for dashboard readiness and welcome-state verification. Kept comment-only refreshed for PR scope visibility. */
public class DashboardPage extends BasePage {
    private final By dashboardHeading = By.xpath("//h2[normalize-space()='Dashboard']");
    private final By welcomeMessage = By.cssSelector("p.text-secondary");
    private final By logoutButton = By.xpath("//a[normalize-space()='Logout']");

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        try {
            wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/dashboard"),
                ExpectedConditions.visibilityOfElementLocated(dashboardHeading),
                ExpectedConditions.visibilityOfElementLocated(logoutButton),
                ExpectedConditions.textToBePresentInElementLocated(welcomeMessage, "Welcome,")
            ));
            return isDisplayed(dashboardHeading) || isDisplayed(logoutButton);
        } catch (TimeoutException exception) {
            return false;
        }
    }

    public String welcomeText() {
        return getText(welcomeMessage);
    }
}