package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    // Locators
    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");
    private final By loginLogo = By.className("login_logo");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void enterUsername(String username) {
        logger.info("Entering username: {}", username);
        elementUtil.sendKeys(usernameInput, username);
    }

    public void enterPassword(String password) {
        logger.info("Entering password");
        elementUtil.sendKeys(passwordInput, password);
    }

    public void clickLogin() {
        logger.info("Clicking Login button");
        elementUtil.click(loginButton);
    }

    public ProductsPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        return new ProductsPage(driver);
    }

    public String getErrorMessage() {
        String msg = elementUtil.getText(errorMessage);
        logger.info("Login error message retrieved: {}", msg);
        return msg;
    }

    public boolean isErrorMessageDisplayed() {
        return elementUtil.isDisplayed(errorMessage);
    }

    public boolean isLoginPageDisplayed() {
        return elementUtil.isDisplayed(loginButton) && elementUtil.isDisplayed(loginLogo);
    }
}
