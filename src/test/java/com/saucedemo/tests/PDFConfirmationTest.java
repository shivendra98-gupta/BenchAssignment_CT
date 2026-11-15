package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.constants.FrameworkConstants;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.CheckoutCompletePage;
import com.saucedemo.pages.CheckoutStepOnePage;
import com.saucedemo.pages.CheckoutStepTwoPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductsPage;
import com.saucedemo.utils.PDFValidationUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;

public class PDFConfirmationTest extends BaseTest {

    @Test(description = "Verify generation and Apache PDFBox validation of order confirmation PDF")
    public void testOrderConfirmationPdfValidation() {
        // Step 1: Complete an order on SauceDemo
        LoginPage loginPage = new LoginPage(getDriver());
        ProductsPage productsPage = loginPage.login("standard_user", "secret_sauce");

        String product = "Sauce Labs Backpack";
        productsPage.addProductToCart(product);

        CartPage cartPage = productsPage.clickCart();
        CheckoutStepOnePage stepOnePage = cartPage.clickCheckout();

        String customerFirstName = "Emily";
        String customerLastName = "Clark";
        String customerFullName = customerFirstName + " " + customerLastName;
        stepOnePage.fillCustomerInformation(customerFirstName, customerLastName, "94107");

        CheckoutStepTwoPage stepTwoPage = stepOnePage.clickContinue();
        double totalAmount = stepTwoPage.getTotalAmount();
        String totalAmountStr = "$" + String.format("%.2f", totalAmount);

        CheckoutCompletePage completePage = stepTwoPage.clickFinish();
        String header = completePage.getConfirmationHeader();
        String message = completePage.getConfirmationText();

        // Step 2: Generate Order Confirmation PDF invoice
        String pdfFilePath = FrameworkConstants.REPORTS_DIR + "Order_Confirmation_Receipt.pdf";
        PDFValidationUtil.createOrderConfirmationPdf(
                pdfFilePath,
                header,
                message,
                customerFullName,
                List.of(product),
                totalAmountStr
        );

        // Step 3: Validate PDF document existence and content via Apache PDFBox
        File pdfFile = new File(pdfFilePath);
        Assert.assertTrue(pdfFile.exists(), "Order confirmation PDF file should be generated successfully");

        int pageCount = PDFValidationUtil.getPageCount(pdfFilePath);
        Assert.assertTrue(pageCount >= 1, "PDF page count should be at least 1");

        String pdfText = PDFValidationUtil.extractPdfText(pdfFilePath);
        logger.info("Extracted PDF Content for validation:\n{}", pdfText);

        // Assertions on PDF content
        Assert.assertTrue(pdfText.contains("Swag Labs - Order Confirmation Invoice"),
                "PDF should contain invoice title");
        Assert.assertTrue(pdfText.contains("Status: Thank you for your order!"),
                "PDF should contain confirmation header");
        Assert.assertTrue(pdfText.contains("Message: Your order has been dispatched"),
                "PDF should contain order dispatch message");
        Assert.assertTrue(pdfText.contains("Customer: " + customerFullName),
                "PDF should contain customer name");
        Assert.assertTrue(pdfText.contains(product),
                "PDF should contain purchased product name: " + product);
        Assert.assertTrue(pdfText.contains("Total Amount Paid: " + totalAmountStr),
                "PDF should contain verified total amount paid: " + totalAmountStr);
    }
}
