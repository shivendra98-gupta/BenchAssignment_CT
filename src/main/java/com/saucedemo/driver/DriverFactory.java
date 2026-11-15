package com.saucedemo.driver;

import com.saucedemo.utils.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

public final class DriverFactory {

    private static final Logger logger = LogManager.getLogger(DriverFactory.class);

    private DriverFactory() {
    }

    public static WebDriver createDriver(String browserName, boolean isHeadless) {
        WebDriver driver;
        String browser = (browserName == null || browserName.trim().isEmpty())
                ? ConfigReader.getBrowser()
                : browserName.toLowerCase().trim();

        logger.info("Initializing browser: {} (Headless: {})", browser, isHeadless);

        switch (browser) {
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (isHeadless) {
                    firefoxOptions.addArguments("-headless");
                }
                firefoxOptions.addArguments("--private");
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                if (isHeadless) {
                    edgeOptions.addArguments("--headless=new");
                }
                edgeOptions.addArguments("--inprivate");
                edgeOptions.addArguments("--remote-allow-origins=*");
                driver = new EdgeDriver(edgeOptions);
                break;

            case "chrome":
            default:
                ChromeOptions chromeOptions = new ChromeOptions();
                if (isHeadless) {
                    chromeOptions.addArguments("--headless=new");
                }
                chromeOptions.addArguments("--incognito");
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--disable-extensions");
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--remote-allow-origins=*");
                driver = new ChromeDriver(chromeOptions);
                break;
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getPageLoadTimeout()));
        driver.manage().window().maximize();

        DriverManager.setDriver(driver);
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = DriverManager.getDriver();
        if (driver != null) {
            try {
                driver.quit();
                logger.info("WebDriver instance quit successfully.");
            } catch (Exception e) {
                logger.error("Error while quitting WebDriver: {}", e.getMessage());
            } finally {
                DriverManager.unload();
            }
        }
    }
}
