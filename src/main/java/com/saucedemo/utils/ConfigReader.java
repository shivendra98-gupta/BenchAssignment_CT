package com.saucedemo.utils;

import com.saucedemo.constants.FrameworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class ConfigReader {

    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static final Properties properties = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream(FrameworkConstants.CONFIG_FILE_PATH)) {
            properties.load(fis);
            logger.info("Configuration properties loaded successfully from {}", FrameworkConstants.CONFIG_FILE_PATH);
        } catch (IOException e) {
            logger.error("Failed to load configuration properties file: {}", e.getMessage(), e);
            throw new RuntimeException("Could not load config.properties file at " + FrameworkConstants.CONFIG_FILE_PATH, e);
        }
    }

    private ConfigReader() {
    }

    public static String getProperty(String key) {
        // System property takes precedence over config.properties file
        String systemProperty = System.getProperty(key);
        if (systemProperty != null && !systemProperty.trim().isEmpty()) {
            return systemProperty.trim();
        }
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Property key '{}' not found in config.properties", key);
            return "";
        }
        return value.trim();
    }

    public static String getBaseUrl() {
        return getProperty("app.url");
    }

    public static String getBrowser() {
        String browser = getProperty("browser");
        return (browser != null && !browser.isEmpty()) ? browser.toLowerCase() : "chrome";
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless"));
    }

    public static int getImplicitWait() {
        try {
            return Integer.parseInt(getProperty("implicit.wait"));
        } catch (NumberFormatException e) {
            return FrameworkConstants.DEFAULT_IMPLICIT_WAIT;
        }
    }

    public static int getExplicitWait() {
        try {
            return Integer.parseInt(getProperty("explicit.wait"));
        } catch (NumberFormatException e) {
            return FrameworkConstants.DEFAULT_EXPLICIT_WAIT;
        }
    }

    public static int getPageLoadTimeout() {
        try {
            return Integer.parseInt(getProperty("page.load.timeout"));
        } catch (NumberFormatException e) {
            return FrameworkConstants.DEFAULT_PAGE_LOAD_TIMEOUT;
        }
    }

    public static boolean isScreenshotOnFailure() {
        return Boolean.parseBoolean(getProperty("screenshot.on.failure"));
    }
}
