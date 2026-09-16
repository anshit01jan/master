package framework.base;

import framework.utils.config;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** Shared Selenium page helpers for waits, navigation, and safe element actions. Kept comment-only refreshed for PR scope visibility. */
public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(config.timeoutSeconds()));
    }

    protected WebElement waitForElementToBePresent(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected WebElement waitForElementToBeVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void clickOnElement(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void enterTextInInputBox(By locator, String text) {
        try {
            WebElement element = waitForElementToBeVisible(locator);
            element.clear();
            element.sendKeys(text);
        } catch (StaleElementReferenceException exception) {
            WebElement element = waitForElementToBeVisible(locator);
            element.clear();
            element.sendKeys(text);
        }
    }

    protected void selectElementFromDropdown(By locator, String visibleText) {
        new Select(waitForElementToBeVisible(locator)).selectByVisibleText(visibleText);
    }

    protected boolean isDisplayed(By locator) {
        return waitForElementToBeVisible(locator).isDisplayed();
    }

    protected String getText(By locator) {
        return waitForElementToBeVisible(locator).getText();
    }

    protected String attribute(By locator, String attributeName) {
        return waitForElementToBeVisible(locator).getAttribute(attributeName);
    }

    protected void open(String path) {
        driver.get(config.baseUrl() + path);
    }
}