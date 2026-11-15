package com.saucedemo.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.saucedemo.utils.ConfigReader;
import com.saucedemo.utils.ExtentReportManager;
import com.saucedemo.utils.ScreenshotUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(TestListener.class);
    private ExtentReports extent;

    @Override
    public void onStart(ITestContext context) {
        logger.info("========== Starting Test Suite: {} ==========", context.getName());
        extent = ExtentReportManager.initReport();
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("========== Finished Test Suite: {} ==========", context.getName());
        ExtentReportManager.flushReport();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        if (description == null || description.isEmpty()) {
            description = testName;
        }

        logger.info("----- Starting Test Method: {} -----", testName);
        ExtentTest test = extent.createTest(testName, description);
        test.assignCategory(result.getTestClass().getRealClass().getSimpleName());
        ExtentReportManager.setTest(test);
        ExtentReportManager.getTest().log(Status.INFO, "Test execution started for: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.info("Test Passed: {}", testName);
        if (ExtentReportManager.getTest() != null) {
            ExtentReportManager.getTest().log(Status.PASS, "Test PASSED: " + testName);
            ExtentReportManager.unloadTest();
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.error("Test Failed: {}. Cause: {}", testName, result.getThrowable());

        if (ExtentReportManager.getTest() != null) {
            ExtentReportManager.getTest().log(Status.FAIL, "Test FAILED: " + testName);
            ExtentReportManager.getTest().log(Status.FAIL, result.getThrowable());

            if (ConfigReader.isScreenshotOnFailure()) {
                try {
                    String base64Screenshot = ScreenshotUtil.captureScreenshotAsBase64();
                    ScreenshotUtil.captureScreenshotAsFile(testName);
                    if (base64Screenshot != null && !base64Screenshot.isEmpty()) {
                        ExtentReportManager.getTest().fail(
                                "Screenshot on failure:",
                                MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build()
                        );
                    }
                } catch (Exception e) {
                    logger.error("Failed to attach screenshot to report: {}", e.getMessage(), e);
                }
            }
            ExtentReportManager.unloadTest();
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.warn("Test Skipped: {}", testName);
        if (ExtentReportManager.getTest() != null) {
            ExtentReportManager.getTest().log(Status.SKIP, "Test SKIPPED: " + testName);
            if (result.getThrowable() != null) {
                ExtentReportManager.getTest().log(Status.SKIP, result.getThrowable());
            }
            ExtentReportManager.unloadTest();
        }
    }
}
