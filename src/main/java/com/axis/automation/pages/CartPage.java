package com.axis.automation.pages;

import com.axis.automation.core.BasePage;
import com.axis.automation.utils.LoggerUtils;
import com.axis.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for the Shopping Cart Page.
 * Verifies product was added successfully to cart.
 */
public class CartPage extends BasePage {

    // ─── Locators ────────────────────────────────────────────────
    private final By pageTitle        = By.cssSelector("div.page-title h1");
    private final By successMessage   = By.cssSelector("ul.messages li.success-msg span");
    private final By cartTable        = By.cssSelector("table#shopping-cart-table");
    private final By productName      = By.cssSelector("td.product-cart-info h2.product-name a");
    private final By productPrice     = By.cssSelector("td.product-cart-price span.price");
    private final By cartSubtotal     = By.cssSelector("td.product-cart-total span.price");
    private final By grandTotal       = By.cssSelector("table#shopping-cart-totals-table tfoot span.price");
    private final By checkoutButton   = By.cssSelector("button.btn-proceed-checkout");

    // ─── Constructor ─────────────────────────────────────────────
    public CartPage(WebDriver driver) {
        super(driver);
    }

    // ─── Verification ────────────────────────────────────────────

    /**
     * Verifies cart page loaded with success message.
     * Success message confirms product was added correctly.
     */
    public boolean isPageLoaded() {
        try {
            WaitUtils.waitForUrlContains(driver, "checkout/cart");
            WaitUtils.waitForVisibility(driver, successMessage);
            LoggerUtils.info("Cart page loaded with success message");
            return true;
        } catch (Exception e) {
            LoggerUtils.error("Cart page failed to load", e);
            return false;
        }
    }

    /**
     * Returns success message text.
     * e.g. "Ann Ankle Boot was added to your shopping cart."
     */
    public String getSuccessMessage() {
        WaitUtils.waitForVisibility(driver, successMessage);
        return getText(successMessage);
    }

    /**
     * Returns product name shown in cart table.
     */
    public String getProductName() {
        WaitUtils.waitForVisibility(driver, productName);
        return getText(productName);
    }

    /**
     * Returns product price shown in cart table.
     */
    public String getProductPrice() {
        WaitUtils.waitForVisibility(driver, productPrice);
        return getText(productPrice);
    }

    /**
     * Returns grand total shown in cart totals.
     */
    public String getGrandTotal() {
        WaitUtils.waitForVisibility(driver, grandTotal);
        return getText(grandTotal);
    }

    /**
     * Returns page title — should be "Shopping Cart".
     */
    public String getPageTitle() {
        WaitUtils.waitForVisibility(driver, pageTitle);
        return getText(pageTitle);
    }
}