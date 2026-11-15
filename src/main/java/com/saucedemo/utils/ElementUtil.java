package com.saucedemo.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ElementUtil {

    private static final Logger logger = LogManager.getLogger(ElementUtil.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    public ElementUtil(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
    }

    public WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void click(By locator) {
        try {
            waitForClickable(locator).click();
            logger.debug("Clicked on element: {}", locator);
        } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
            logger.warn("Standard click failed on {}. Retrying via JavaScriptExecutor.", locator);
            clickUsingJS(locator);
        }
    }

    public void clickUsingJS(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        logger.debug("Clicked on element using JS: {}", locator);
    }

    public void sendKeys(By locator, String text) {
        WebElement element = waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
        logger.debug("Typed text '{}' into element: {}", text, locator);
    }

    public String getText(By locator) {
        return waitForVisibility(locator).getText().trim();
    }

    public boolean isDisplayed(By locator) {
        try {
            return waitForVisibility(locator).isDisplayed();
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    public List<WebElement> getElements(By locator) {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        return driver.findElements(locator);
    }

    public void selectByVisibleText(By locator, String visibleText) {
        WebElement element = waitForVisibility(locator);
        Select select = new Select(element);
        select.selectByVisibleText(visibleText);
        logger.debug("Selected '{}' from dropdown: {}", visibleText, locator);
    }

    public void scrollToElement(By locator) {
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}
