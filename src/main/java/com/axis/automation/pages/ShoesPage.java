package com.axis.automation.pages;

import com.axis.automation.core.BasePage;
import com.axis.automation.utils.LoggerUtils;
import com.axis.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for the Shoes listing page.
 * Handles sorting and product selection dynamically.
 */
public class ShoesPage extends BasePage {

    // ─── Locators ────────────────────────────────────────────────
    private final By pageTitle        = By.cssSelector("div.page-title h1");
    private final By sortByDropdown   = By.cssSelector("div.sort-by select");
    private final By productsGrid     = By.cssSelector("ul.products-grid");
    private final By firstProductName = By.cssSelector("ul.products-grid li.item:first-child h2.product-name a");
    private final By firstViewDetails = By.cssSelector("ul.products-grid li.item:first-child div.actions a.button");

    // ─── Constructor ─────────────────────────────────────────────
    public ShoesPage(WebDriver driver) {
        super(driver);
    }

    // ─── Actions ─────────────────────────────────────────────────

    /**
     * Sorts products by Price low to high.
     * Returns this for method chaining.
     */
    public ShoesPage sortByPriceLowToHigh() {
        WaitUtils.waitForVisibility(driver, sortByDropdown);
        selectByVisibleText(sortByDropdown, "Price");
        LoggerUtils.info("Sorted by Price low to high");
        WaitUtils.waitForVisibility(driver, productsGrid);
        return this;
    }

    /**
     * Gets name of first product after sorting.
     * Capture BEFORE clicking — use in assertions later.
     */
    public String getFirstProductName() {
        WaitUtils.waitForVisibility(driver, firstProductName);
        String name = getText(firstProductName);
        LoggerUtils.info("First product after sort: " + name);
        return name;
    }

    /**
     * Clicks View Details for first product after sorting.
     * Returns ProductDetailsPage.
     */
    public ProductDetailsPage clickFirstProduct() {
        WaitUtils.waitForClickability(driver, firstViewDetails);
        click(firstViewDetails);
        LoggerUtils.info("Clicked View Details for first product");
        return new ProductDetailsPage(driver);
    }

    // ─── Verification ────────────────────────────────────────────

    /**
     * Verifies shoes page is loaded.
     */
    public boolean isPageLoaded() {
        try {
            WaitUtils.waitForUrlContains(driver, "accessories/shoes");
            WaitUtils.waitForVisibility(driver, productsGrid);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns page title text.
     */
    public String getPageTitle() {
        WaitUtils.waitForVisibility(driver, pageTitle);
        return getText(pageTitle);
    }
}