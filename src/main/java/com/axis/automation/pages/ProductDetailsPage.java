package com.axis.automation.pages;

import com.axis.automation.core.BasePage;
import com.axis.automation.utils.LoggerUtils;
import com.axis.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for the Product Details Page.
 * Color and size are selected dynamically from the page.
 */
public class ProductDetailsPage extends BasePage {

    // ─── Locators ────────────────────────────────────────────────
    private final By productName     = By.cssSelector("div.product-name h1");
    private final By productPrice    = By.cssSelector("div.price-box span.price");
    private final By addToCartBtn    = By.cssSelector("button.btn-cart");
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
        WaitUtils.waitForVisibility(driver, availableColors);
        List<WebElement> colors = findElements(availableColors);
        String colorTitle = colors.get(0).getAttribute("title");
        colors.get(0).click();
        LoggerUtils.info("Selected first available color: " + colorTitle);
        return colorTitle;
    }

    /**
     * Selects first available size from swatch list.
     * Returns selected size title — used for assertion.
     */
    public String selectFirstAvailableSize() {
        WaitUtils.waitForVisibility(driver, availableSizes);
        List<WebElement> sizes = findElements(availableSizes);
        String sizeTitle = sizes.get(0).getAttribute("title");
        sizes.get(0).click();
        LoggerUtils.info("Selected first available size: " + sizeTitle);
        return sizeTitle;
    }

    /**
     * Clicks Add to Cart — site redirects to CartPage automatically.
     * Returns CartPage.
     */
    public CartPage addToCart() {
        WaitUtils.waitForClickability(driver, addToCartBtn);
        click(addToCartBtn);
        LoggerUtils.info("Clicked Add to Cart — redirecting to Cart page");
        return new CartPage(driver);
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
            WaitUtils.waitForVisibility(driver, productName);
            WaitUtils.waitForVisibility(driver, addToCartBtn);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}