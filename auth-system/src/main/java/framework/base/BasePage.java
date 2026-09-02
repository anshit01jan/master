package framework.base;

import framework.utils.Config;
import framework.utils.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Config.getExplicitWait()));
    }

    protected WebElement waitForElementToBePresent(By locator) {
        Logger.debug("Waiting for element to be present: " + locator);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected WebElement waitForElementToBeVisible(By locator) {
        Logger.debug("Waiting for element to be visible: " + locator);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForElementToBeClickable(By locator) {
        Logger.debug("Waiting for element to be clickable: " + locator);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void clickOnElement(By locator) {
        Logger.debug("Clicking on element: " + locator);
        waitForElementToBeClickable(locator).click();
    }

    protected void enterTextInInputBox(By locator, String text) {
        Logger.debug("Entering text in input box: " + locator + " with value: " + text);
        WebElement element = waitForElementToBeVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected void selectElementFromDropdown(By locator, String value) {
        Logger.debug("Selecting from dropdown: " + locator + " with value: " + value);
        WebElement element = waitForElementToBeVisible(locator);
        Select select = new Select(element);
        select.selectByVisibleText(value);
    }

    protected String getElementText(By locator) {
        Logger.debug("Getting text from element: " + locator);
        return waitForElementToBeVisible(locator).getText();
    }

    protected boolean isElementDisplayed(By locator) {
        try {
            Logger.debug("Checking if element is displayed: " + locator);
            return waitForElementToBeVisible(locator).isDisplayed();
        } catch (Exception e) {
            Logger.debug("Element not displayed: " + locator);
            return false;
        }
    }

    protected boolean isElementEnabled(By locator) {
        Logger.debug("Checking if element is enabled: " + locator);
        return waitForElementToBeVisible(locator).isEnabled();
    }

    protected List<WebElement> getElements(By locator) {
        Logger.debug("Getting list of elements: " + locator);
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    protected void scrollToElement(By locator) {
        Logger.debug("Scrolling to element: " + locator);
        WebElement element = waitForElementToBePresent(locator);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    protected String getCurrentUrl() {
        Logger.debug("Getting current URL");
        return driver.getCurrentUrl();
    }

    protected String getPageTitle() {
        Logger.debug("Getting page title");
        return driver.getTitle();
    }

    protected void navigateToUrl(String url) {
        Logger.info("Navigating to URL: " + url);
        driver.get(url);
    }
}
