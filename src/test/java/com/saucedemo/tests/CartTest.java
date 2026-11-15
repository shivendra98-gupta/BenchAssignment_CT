package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.CheckoutStepOnePage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class CartTest extends BaseTest {

    private ProductsPage productsPage;

    @BeforeMethod(alwaysRun = true)
    public void loginAndNavigateToProducts() {
        LoginPage loginPage = new LoginPage(getDriver());
        productsPage = loginPage.login("standard_user", "secret_sauce");
    }

    @Test(description = "Verify items added to cart are displayed accurately on Cart page")
    public void testCartItemDisplay() {
        String item = "Sauce Labs Backpack";
        productsPage.addProductToCart(item);

        CartPage cartPage = productsPage.clickCart();
        Assert.assertTrue(cartPage.isCartPageDisplayed(), "Cart page should be displayed");

        List<String> itemsInCart = cartPage.getCartItemNames();
        Assert.assertTrue(itemsInCart.contains(item), "Cart should contain the added item: " + item);
    }

    @Test(description = "Verify removing an item directly from Cart page")
    public void testRemoveItemFromCart() {
        productsPage.addProductToCart("Sauce Labs Backpack");
        productsPage.addProductToCart("Sauce Labs Bike Light");

        CartPage cartPage = productsPage.clickCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Cart should have 2 items");

        cartPage.removeItem("Sauce Labs Backpack");
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Cart should have 1 item after removal");
        Assert.assertFalse(cartPage.getCartItemNames().contains("Sauce Labs Backpack"), "Removed item should not be present");
    }

    @Test(description = "Verify clicking Continue Shopping navigates back to Products page")
    public void testContinueShoppingNavigation() {
        CartPage cartPage = productsPage.clickCart();
        ProductsPage returnedProductsPage = cartPage.clickContinueShopping();
        Assert.assertTrue(returnedProductsPage.isProductsPageDisplayed(), "Should return to Products page");
    }

    @Test(description = "Verify clicking Checkout navigates to Checkout Step One page")
    public void testProceedToCheckoutNavigation() {
        productsPage.addProductToCart("Sauce Labs Backpack");
        CartPage cartPage = productsPage.clickCart();

        CheckoutStepOnePage stepOnePage = cartPage.clickCheckout();
        Assert.assertTrue(stepOnePage.isCheckoutStepOnePageDisplayed(), "Should navigate to Checkout Step One page");
    }
}
