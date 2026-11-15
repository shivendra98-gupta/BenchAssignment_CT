package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutStepOnePage extends BasePage {

    // Locators
    private final By pageTitle = By.cssSelector(".title");
    private final By firstNameInput = By.id("first-name");
    private final By lastNameInput = By.id("last-name");
    private final By postalCodeInput = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By cancelButton = By.id("cancel");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public CheckoutStepOnePage(WebDriver driver) {
        super(driver);
    }

    public boolean isCheckoutStepOnePageDisplayed() {
        return elementUtil.isDisplayed(pageTitle) &&
                elementUtil.getText(pageTitle).equalsIgnoreCase("Checkout: Your Information");
    }

    public void enterFirstName(String firstName) {
        logger.info("Entering first name: {}", firstName);
        elementUtil.sendKeys(firstNameInput, firstName);
    }

    public void enterLastName(String lastName) {
        logger.info("Entering last name: {}", lastName);
        elementUtil.sendKeys(lastNameInput, lastName);
    }

    public void enterPostalCode(String postalCode) {
        logger.info("Entering postal code: {}", postalCode);
        elementUtil.sendKeys(postalCodeInput, postalCode);
    }

    public void fillCustomerInformation(String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
    }

    public CheckoutStepTwoPage clickContinue() {
        logger.info("Clicking Continue button to proceed to Checkout Step Two");
        elementUtil.click(continueButton);
        return new CheckoutStepTwoPage(driver);
    }

    public CartPage clickCancel() {
        logger.info("Clicking Cancel button in Checkout Step One");
        elementUtil.click(cancelButton);
        return new CartPage(driver);
    }

    public String getErrorMessage() {
        return elementUtil.getText(errorMessage);
    }

    public boolean isErrorMessageDisplayed() {
        return elementUtil.isDisplayed(errorMessage);
    }
}
