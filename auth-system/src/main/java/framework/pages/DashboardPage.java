package framework.pages;

import framework.base.BasePage;
import framework.utils.Config;
import framework.utils.environment.Env;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DashboardPage extends BasePage {

    // Locators
    private By welcomeMessage = By.cssSelector(".welcome-message, h1");
    private By logoutButton = By.linkText("Logout");
    private By userProfile = By.cssSelector(".user-profile, .username");
    private By dashboardContent = By.cssSelector(".dashboard-content, .main-content");
    private By pageTitle = By.tagName("h1");

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToDashboard() {
        String baseUrl = Env.getBaseUrl();
        String dashboardPath = Config.getDashboardPath();
        navigateToUrl(baseUrl + dashboardPath);
    }

    public boolean isWelcomeMessageDisplayed() {
        return isElementDisplayed(welcomeMessage);
    }

    public String getWelcomeMessage() {
        return getElementText(welcomeMessage);
    }

    public void clickLogout() {
        clickOnElement(logoutButton);
    }

    public boolean isUserProfileDisplayed() {
        return isElementDisplayed(userProfile);
    }

    public String getUserProfile() {
        return getElementText(userProfile);
    }

    public boolean isDashboardContentDisplayed() {
        return isElementDisplayed(dashboardContent);
    }

    public String getPageTitle() {
        return getElementText(pageTitle);
    }

    public String getDashboardUrl() {
        return getCurrentUrl();
    }

    public boolean isDashboardPage() {
        String currentUrl = getCurrentUrl();
        return currentUrl.contains(Config.getDashboardPath());
    }
}
