package com.saucedemo.base;

import com.saucedemo.driver.DriverFactory;
import com.saucedemo.driver.DriverManager;
import com.saucedemo.listeners.TestListener;
import com.saucedemo.utils.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

@Listeners(TestListener.class)
public abstract class BaseTest {

    protected final Logger logger = LogManager.getLogger(this.getClass());

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser"})
    public void setUp(@Optional("") String browser) {
        String chosenBrowser = (browser != null && !browser.isEmpty()) ? browser : ConfigReader.getBrowser();
        boolean isHeadless = ConfigReader.isHeadless();

        logger.info("Initializing WebDriver for test setup: browser={}, headless={}", chosenBrowser, isHeadless);
        WebDriver driver = DriverFactory.createDriver(chosenBrowser, isHeadless);

        String baseUrl = ConfigReader.getBaseUrl();
        logger.info("Navigating to URL: {}", baseUrl);
        driver.get(baseUrl);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        logger.info("Tearing down WebDriver instance");
        DriverFactory.quitDriver();
    }

    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }
}
