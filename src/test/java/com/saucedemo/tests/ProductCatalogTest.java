package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductCatalogTest extends BaseTest {

    private ProductsPage productsPage;

    @BeforeMethod(alwaysRun = true)
    public void loginAndNavigateToProducts() {
        LoginPage loginPage = new LoginPage(getDriver());
        productsPage = loginPage.login("standard_user", "secret_sauce");
        Assert.assertTrue(productsPage.isProductsPageDisplayed(), "Products page must be displayed");
    }

    @Test(description = "Verify product inventory list is displayed and contains items")
    public void testProductInventoryDisplay() {
        List<String> productNames = productsPage.getAllProductNames();
        Assert.assertFalse(productNames.isEmpty(), "Product catalog should not be empty");
        Assert.assertTrue(productNames.contains("Sauce Labs Backpack"), "Product catalog should contain Sauce Labs Backpack");
    }

    @Test(description = "Verify sorting products by price low to high")
    public void testSortProductsPriceLowToHigh() {
        productsPage.selectSortOption("Price (low to high)");
        List<Double> actualPrices = productsPage.getAllProductPrices();

        List<Double> sortedPrices = new ArrayList<>(actualPrices);
        Collections.sort(sortedPrices);

        Assert.assertEquals(actualPrices, sortedPrices, "Products are not sorted by price in ascending order");
    }

    @Test(description = "Verify sorting products by price high to low")
    public void testSortProductsPriceHighToLow() {
        productsPage.selectSortOption("Price (high to low)");
        List<Double> actualPrices = productsPage.getAllProductPrices();

        List<Double> sortedPrices = new ArrayList<>(actualPrices);
        sortedPrices.sort(Collections.reverseOrder());

        Assert.assertEquals(actualPrices, sortedPrices, "Products are not sorted by price in descending order");
    }

    @Test(description = "Verify adding and removing products updates the cart badge")
    public void testAddAndRemoveProductFromCatalog() {
        Assert.assertEquals(productsPage.getCartBadgeCount(), 0, "Cart badge should initially be 0");

        productsPage.addProductToCart("Sauce Labs Backpack");
        Assert.assertEquals(productsPage.getCartBadgeCount(), 1, "Cart badge count should be 1 after adding an item");

        productsPage.addProductToCart("Sauce Labs Bike Light");
        Assert.assertEquals(productsPage.getCartBadgeCount(), 2, "Cart badge count should be 2 after adding second item");

        productsPage.removeProductFromCart("Sauce Labs Backpack");
        Assert.assertEquals(productsPage.getCartBadgeCount(), 1, "Cart badge count should be 1 after removing an item");
    }
}
