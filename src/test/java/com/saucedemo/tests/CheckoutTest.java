package com.saucedemo.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.saucedemo.base.BaseTest;
import com.saucedemo.constants.FrameworkConstants;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.CheckoutStepOnePage;
import com.saucedemo.pages.CheckoutStepTwoPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductsPage;
import com.saucedemo.utils.JsonDataReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CheckoutTest extends BaseTest {

    private CheckoutStepOnePage checkoutStepOnePage;

    @BeforeMethod(alwaysRun = true)
    public void setupCartAndNavigateToCheckout() {
        LoginPage loginPage = new LoginPage(getDriver());
        ProductsPage productsPage = loginPage.login("standard_user", "secret_sauce");
        productsPage.addProductToCart("Sauce Labs Backpack");

        CartPage cartPage = productsPage.clickCart();
        checkoutStepOnePage = cartPage.clickCheckout();
        Assert.assertTrue(checkoutStepOnePage.isCheckoutStepOnePageDisplayed(),
                "Checkout Step One page should be displayed");
    }

    @Test(description = "Verify successful submission of valid customer checkout information")
    public void testValidCustomerInformationSubmission() {
        checkoutStepOnePage.fillCustomerInformation("John", "Doe", "12345");
        CheckoutStepTwoPage stepTwoPage = checkoutStepOnePage.clickContinue();

        Assert.assertTrue(stepTwoPage.isCheckoutStepTwoPageDisplayed(),
                "Should successfully navigate to Checkout Step Two Overview");
    }

    @Test(description = "Verify error message when First Name is empty")
    public void testEmptyFirstNameValidation() {
        checkoutStepOnePage.fillCustomerInformation("", "Doe", "12345");
        checkoutStepOnePage.clickContinue();

        Assert.assertTrue(checkoutStepOnePage.isErrorMessageDisplayed(), "Error message should be displayed");
        Assert.assertEquals(checkoutStepOnePage.getErrorMessage(),
                "Error: First Name is required", "Error message mismatch");
    }

    @Test(description = "Verify error message when Last Name is empty")
    public void testEmptyLastNameValidation() {
        checkoutStepOnePage.fillCustomerInformation("John", "", "12345");
        checkoutStepOnePage.clickContinue();

        Assert.assertTrue(checkoutStepOnePage.isErrorMessageDisplayed(), "Error message should be displayed");
        Assert.assertEquals(checkoutStepOnePage.getErrorMessage(),
                "Error: Last Name is required", "Error message mismatch");
    }

    @Test(description = "Verify error message when Postal Code is empty")
    public void testEmptyPostalCodeValidation() {
        checkoutStepOnePage.fillCustomerInformation("John", "Doe", "");
        checkoutStepOnePage.clickContinue();

        Assert.assertTrue(checkoutStepOnePage.isErrorMessageDisplayed(), "Error message should be displayed");
        Assert.assertEquals(checkoutStepOnePage.getErrorMessage(),
                "Error: Postal Code is required", "Error message mismatch");
    }

    @DataProvider(name = "negativeCheckoutDataProvider")
    public Object[][] getNegativeCheckoutData() {
        JsonNode rootNode = JsonDataReader.readJsonFile(FrameworkConstants.TEST_DATA_CHECKOUT_PATH);
        JsonNode scenarios = rootNode.get("negativeCheckoutScenarios");
        Object[][] data = new Object[scenarios.size()][4];

        for (int i = 0; i < scenarios.size(); i++) {
            JsonNode item = scenarios.get(i);
            data[i][0] = item.get("firstName").asText();
            data[i][1] = item.get("lastName").asText();
            data[i][2] = item.get("postalCode").asText();
            data[i][3] = item.get("expectedErrorMessage").asText();
        }
        return data;
    }

    @Test(dataProvider = "negativeCheckoutDataProvider", description = "Data-driven negative checkout information validation")
    public void testNegativeCheckoutDataDriven(String firstName, String lastName, String postalCode, String expectedErrorMessage) {
        checkoutStepOnePage.fillCustomerInformation(firstName, lastName, postalCode);
        checkoutStepOnePage.clickContinue();

        Assert.assertTrue(checkoutStepOnePage.isErrorMessageDisplayed(), "Error message should appear");
        Assert.assertEquals(checkoutStepOnePage.getErrorMessage(), expectedErrorMessage, "Error message mismatch");
    }

    @Test(description = "Verify cancel button on Checkout Step One navigates back to Cart")
    public void testCancelCheckoutNavigation() {
        CartPage cartPage = checkoutStepOnePage.clickCancel();
        Assert.assertTrue(cartPage.isCartPageDisplayed(), "Cancel should navigate back to Cart page");
    }
}
