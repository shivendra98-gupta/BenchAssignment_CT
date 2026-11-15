package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class CheckoutStepTwoPage extends BasePage {

    // Locators
    private final By pageTitle = By.cssSelector(".title");
    private final By cartItems = By.cssSelector(".cart_item");
    private final By itemNames = By.cssSelector(".inventory_item_name");
    private final By itemPrices = By.cssSelector(".inventory_item_price");
    private final By paymentInfoValue = By.cssSelector("[data-test='payment-info-value']");
    private final By shippingInfoValue = By.cssSelector("[data-test='shipping-info-value']");
    private final By subtotalLabel = By.cssSelector(".summary_subtotal_label");
    private final By taxLabel = By.cssSelector(".summary_tax_label");
    private final By totalLabel = By.cssSelector(".summary_total_label");
    private final By finishButton = By.id("finish");
    private final By cancelButton = By.id("cancel");

    public CheckoutStepTwoPage(WebDriver driver) {
        super(driver);
    }

    public boolean isCheckoutStepTwoPageDisplayed() {
        return elementUtil.isDisplayed(pageTitle) &&
                elementUtil.getText(pageTitle).equalsIgnoreCase("Checkout: Overview");
    }

    public List<String> getItemNames() {
        List<WebElement> elements = elementUtil.getElements(itemNames);
        List<String> names = new ArrayList<>();
        for (WebElement el : elements) {
            names.add(el.getText().trim());
        }
        return names;
    }

    public double getItemTotal() {
        String text = elementUtil.getText(subtotalLabel);
        // Format: "Item total: $29.99"
        return parsePrice(text);
    }

    public double getTaxAmount() {
        String text = elementUtil.getText(taxLabel);
        // Format: "Tax: $2.40"
        return parsePrice(text);
    }

    public double getTotalAmount() {
        String text = elementUtil.getText(totalLabel);
        // Format: "Total: $32.39"
        return parsePrice(text);
    }

    public double calculateSumOfItemPrices() {
        List<WebElement> elements = elementUtil.getElements(itemPrices);
        double sum = 0.0;
        for (WebElement el : elements) {
            String text = el.getText().replace("$", "").trim();
            sum += Double.parseDouble(text);
        }
        return Math.round(sum * 100.0) / 100.0;
    }

    public String getPaymentInfo() {
        return elementUtil.getText(paymentInfoValue);
    }

    public String getShippingInfo() {
        return elementUtil.getText(shippingInfoValue);
    }

    public CheckoutCompletePage clickFinish() {
        logger.info("Clicking Finish button on Checkout Step Two");
        elementUtil.click(finishButton);
        return new CheckoutCompletePage(driver);
    }

    public ProductsPage clickCancel() {
        logger.info("Clicking Cancel button on Checkout Step Two");
        elementUtil.click(cancelButton);
        return new ProductsPage(driver);
    }

    private double parsePrice(String text) {
        int dollarIndex = text.indexOf('$');
        if (dollarIndex != -1) {
            return Double.parseDouble(text.substring(dollarIndex + 1).trim());
        }
        throw new IllegalArgumentException("Could not extract price from string: " + text);
    }
}
