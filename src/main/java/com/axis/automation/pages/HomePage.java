package com.axis.automation.pages;

import com.axis.automation.components.HeaderComponent;
import com.axis.automation.core.BasePage;
import com.axis.automation.utils.LoggerUtils;
import com.axis.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for the Home Page.
 * Entry point of the application — provides access to HeaderComponent.
 */
public class HomePage extends BasePage {

    // ─── Locators ────────────────────────────────────────────────
    private final By slideshowContainer = By.cssSelector("div.slideshow-container");
    private final By logo               = By.cssSelector("a.logo");

    // ─── Components ──────────────────────────────────────────────
    public final HeaderComponent header;

    // ─── Constructor ─────────────────────────────────────────────
    public HomePage(WebDriver driver) {
        super(driver);
        this.header = new HeaderComponent(driver);
    }

    // ─── Actions ─────────────────────────────────────────────────

    /**
     * Returns the HeaderComponent for navigation actions.
     * Provides clean access: homePage.getHeader().clickRegister()
     */
    public HeaderComponent getHeader() {
        return header;
    }

    /**
     * Opens the home page and waits for it to load.
     * Returns this for method chaining.
     */
    public HomePage open(String baseUrl) {
        driver.get(baseUrl);
        WaitUtils.waitForVisibility(driver, slideshowContainer);
        LoggerUtils.info("Home page opened: " + baseUrl);
        return this;
    }

    /**
     * Verifies home page is fully loaded.
     */
    public boolean isPageLoaded() {
        return isDisplayed(slideshowContainer);
    }

    /**
     * Verifies logo is visible.
     */
    public boolean isLogoDisplayed() {
        return isDisplayed(logo);
    }
}