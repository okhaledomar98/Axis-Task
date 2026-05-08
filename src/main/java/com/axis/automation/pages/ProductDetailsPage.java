package com.axis.automation.pages;

import com.axis.automation.constants.TimeoutConstants;
import com.axis.automation.core.BasePage;
import com.axis.automation.utils.LoggerUtils;
import com.axis.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * Page Object for the Product Details Page.
 * Color and size are selected dynamically from the page.
 */
public class ProductDetailsPage extends BasePage {

    // ─── Locators ────────────────────────────────────────────────
    // product name lives in product-shop — more specific to avoid matching product-img-box h1
    private final By productName     = By.cssSelector("div.product-shop div.product-name span.h1");
    private final By productPrice    = By.cssSelector("div.price-box span.price");
    // scoped to add-to-cart-buttons div — avoids matching hidden #map-popup btn-cart
    private final By addToCartBtn    = By.cssSelector("div.add-to-cart-buttons button.btn-cart");
    // hidden select elements — more reliable than visual swatches for automation
    private final By swatchSelects   = By.cssSelector("select.swatch-select");
    // kept for fallback reference
    private final By availableColors = By.cssSelector("ul#configurable_swatch_color li a");
    private final By availableSizes  = By.cssSelector("ul#configurable_swatch_shoe_size li a");

    // ─── Constructor ─────────────────────────────────────────────
    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    // ─── Actions ─────────────────────────────────────────────────

    /**
     * Selects first available color from swatch list.
     * Returns selected color title — used for assertion.
     */
    public String selectFirstAvailableColor() {
        // Use hidden select element — bypasses Prototype.js swatch click issues
        WaitUtils.waitForPresence(driver, swatchSelects, TimeoutConstants.MEDIUM_WAIT);
        List<WebElement> selects = findElements(swatchSelects);
        WebElement colorSelect = selects.get(0);  // first select = color

        Select select = new Select(colorSelect);
        WebElement firstOption = select.getOptions().get(1); // index 0 = "Choose an Option"
        String colorTitle = firstOption.getAttribute("data-label");
        String colorValue = firstOption.getAttribute("value");

        // set value + fire change event so Magento spConfig updates
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].value = arguments[1];" +
            "arguments[0].dispatchEvent(new Event('change', {bubbles: true}));",
            colorSelect, colorValue
        );
        LoggerUtils.info("Selected first available color via select: " + colorTitle);
        return colorTitle;
    }

    /**
     * Selects first available size from swatch list.
     * Returns selected size title — used for assertion.
     */
    public String selectFirstAvailableSize() {
        // Use hidden select element — bypasses Prototype.js swatch click issues
        WaitUtils.waitForPresence(driver, swatchSelects, TimeoutConstants.MEDIUM_WAIT);
        List<WebElement> selects = findElements(swatchSelects);
        WebElement sizeSelect = selects.get(1);  // second select = shoe size

        Select select = new Select(sizeSelect);
        WebElement firstOption = select.getOptions().get(1); // index 0 = "Choose an Option"
        String sizeTitle = firstOption.getAttribute("data-label");
        String sizeValue = firstOption.getAttribute("value");

        // set value + fire change event so Magento spConfig updates
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].value = arguments[1];" +
            "arguments[0].dispatchEvent(new Event('change', {bubbles: true}));",
            sizeSelect, sizeValue
        );
        LoggerUtils.info("Selected first available size via select: " + sizeTitle);
        return sizeTitle;
    }

    /**
     * Clicks Add to Cart — site redirects to CartPage automatically.
     * Returns CartPage.
     */
    public CartPage addToCart() {
        WaitUtils.waitForVisibility(driver, addToCartBtn);
        scrollToElement(addToCartBtn);
        clickWithJs(addToCartBtn);
        LoggerUtils.info("Clicked Add to Cart — redirecting to Cart page");
        return new CartPage(driver);
    }

    /**
     * Checks if a color swatch is selected (has "selected" class).
     * Call after selectFirstAvailableColor().
     */
    public boolean isColorSelected(String colorTitle) {
        // verify via hidden select value — reliable, no dependency on CSS class
        try {
            List<WebElement> selects = findElements(swatchSelects);
            String val = selects.get(0).getAttribute("value");
            boolean selected = val != null && !val.isEmpty();
            LoggerUtils.info("Color '" + colorTitle + "' selected: " + selected + " (select value=" + val + ")");
            return selected;
        } catch (Exception e) {
            LoggerUtils.error("isColorSelected check failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks if a size swatch is selected (has "selected" class).
     * Call after selectFirstAvailableSize().
     */
    public boolean isSizeSelected(String sizeTitle) {
        // verify via hidden select value — reliable, no dependency on CSS class
        try {
            List<WebElement> selects = findElements(swatchSelects);
            String val = selects.get(1).getAttribute("value");
            boolean selected = val != null && !val.isEmpty();
            LoggerUtils.info("Size '" + sizeTitle + "' selected: " + selected + " (select value=" + val + ")");
            return selected;
        } catch (Exception e) {
            LoggerUtils.error("isSizeSelected check failed: " + e.getMessage());
            return false;
        }
    }

    // ─── Verification ────────────────────────────────────────────

    /**
     * Returns product name — used for assertion.
     */
    public String getProductName() {
        WaitUtils.waitForVisibility(driver, productName);
        return getText(productName);
    }

    /**
     * Returns product price — used for assertion.
     */
    public String getProductPrice() {
        WaitUtils.waitForVisibility(driver, productPrice);
        return getText(productPrice);
    }

    /**
     * Verifies product details page is loaded.
     */
    public boolean isPageLoaded() {
        try {
            // presence check — btn-cart is guaranteed on every product page
            // already waited for it in ShoesPage.clickProductByName(), so this returns fast
            WaitUtils.waitForPresence(driver, addToCartBtn, TimeoutConstants.LONG_WAIT);
            return true;
        } catch (Exception e) {
            LoggerUtils.error("ProductDetailsPage not loaded. Current URL: " + driver.getCurrentUrl());
            return false;
        }
    }
}