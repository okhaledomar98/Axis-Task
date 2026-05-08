package com.axis.automation.components;

import com.axis.automation.core.BasePage;
import com.axis.automation.pages.LoginPage;
import com.axis.automation.pages.RegisterPage;
import com.axis.automation.pages.ShoesPage;
import com.axis.automation.utils.LoggerUtils;
import com.axis.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Header component present across all pages.
 * Handles navigation, account menu, login, register, and cart interactions.
 */
public class HeaderComponent extends BasePage {

    // ─── Locators ────────────────────────────────────────────────

    // Account toggle button (opens dropdown)
    private final By accountToggle    = By.cssSelector("a.skip-account");

    // Account dropdown links
    private final By loginLink        = By.cssSelector("#header-account a[href*='account/login']");
    private final By registerLink     = By.cssSelector("#header-account a[href*='account/create']");
    private final By myAccountLink    = By.cssSelector("#header-account a[href*='customer/account/']");

    // Cart
    private final By cartLink         = By.cssSelector("a.skip-cart");
    private final By cartCount        = By.cssSelector("a.skip-cart .count");

    // Navigation — Accessories
    private final By accessoriesMenu  = By.cssSelector("li.nav-3 > a");
    private final By shoesSubMenu     = By.cssSelector("a[href*='accessories/shoes']");

    // ─── Constructor ─────────────────────────────────────────────

    public HeaderComponent(WebDriver driver) {
        super(driver);
    }

    // ─── Account Actions ─────────────────────────────────────────

    /**
     * Opens account dropdown then clicks Log In.
     * Returns LoginPage for fluent navigation chaining.
     */
    public LoginPage clickLogin() {
        click(accountToggle);
        LoggerUtils.info("Opened account dropdown");
        WaitUtils.waitForClickability(driver, loginLink);
        click(loginLink);
        LoggerUtils.info("Clicked Log In from header");
        return new LoginPage(driver);
    }

    /**
     * Opens account dropdown then clicks Register.
     * Returns RegisterPage for fluent navigation chaining.
     */
    public RegisterPage clickRegister() {
        click(accountToggle);
        LoggerUtils.info("Opened account dropdown");
        WaitUtils.waitForClickability(driver, registerLink);
        click(registerLink);
        LoggerUtils.info("Clicked Register from header");
        return new RegisterPage(driver);
    }

    /**
     * Checks if user is logged in by verifying My Account link is visible.
     */
    public boolean isUserLoggedIn() {
        click(accountToggle);
        try {
            WaitUtils.waitForVisibility(driver, myAccountLink);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ─── Navigation Actions ──────────────────────────────────────

    /**
     * Hovers over Accessories menu — dropdown appears.
     * Use isAccessoriesDropdownVisible() to assert after this.
     */
    public HeaderComponent hoverOverAccessories() {
        WaitUtils.waitForVisibility(driver, accessoriesMenu);
        hover(accessoriesMenu);
        LoggerUtils.info("Hovered over Accessories menu");
        return this;
    }

    /**
     * Asserts Accessories dropdown is visible after hover.
     */
    public boolean isAccessoriesDropdownVisible() {
        return isDisplayed(shoesSubMenu);
    }

    /**
     * Clicks Shoes from Accessories dropdown.
     * Call after hoverOverAccessories().
     */
    public ShoesPage clickShoes() {
        WaitUtils.waitForClickability(driver, shoesSubMenu);
        click(shoesSubMenu);
        LoggerUtils.info("Clicked Shoes from Accessories submenu");
        return new ShoesPage(driver);
    }

    /**
     * Hovers over Accessories then clicks Shoes — one-step navigation.
     * Used by AccountDashboardPage.navigateToShoes().
     */
    public void navigateToShoes() {
        hoverOverAccessories();
        clickShoes();
    }

    // ─── Cart Actions ────────────────────────────────────────────

    /**
     * Returns cart item count from cart badge.
     */
    public String getCartCount() {
        return getText(cartCount);
    }

    /**
     * Clicks on cart icon.
     */
    public void clickCart() {
        click(cartLink);
        LoggerUtils.info("Clicked cart icon");
    }
}