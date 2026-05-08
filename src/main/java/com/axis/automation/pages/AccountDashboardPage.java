package com.axis.automation.pages;

import com.axis.automation.components.HeaderComponent;
import com.axis.automation.core.BasePage;
import com.axis.automation.utils.LoggerUtils;
import com.axis.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for the Account Dashboard Page.
 * Appears after successful login or registration.
 * Primary role: verify login success + navigate to shoes via header hover.
 */
public class AccountDashboardPage extends BasePage {

    // ─── Locators ────────────────────────────────────────────────
    private final By dashboardTitle  = By.cssSelector("div.page-title h1");
    private final By helloMessage    = By.cssSelector("div.welcome-msg p.hello");

    // ─── Components ──────────────────────────────────────────────
    public final HeaderComponent header;

    // ─── Constructor ─────────────────────────────────────────────
    public AccountDashboardPage(WebDriver driver) {
        super(driver);
        this.header = new HeaderComponent(driver);
    }

    // ─── Verification ────────────────────────────────────────────

    /**
     * Verifies dashboard page is loaded after login.
     */
    public boolean isPageLoaded() {
        try {
            WaitUtils.waitForUrlContains(driver, "customer/account");
            WaitUtils.waitForVisibility(driver, dashboardTitle);
            LoggerUtils.info("Account dashboard loaded successfully");
            return true;
        } catch (Exception e) {
            LoggerUtils.error("Account dashboard failed to load", e);
            return false;
        }
    }

    /**
     * Returns hello message — used for assertion in tests.
     * Example: "Hello, Omar Khaled!"
     */
    public String getWelcomeMessage() {
        WaitUtils.waitForVisibility(driver, helloMessage);
        return getText(helloMessage);
    }

    // ─── Navigation Actions ──────────────────────────────────────

    /**
     * Hovers over Accessories then clicks Shoes — returns ShoesPage.
     * Primary navigation action of this page in the E2E flow.
     */
    public ShoesPage navigateToShoes() {
        header.navigateToShoes();
        LoggerUtils.info("Navigated to Shoes page");
        return new ShoesPage(driver);
    }
}