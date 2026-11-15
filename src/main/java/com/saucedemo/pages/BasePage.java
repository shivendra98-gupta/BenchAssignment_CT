package com.saucedemo.pages;

import com.saucedemo.utils.ElementUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final ElementUtil elementUtil;
    protected final Logger logger;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.elementUtil = new ElementUtil(driver);
        this.logger = LogManager.getLogger(this.getClass());
    }

    public String getPageTitle() {
        return elementUtil.getPageTitle();
    }

    public String getCurrentUrl() {
        return elementUtil.getCurrentUrl();
    }
}
