package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class CartPage extends BasePage {

    // Locators
    private final By pageTitle = By.cssSelector(".title");
    private final By cartItems = By.cssSelector(".cart_item");
    private final By cartItemNames = By.cssSelector(".inventory_item_name");
    private final By cartItemPrices = By.cssSelector(".inventory_item_price");
    private final By continueShoppingButton = By.id("continue-shopping");
    private final By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isCartPageDisplayed() {
        return elementUtil.isDisplayed(pageTitle) && elementUtil.getText(pageTitle).equalsIgnoreCase("Your Cart");
    }

    public List<String> getCartItemNames() {
        List<String> names = new ArrayList<>();
        if (!elementUtil.isDisplayed(cartItems)) {
            return names;
        }
        List<WebElement> elements = elementUtil.getElements(cartItemNames);
        for (WebElement el : elements) {
            names.add(el.getText().trim());
        }
        return names;
    }

    public void removeItem(String productName) {
        logger.info("Removing item from cart: {}", productName);
        String formattedName = productName.toLowerCase().replace(" ", "-");
        By removeBtn = By.cssSelector("[data-test='remove-" + formattedName + "']");
        elementUtil.click(removeBtn);
    }

    public ProductsPage clickContinueShopping() {
        logger.info("Clicking Continue Shopping");
        elementUtil.click(continueShoppingButton);
        return new ProductsPage(driver);
    }

    public CheckoutStepOnePage clickCheckout() {
        logger.info("Clicking Checkout button");
        elementUtil.click(checkoutButton);
        return new CheckoutStepOnePage(driver);
    }

    public int getCartItemCount() {
        try {
            return elementUtil.getElements(cartItems).size();
        } catch (Exception e) {
            return 0;
        }
    }
}
