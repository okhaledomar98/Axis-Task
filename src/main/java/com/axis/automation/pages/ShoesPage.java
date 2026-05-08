package com.axis.automation.pages;

import com.axis.automation.constants.TimeoutConstants;
import com.axis.automation.core.BasePage;
import com.axis.automation.utils.LoggerUtils;
import com.axis.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

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
    private final By allProductPrices = By.cssSelector("ul.products-grid li.item span.price");
    private final By allProductNames  = By.cssSelector("ul.products-grid li.item h2.product-name a");
    private final By allViewDetails   = By.cssSelector("ul.products-grid li.item div.actions a.button");

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
        WaitUtils.waitForVisibility(driver, firstViewDetails);
        scrollToElement(firstViewDetails);
        clickWithJs(firstViewDetails);
        LoggerUtils.info("Clicked View Details for first product");
        return new ProductDetailsPage(driver);
    }

    /**
     * Navigates directly to the product page whose name contains the given keyword.
     * Gets href from the product name link — 100% reliable, no index matching needed.
     * Returns ProductDetailsPage.
     */
    public ProductDetailsPage clickProductByName(String nameKeyword) {
        WaitUtils.waitForVisibility(driver, allProductNames);
        List<WebElement> names = findElements(allProductNames);

        for (WebElement nameEl : names) {
            if (nameEl.getText().toLowerCase().contains(nameKeyword.toLowerCase())) {
                String productUrl = nameEl.getAttribute("href");
                LoggerUtils.info("Found product: " + nameEl.getText() + " — navigating to: " + productUrl);
                driver.get(productUrl);
                WaitUtils.waitForPresence(driver, By.cssSelector("div.add-to-cart-buttons button.btn-cart"), TimeoutConstants.LONG_WAIT);
                LoggerUtils.info("Product details page loaded — btn-cart confirmed");
                return new ProductDetailsPage(driver);
            }
        }
        throw new RuntimeException("Product containing '" + nameKeyword + "' not found on page");
    }

    /**
     * Returns list of product prices as doubles for sort assertion.
     * Strips "$" and parses to double.
     */
    public List<Double> getProductPrices() {
        WaitUtils.waitForVisibility(driver, allProductPrices);
        List<WebElement> priceElements = findElements(allProductPrices);
        List<Double> prices = priceElements.stream()
                .map(el -> el.getText().replace("$", "").replace(",", "").trim())
                .map(Double::parseDouble)
                .collect(Collectors.toList());
        LoggerUtils.info("Product prices found: " + prices);
        return prices;
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