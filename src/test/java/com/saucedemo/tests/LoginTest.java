package com.saucedemo.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.saucedemo.base.BaseTest;
import com.saucedemo.constants.FrameworkConstants;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductsPage;
import com.saucedemo.utils.JsonDataReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(description = "Verify successful login with valid standard user credentials")
    public void testValidLogin() {
        LoginPage loginPage = new LoginPage(getDriver());
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page should be displayed initially");

        ProductsPage productsPage = loginPage.login("standard_user", "secret_sauce");
        Assert.assertTrue(productsPage.isProductsPageDisplayed(), "Products page should be displayed upon valid login");
    }

    @Test(description = "Verify login attempt with locked out user")
    public void testLockedOutUserLogin() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login("locked_out_user", "secret_sauce");

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for locked out user");
        Assert.assertEquals(loginPage.getErrorMessage(),
                "Epic sadface: Sorry, this user has been locked out.",
                "Error message text does not match expected");
    }

    @Test(description = "Verify login attempt with invalid password")
    public void testInvalidPasswordLogin() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login("standard_user", "incorrect_password");

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for invalid password");
        Assert.assertEquals(loginPage.getErrorMessage(),
                "Epic sadface: Username and password do not match any user in this service",
                "Error message text does not match expected");
    }

    @Test(description = "Verify login attempt with empty username")
    public void testEmptyUsernameLogin() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login("", "secret_sauce");

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for empty username");
        Assert.assertEquals(loginPage.getErrorMessage(),
                "Epic sadface: Username is required",
                "Error message text does not match expected");
    }

    @Test(description = "Verify login attempt with empty password")
    public void testEmptyPasswordLogin() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login("standard_user", "");

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for empty password");
        Assert.assertEquals(loginPage.getErrorMessage(),
                "Epic sadface: Password is required",
                "Error message text does not match expected");
    }

    @DataProvider(name = "negativeLoginDataProvider")
    public Object[][] getNegativeLoginData() {
        JsonNode rootNode = JsonDataReader.readJsonFile(FrameworkConstants.TEST_DATA_LOGIN_PATH);
        JsonNode invalidUsersNode = rootNode.get("invalidUsers");
        Object[][] data = new Object[invalidUsersNode.size()][3];

        for (int i = 0; i < invalidUsersNode.size(); i++) {
            JsonNode item = invalidUsersNode.get(i);
            data[i][0] = item.get("username").asText();
            data[i][1] = item.get("password").asText();
            data[i][2] = item.get("expectedErrorMessage").asText();
        }
        return data;
    }

    @Test(dataProvider = "negativeLoginDataProvider", description = "Data-driven negative login test using JSON test data")
    public void testNegativeLoginDataDriven(String username, String password, String expectedErrorMessage) {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLogin();

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should appear for invalid login credentials");
        Assert.assertEquals(loginPage.getErrorMessage(), expectedErrorMessage, "Error message mismatch");
    }
}
