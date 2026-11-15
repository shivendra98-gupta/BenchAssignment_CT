package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.CheckoutCompletePage;
import com.saucedemo.pages.CheckoutStepOnePage;
import com.saucedemo.pages.CheckoutStepTwoPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class EndToEndCheckoutTest extends BaseTest {

    @Test(description = "Automate complete e-commerce checkout flow with total price calculation & confirmation assertion")
    public void testCompleteCheckoutFlowSingleItem() {
        // Step 1: Log in
        LoginPage loginPage = new LoginPage(getDriver());
        ProductsPage productsPage = loginPage.login("standard_user", "secret_sauce");
        Assert.assertTrue(productsPage.isProductsPageDisplayed(), "Products page should be displayed after login");

        // Step 2: Add item to cart and proceed to checkout
        String selectedProduct = "Sauce Labs Backpack";
        productsPage.addProductToCart(selectedProduct);
        Assert.assertEquals(productsPage.getCartBadgeCount(), 1, "Cart count should be 1");

        CartPage cartPage = productsPage.clickCart();
        Assert.assertTrue(cartPage.isCartPageDisplayed(), "Cart page should be displayed");
        Assert.assertTrue(cartPage.getCartItemNames().contains(selectedProduct), "Item must be present in cart");

        CheckoutStepOnePage stepOnePage = cartPage.clickCheckout();
        Assert.assertTrue(stepOnePage.isCheckoutStepOnePageDisplayed(), "Checkout Step One page should be displayed");

        // Step 3: Fill customer information and validate totals
        stepOnePage.fillCustomerInformation("Alice", "Smith", "90210");
        CheckoutStepTwoPage stepTwoPage = stepOnePage.clickContinue();
        Assert.assertTrue(stepTwoPage.isCheckoutStepTwoPageDisplayed(), "Checkout Step Two page should be displayed");

        double sumOfItems = stepTwoPage.calculateSumOfItemPrices();
        double itemTotal = stepTwoPage.getItemTotal();
        double tax = stepTwoPage.getTaxAmount();
        double total = stepTwoPage.getTotalAmount();

        // Total price assertions
        Assert.assertEquals(itemTotal, sumOfItems, "Item total does not match the sum of item prices");
        double expectedTotal = Math.round((itemTotal + tax) * 100.0) / 100.0;
        Assert.assertEquals(total, expectedTotal, 0.01, "Total amount does not equal Item total + Tax");

        // Step 4: Complete the order and validate confirmation message
        CheckoutCompletePage completePage = stepTwoPage.clickFinish();
        Assert.assertTrue(completePage.isCheckoutCompletePageDisplayed(), "Checkout Complete page should be displayed");
        Assert.assertEquals(completePage.getConfirmationHeader(),
                "Thank you for your order!",
                "Order confirmation header mismatch");
        Assert.assertTrue(completePage.getConfirmationText().contains("Your order has been dispatched"),
                "Order confirmation text does not contain expected dispatch notice");
        Assert.assertTrue(completePage.isPonyExpressImageDisplayed(), "Pony Express image should be displayed");

        ProductsPage returnedHome = completePage.clickBackHome();
        Assert.assertTrue(returnedHome.isProductsPageDisplayed(), "Should return to Products page after Back Home");
    }

    @Test(description = "Complete checkout flow with multiple items and price aggregation assertions")
    public void testCompleteCheckoutFlowMultipleItems() {
        LoginPage loginPage = new LoginPage(getDriver());
        ProductsPage productsPage = loginPage.login("standard_user", "secret_sauce");

        // Add multiple items
        List<String> itemsToAdd = List.of(
                "Sauce Labs Backpack",
                "Sauce Labs Bike Light",
                "Sauce Labs Bolt T-Shirt"
        );
        for (String item : itemsToAdd) {
            productsPage.addProductToCart(item);
        }
        Assert.assertEquals(productsPage.getCartBadgeCount(), itemsToAdd.size(),
                "Cart badge count should match number of added items");

        CartPage cartPage = productsPage.clickCart();
        List<String> cartItems = cartPage.getCartItemNames();
        for (String item : itemsToAdd) {
            Assert.assertTrue(cartItems.contains(item), "Cart missing item: " + item);
        }

        CheckoutStepOnePage stepOnePage = cartPage.clickCheckout();
        stepOnePage.fillCustomerInformation("Robert", "Johnson", "60601");

        CheckoutStepTwoPage stepTwoPage = stepOnePage.clickContinue();
        Assert.assertEquals(stepTwoPage.getItemNames().size(), itemsToAdd.size(),
                "Step Two items count should match");

        double sumOfItems = stepTwoPage.calculateSumOfItemPrices();
        double itemTotal = stepTwoPage.getItemTotal();
        double tax = stepTwoPage.getTaxAmount();
        double total = stepTwoPage.getTotalAmount();

        Assert.assertEquals(itemTotal, sumOfItems, "Item total mismatch on multi-item checkout");
        double expectedTotal = Math.round((itemTotal + tax) * 100.0) / 100.0;
        Assert.assertEquals(total, expectedTotal, 0.01, "Grand total mismatch on multi-item checkout");

        CheckoutCompletePage completePage = stepTwoPage.clickFinish();
        Assert.assertEquals(completePage.getConfirmationHeader(), "Thank you for your order!");
    }
}
