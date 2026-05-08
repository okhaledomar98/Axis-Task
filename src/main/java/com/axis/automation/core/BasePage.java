package com.axis.automation.core;

import com.axis.automation.utils.LoggerUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Abstract base class for all Page Objects.
 * Provides reusable Selenium interactions and consent modal handling.
 */
public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    // ─── Consent Modal Locators ──────────────────────────────────
    private final By consentModal     = By.cssSelector("div.privacy_prompt");
    private final By consentOptIn     = By.cssSelector("input#privacy_pref_optin");
    private final By consentSubmitBtn = By.cssSelector("div#consent_prompt_submit");

    // ─── Constructor ─────────────────────────────────────────────
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ─── Consent Modal Handler ───────────────────────────────────

    /**
     * Handles the consent/cookie modal if it appears.
     * Safe to call even if modal is not present — will not throw exception.
     */
    public void handleConsentModal() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(consentModal));

            click(consentOptIn);
            LoggerUtils.info("Consent modal detected — selected Opt-In");

            click(consentSubmitBtn);
            LoggerUtils.info("Consent modal dismissed successfully");

            shortWait.until(ExpectedConditions.invisibilityOfElementLocated(consentModal));

        } catch (TimeoutException e) {
            LoggerUtils.info("Consent modal not present — continuing normally");
        }
    }

    // ─── Core Interactions ───────────────────────────────────────

    /**
     * Clicks on an element located by the given By locator.
     */
    @Step("Click on element: {locator}")
    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        LoggerUtils.info("Clicked: " + locator);
    }

    /**
     * Types text into an input field after clearing it.
     */
    @Step("Type '{text}' into element: {locator}")
    protected void type(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
        LoggerUtils.info("Typed '" + text + "' into: " + locator);
    }

    /**
     * Gets visible text of an element.
     */
    protected String getText(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }

    /**
     * Gets value attribute of an input element.
     */
    protected String getValue(By locator) {
        return getAttribute(locator, "value");
    }

    /**
     * Gets any attribute value of an element.
     */
    protected String getAttribute(By locator, String attribute) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator))
                .getAttribute(attribute);
    }

    /**
     * Checks if element is displayed on the page.
     */
    protected boolean isDisplayed(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Checks if element is enabled.
     */
    protected boolean isEnabled(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isEnabled();
    }

    /**
     * Checks if checkbox or radio button is selected.
     */
    protected boolean isSelected(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isSelected();
    }

    /**
     * Hovers over an element using Actions class.
     */
    @Step("Hover over element: {locator}")
    protected void hover(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        new Actions(driver).moveToElement(element).perform();
        LoggerUtils.info("Hovered over: " + locator);
    }

    /**
     * Clicks element using JavaScript — fallback for unclickable elements.
     */
    @Step("JS Click on element: {locator}")
    protected void clickWithJs(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        LoggerUtils.info("JS Clicked: " + locator);
    }

    /**
     * Clicks a WebElement using JavaScript — overload for already-resolved elements.
     */
    protected void clickWithJs(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        LoggerUtils.info("JS Clicked element: " + element);
    }

    /**
     * Fires a real MouseEvent via dispatchEvent — needed for Prototype.js / Magento swatches.
     * arguments[0].click() does NOT trigger Prototype.js event observers.
     * new MouseEvent('click', {bubbles:true}) DOES.
     */
    protected void fireMouseClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
            "var evt = new MouseEvent('click', {bubbles: true, cancelable: true, view: window});" +
            "arguments[0].dispatchEvent(evt);",
            element
        );
        LoggerUtils.info("Fired MouseEvent click on: " + element);
    }

    /**
     * Scrolls element into view using JavaScript.
     */
    protected void scrollToElement(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        LoggerUtils.info("Scrolled to: " + locator);
    }

    /**
     * Scrolls a WebElement into view — overload for already-resolved elements.
     */
    protected void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
        LoggerUtils.info("Scrolled to element: " + element);
    }

    /**
     * Selects dropdown option by visible text.
     */
    @Step("Select '{text}' from dropdown: {locator}")
    protected void selectByVisibleText(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        new Select(element).selectByVisibleText(text);
        LoggerUtils.info("Selected '" + text + "' from: " + locator);
    }

    /**
     * Selects dropdown option by value attribute.
     */
    protected void selectByValue(By locator, String value) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        new Select(element).selectByValue(value);
        LoggerUtils.info("Selected value '" + value + "' from: " + locator);
    }

    /**
     * Finds all elements matching the locator.
     */
    protected List<WebElement> findElements(By locator) {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    /**
     * Returns count of elements matching the locator.
     */
    protected int getElementsCount(By locator) {
        return findElements(locator).size();
    }

    /**
     * Returns current page title.
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Returns current page URL.
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}