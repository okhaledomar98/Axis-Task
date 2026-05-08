package com.axis.automation.core;

import com.axis.automation.config.ConfigReader;
import com.axis.automation.enums.BrowserType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * Factory class for creating and managing WebDriver instances.
 *
 * Uses ThreadLocal to support parallel test execution where each
 * thread gets its own WebDriver instance.
 */
public final class DriverFactory {

    // ThreadLocal ensures each thread has its own WebDriver instance
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverFactory() {
        throw new UnsupportedOperationException("DriverFactory is a utility class");
    }

    /**
     * Initializes a WebDriver instance based on the browser type.
     *
     * @param browserType the type of browser to launch
     */
    public static void initDriver(BrowserType browserType) {
        WebDriver driver = createDriver(browserType);
        configureDriver(driver);
        driverThreadLocal.set(driver);
    }

    /**
     * Returns the WebDriver instance for the current thread.
     *
     * @return the WebDriver instance
     * @throws IllegalStateException if driver was not initialized
     */
    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();

        if (driver == null) {
            throw new IllegalStateException(
                    "WebDriver not initialized. Call initDriver() first."
            );
        }

        return driver;
    }

    /**
     * Quits the WebDriver and removes it from ThreadLocal.
     * Important to call this in @AfterMethod to prevent memory leaks.
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();

        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
        }
    }

    /**
     * Creates a WebDriver instance based on browser type.
     */
    private static WebDriver createDriver(BrowserType browserType) {
        boolean headless = ConfigReader.getBoolean("headless");

        return switch (browserType) {
            case CHROME -> createChromeDriver(headless);
            case FIREFOX -> createFirefoxDriver(headless);
            case EDGE -> createEdgeDriver(headless);
        };
    }

    /**
     * Creates and configures a Chrome WebDriver.
     */
    private static WebDriver createChromeDriver(boolean headless) {
        ChromeOptions options = new ChromeOptions();

        if (headless) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--remote-allow-origins=*");

        return new ChromeDriver(options);
    }

    /**
     * Creates and configures a Firefox WebDriver.
     */
    private static WebDriver createFirefoxDriver(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();

        if (headless) {
            options.addArguments("--headless");
        }

        return new FirefoxDriver(options);
    }

    /**
     * Creates and configures an Edge WebDriver.
     */
    private static WebDriver createEdgeDriver(boolean headless) {
        EdgeOptions options = new EdgeOptions();

        if (headless) {
            options.addArguments("--headless=new");
        }

        return new EdgeDriver(options);
    }

    /**
     * Applies common configuration to the WebDriver.
     */
    private static void configureDriver(WebDriver driver) {
        boolean maximize = ConfigReader.getBoolean("maximize.window");
        int pageLoadTimeout = ConfigReader.getInt("page.load.timeout");

        if (maximize) {
            driver.manage().window().maximize();
        }

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
    }
}