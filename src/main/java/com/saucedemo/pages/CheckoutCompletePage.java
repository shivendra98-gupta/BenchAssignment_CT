package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutCompletePage extends BasePage {

    // Locators
    private final By pageTitle = By.cssSelector(".title");
    private final By completeHeader = By.cssSelector(".complete-header");
    private final By completeText = By.cssSelector(".complete-text");
    private final By ponyExpressImage = By.cssSelector(".pony_express");
    private final By backHomeButton = By.id("back-to-products");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public boolean isCheckoutCompletePageDisplayed() {
        return elementUtil.isDisplayed(pageTitle) &&
                elementUtil.getText(pageTitle).equalsIgnoreCase("Checkout: Complete!");
    }

    public String getConfirmationHeader() {
        return elementUtil.getText(completeHeader);
    }

    public String getConfirmationText() {
        return elementUtil.getText(completeText);
    }

    public boolean isPonyExpressImageDisplayed() {
        return elementUtil.isDisplayed(ponyExpressImage);
    }

    public ProductsPage clickBackHome() {
        logger.info("Clicking Back Home button on Checkout Complete page");
        elementUtil.click(backHomeButton);
        return new ProductsPage(driver);
    }
}
