package com.saucedemo.utils;

import com.saucedemo.constants.FrameworkConstants;
import com.saucedemo.driver.DriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class ScreenshotUtil {

    private static final Logger logger = LogManager.getLogger(ScreenshotUtil.class);

    private ScreenshotUtil() {
    }

    public static String captureScreenshotAsFile(String testName) {
        WebDriver driver = DriverManager.getDriver();
        if (driver == null) {
            logger.warn("WebDriver is null. Cannot capture screenshot for test: {}", testName);
            return "";
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
        String fileName = testName + "_" + timestamp + ".png";
        String destinationPath = FrameworkConstants.SCREENSHOTS_DIR + fileName;

        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File destFile = new File(destinationPath);

        try {
            FileUtils.copyFile(srcFile, destFile);
            logger.info("Screenshot successfully captured: {}", destinationPath);
            return destinationPath;
        } catch (IOException e) {
            logger.error("Failed to save screenshot to {}: {}", destinationPath, e.getMessage(), e);
            return "";
        }
    }

    public static String captureScreenshotAsBase64() {
        WebDriver driver = DriverManager.getDriver();
        if (driver == null) {
            return "";
        }
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }
}
