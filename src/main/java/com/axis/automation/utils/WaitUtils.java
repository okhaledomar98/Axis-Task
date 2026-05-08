package com.axis.automation.utils;

import com.axis.automation.constants.TimeoutConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Utility class providing reusable wait methods for the framework.
 *
 * NEVER use Thread.sleep() in tests - always use these explicit waits.
 * Explicit waits are smarter: they wait only as long as needed,
 * unlike Thread.sleep() which always waits the full duration.
 */
public final class WaitUtils {

    private WaitUtils() {
        throw new UnsupportedOperationException("WaitUtils is a utility class");
    }

    /**
     * Creates a WebDriverWait instance with the specified timeout.
     *
     * @param driver the WebDriver instance
     * @param timeoutInSeconds maximum time to wait
     * @return a configured WebDriverWait instance
     */
    private static WebDriverWait createWait(WebDriver driver, int timeoutInSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }

    // ==================== VISIBILITY WAITS ====================

    /**
     * Waits for an element to be visible on the page.
     *
     * @param driver the WebDriver instance
     * @param locator the element locator
     * @return the visible WebElement
     */
    public static WebElement waitForVisibility(WebDriver driver, By locator) {
        return waitForVisibility(driver, locator, TimeoutConstants.MEDIUM_WAIT);
    }

    /**
     * Waits for an element to be visible with custom timeout.
     */
    public static WebElement waitForVisibility(WebDriver driver, By locator, int timeoutInSeconds) {
        return createWait(driver, timeoutInSeconds)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // ==================== CLICKABILITY WAITS ====================

    /**
     * Waits for an element to be clickable (visible AND enabled).
     */
    public static WebElement waitForClickability(WebDriver driver, By locator) {
        return waitForClickability(driver, locator, TimeoutConstants.MEDIUM_WAIT);
    }

    public static WebElement waitForClickability(WebDriver driver, By locator, int timeoutInSeconds) {
        return createWait(driver, timeoutInSeconds)
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    // ==================== PRESENCE WAITS ====================

    /**
     * Waits for an element to be present in the DOM (may not be visible).
     * Useful for hidden elements or elements loaded via JavaScript.
     */
    public static WebElement waitForPresence(WebDriver driver, By locator) {
        return waitForPresence(driver, locator, TimeoutConstants.MEDIUM_WAIT);
    }

    public static WebElement waitForPresence(WebDriver driver, By locator, int timeoutInSeconds) {
        return createWait(driver, timeoutInSeconds)
                .until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    // ==================== INVISIBILITY WAITS ====================

    /**
     * Waits for an element to disappear (e.g., loading spinners).
     */
    public static boolean waitForInvisibility(WebDriver driver, By locator) {
        return waitForInvisibility(driver, locator, TimeoutConstants.MEDIUM_WAIT);
    }

    public static boolean waitForInvisibility(WebDriver driver, By locator, int timeoutInSeconds) {
        return createWait(driver, timeoutInSeconds)
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    // ==================== TEXT WAITS ====================

    /**
     * Waits for an element to contain specific text.
     */
    public static boolean waitForTextToBePresent(WebDriver driver, By locator, String text) {
        return createWait(driver, TimeoutConstants.MEDIUM_WAIT)
                .until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    // ==================== URL WAITS ====================

    /**
     * Waits for the URL to contain the specified text.
     */
    public static boolean waitForUrlContains(WebDriver driver, String urlFragment) {
        return createWait(driver, TimeoutConstants.MEDIUM_WAIT)
                .until(ExpectedConditions.urlContains(urlFragment));
    }

    // ==================== LIST WAITS ====================

    /**
     * Waits for all elements matching the locator to be visible.
     */
    public static List<WebElement> waitForAllElementsVisible(WebDriver driver, By locator) {
        return createWait(driver, TimeoutConstants.MEDIUM_WAIT)
                .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }
}