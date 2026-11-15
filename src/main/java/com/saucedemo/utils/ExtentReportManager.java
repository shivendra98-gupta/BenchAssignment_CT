package com.saucedemo.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.saucedemo.constants.FrameworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public final class ExtentReportManager {

    private static final Logger logger = LogManager.getLogger(ExtentReportManager.class);
    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> extentTestThreadLocal = new ThreadLocal<>();

    private ExtentReportManager() {
    }

    public static synchronized ExtentReports initReport() {
        if (extentReports == null) {
            File reportDir = new File(FrameworkConstants.REPORTS_DIR);
            if (!reportDir.exists()) {
                reportDir.mkdirs();
            }

            String reportPath = FrameworkConstants.REPORTS_DIR + "index.html";
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle("SauceDemo Automation Test Execution Report");
            sparkReporter.config().setReportName("SauceDemo E-commerce Checkout Test Automation");
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setEncoding("UTF-8");
            sparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");

            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
            extentReports.setSystemInfo("Operating System", System.getProperty("os.name"));
            extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
            extentReports.setSystemInfo("Browser", ConfigReader.getBrowser());
            extentReports.setSystemInfo("Environment", "QA");
            extentReports.setSystemInfo("Application URL", ConfigReader.getBaseUrl());
            extentReports.setSystemInfo("Author", "Shivendra");

            logger.info("ExtentReports initialized with report path: {}", reportPath);
        }
        return extentReports;
    }

    public static synchronized void flushReport() {
        if (extentReports != null) {
            extentReports.flush();
            logger.info("ExtentReports flushed successfully.");
        }
    }

    public static ExtentTest getTest() {
        return extentTestThreadLocal.get();
    }

    public static void setTest(ExtentTest test) {
        extentTestThreadLocal.set(test);
    }

    public static void unloadTest() {
        extentTestThreadLocal.remove();
    }
}
