package com.axis.automation.core;

import com.axis.automation.config.ConfigReader;
import com.axis.automation.enums.BrowserType;
import com.axis.automation.pages.HomePage;
import com.axis.automation.utils.LoggerUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

/**
 * Abstract base class for all Test classes.
 * Handles driver lifecycle and consent modal on startup.
 */
public abstract class BaseTest {

    protected WebDriver driver;
    protected HomePage homePage;

    // ─── Setup ───────────────────────────────────────────────────

    @BeforeMethod(alwaysRun = true)
    @Parameters("browser")
    public void setUp(@Optional String browser) {
        // 1. Determine browser from parameter or config fallback
        String browserName = (browser != null && !browser.isEmpty())
                ? browser
                : ConfigReader.get("browser", "chrome");

        // 2. Init driver
        DriverFactory.initDriver(BrowserType.fromString(browserName));
        driver = DriverFactory.getDriver();

        // 3. Open base URL
        String baseUrl = ConfigReader.get("base.url");
        driver.get(baseUrl);
        LoggerUtils.info("Browser launched: " + browserName + " | URL: " + baseUrl);

        // 4. Init HomePage — available to all test classes
        homePage = new HomePage(driver);

        // 5. Handle consent modal — safe even if not present
        homePage.handleConsentModal();
    }

    // ─── Teardown ────────────────────────────────────────────────

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        // Log result with status
        switch (result.getStatus()) {
            case ITestResult.SUCCESS ->
                    LoggerUtils.info("✅ PASSED: " + result.getName());
            case ITestResult.FAILURE ->
                    LoggerUtils.error("❌ FAILED: " + result.getName(), result.getThrowable());
            case ITestResult.SKIP ->
                    LoggerUtils.warn("⏭️ SKIPPED: " + result.getName());
        }

        // Quit driver
        DriverFactory.quitDriver();
        LoggerUtils.info("Browser closed after: " + result.getName());
    }
}