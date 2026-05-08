package com.axis.automation.utils;

import com.axis.automation.core.DriverFactory;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;

/**
 * Utility class for capturing and attaching screenshots to Allure reports.
 */
public class ScreenshotUtils {

    private ScreenshotUtils() {}

    /**
     * Captures screenshot and attaches it directly to Allure report.
     *
     * @param screenshotName name shown in Allure report
     */
    public static void captureAndAttach(String screenshotName) {
        try {
            WebDriver driver = DriverFactory.getDriver();
            byte[] screenshot = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BYTES);

            Allure.addAttachment(
                    screenshotName,
                    "image/png",
                    new ByteArrayInputStream(screenshot),
                    "png"
            );

            LoggerUtils.info("Screenshot captured: " + screenshotName);

        } catch (Exception e) {
            LoggerUtils.error("Failed to capture screenshot: " + screenshotName, e);
        }
    }
}