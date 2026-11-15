package com.saucedemo.constants;

import java.io.File;

public final class FrameworkConstants {

    private FrameworkConstants() {
        // Prevent instantiation
    }

    public static final String USER_DIR = System.getProperty("user.dir");
    public static final String CONFIG_FILE_PATH = USER_DIR + File.separator + "src" + File.separator + "main"
            + File.separator + "resources" + File.separator + "config" + File.separator + "config.properties";

    public static final String TEST_DATA_LOGIN_PATH = USER_DIR + File.separator + "src" + File.separator + "test"
            + File.separator + "resources" + File.separator + "testdata" + File.separator + "logindata.json";

    public static final String TEST_DATA_CHECKOUT_PATH = USER_DIR + File.separator + "src" + File.separator + "test"
            + File.separator + "resources" + File.separator + "testdata" + File.separator + "checkoutdata.json";

    public static final String REPORTS_DIR = USER_DIR + File.separator + "reports" + File.separator;
    public static final String SCREENSHOTS_DIR = USER_DIR + File.separator + "screenshots" + File.separator;
    public static final String LOGS_DIR = USER_DIR + File.separator + "logs" + File.separator;

    public static final int DEFAULT_EXPLICIT_WAIT = 15;
    public static final int DEFAULT_IMPLICIT_WAIT = 10;
    public static final int DEFAULT_PAGE_LOAD_TIMEOUT = 30;
}
