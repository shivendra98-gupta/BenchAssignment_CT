package com.saucedemo.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class PDFValidationUtil {

    private static final Logger logger = LogManager.getLogger(PDFValidationUtil.class);

    private PDFValidationUtil() {
    }

    /**
     * Extracts all text from a given PDF file.
     *
     * @param pdfFilePath Absolute path to the PDF file.
     * @return Extracted text content.
     */
    public static String extractPdfText(String pdfFilePath) {
        File file = new File(pdfFilePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("PDF file not found at: " + pdfFilePath);
        }

        try (PDDocument document = Loader.loadPDF(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            logger.info("Successfully extracted {} characters of text from PDF: {}", text.length(), pdfFilePath);
            return text;
        } catch (IOException e) {
            logger.error("Failed to read PDF file at {}: {}", pdfFilePath, e.getMessage(), e);
            throw new RuntimeException("Error reading PDF file: " + pdfFilePath, e);
        }
    }

    /**
     * Gets the total page count of a PDF document.
     */
    public static int getPageCount(String pdfFilePath) {
        File file = new File(pdfFilePath);
        try (PDDocument document = Loader.loadPDF(file)) {
            return document.getNumberOfPages();
        } catch (IOException e) {
            logger.error("Failed to count pages for PDF at {}: {}", pdfFilePath, e.getMessage(), e);
            throw new RuntimeException("Error getting page count: " + pdfFilePath, e);
        }
    }

    /**
     * Validates whether the PDF contains an expected text snippet.
     */
    public static boolean validatePdfContainsText(String pdfFilePath, String expectedText) {
        String pdfText = extractPdfText(pdfFilePath);
        boolean contains = pdfText.contains(expectedText);
        logger.info("PDF contains text '{}': {}", expectedText, contains);
        return contains;
    }

    /**
     * Generates a sample Order Confirmation PDF for testing and validation purposes.
     */
    public static void createOrderConfirmationPdf(String destinationPath,
                                                 String orderHeader,
                                                 String confirmationMsg,
                                                 String customerName,
                                                 List<String> items,
                                                 String totalAmount) {
        File targetFile = new File(destinationPath);
        File parentDir = targetFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 18);
                contentStream.setLeading(22f);
                contentStream.newLineAtOffset(50, 750);

                contentStream.showText("Swag Labs - Order Confirmation Invoice");
                contentStream.newLine();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                contentStream.newLine();

                contentStream.showText("Status: " + orderHeader);
                contentStream.newLine();
                contentStream.showText("Message: " + confirmationMsg);
                contentStream.newLine();
                contentStream.showText("Customer: " + customerName);
                contentStream.newLine();
                contentStream.newLine();

                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
                contentStream.showText("Ordered Items:");
                contentStream.newLine();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);

                for (String item : items) {
                    contentStream.showText("- " + item);
                    contentStream.newLine();
                }

                contentStream.newLine();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
                contentStream.showText("Total Amount Paid: " + totalAmount);
                contentStream.newLine();

                contentStream.endText();
            }

            document.save(destinationPath);
            logger.info("Generated sample order confirmation PDF at: {}", destinationPath);
        } catch (IOException e) {
            logger.error("Failed to generate order confirmation PDF: {}", e.getMessage(), e);
            throw new RuntimeException("Error generating PDF: " + destinationPath, e);
        }
    }
}
