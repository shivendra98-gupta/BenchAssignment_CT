package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class ProductsPage extends BasePage {

    // Locators
    private final By pageTitle = By.cssSelector(".title");
    private final By inventoryItems = By.cssSelector(".inventory_item");
    private final By itemNames = By.cssSelector(".inventory_item_name");
    private final By itemPrices = By.cssSelector(".inventory_item_price");
    private final By sortDropdown = By.cssSelector("[data-test='product-sort-container']");
    private final By shoppingCartLink = By.cssSelector(".shopping_cart_link");
    private final By shoppingCartBadge = By.cssSelector(".shopping_cart_badge");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isProductsPageDisplayed() {
        return elementUtil.isDisplayed(pageTitle) && elementUtil.getText(pageTitle).equalsIgnoreCase("Products");
    }

    public List<String> getAllProductNames() {
        List<WebElement> elements = elementUtil.getElements(itemNames);
        List<String> names = new ArrayList<>();
        for (WebElement el : elements) {
            names.add(el.getText().trim());
        }
        return names;
    }

    public List<Double> getAllProductPrices() {
        List<WebElement> elements = elementUtil.getElements(itemPrices);
        List<Double> prices = new ArrayList<>();
        for (WebElement el : elements) {
            String text = el.getText().replace("$", "").trim();
            prices.add(Double.parseDouble(text));
        }
        return prices;
    }

    public void addProductToCart(String productName) {
        logger.info("Adding product to cart: {}", productName);
        String formattedName = productName.toLowerCase().replace(" ", "-");
        By addToCartBtn = By.cssSelector("[data-test='add-to-cart-" + formattedName + "']");
        elementUtil.click(addToCartBtn);
    }

    public void removeProductFromCart(String productName) {
        logger.info("Removing product from cart: {}", productName);
        String formattedName = productName.toLowerCase().replace(" ", "-");
        By removeBtn = By.cssSelector("[data-test='remove-" + formattedName + "']");
        elementUtil.click(removeBtn);
    }

    public int getCartBadgeCount() {
        if (elementUtil.isDisplayed(shoppingCartBadge)) {
            String countText = elementUtil.getText(shoppingCartBadge);
            return Integer.parseInt(countText);
        }
        return 0;
    }

    public void selectSortOption(String sortOption) {
        logger.info("Sorting products by: {}", sortOption);
        elementUtil.selectByVisibleText(sortDropdown, sortOption);
    }

    public CartPage clickCart() {
        logger.info("Navigating to Cart Page");
        elementUtil.click(shoppingCartLink);
        return new CartPage(driver);
    }
}
