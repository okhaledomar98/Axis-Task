package com.axis.automation.tests.cart;

import com.axis.automation.core.BaseTest;
import com.axis.automation.data.TestDataReader;
import com.axis.automation.data.User;
import com.axis.automation.listeners.RetryAnalyzer;
import com.axis.automation.listeners.TestListener;
import com.axis.automation.pages.AccountDashboardPage;
import com.axis.automation.pages.CartPage;
import com.axis.automation.pages.ProductDetailsPage;
import com.axis.automation.pages.ShoesPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;

@Listeners(TestListener.class)
@Epic("Shopping")
@Feature("Add to Cart")
public class AddToCartTests extends BaseTest {

    @Test(
            priority = 1,
            description = "E2E flow: Login → Accessories → Shoes → Sort → Dorian → Color/Size → Cart",
            retryAnalyzer = RetryAnalyzer.class
    )
    @Story("Add Dorian Shoes to Cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Complete E2E flow from login to adding Dorian shoes to the shopping cart")
    public void tc01_addDorianShoesToCart() {

        // ─── Step 1: Login ───────────────────────────────────────
        User existingUser = TestDataReader.getExistingUser();

        AccountDashboardPage dashboardPage = homePage.getHeader()
                .clickLogin()
                .login(existingUser);

        Assert.assertTrue(dashboardPage.isPageLoaded(),
                "Step 1 FAILED — User should be redirected to Account Dashboard after login");

        // ─── Step 2: Hover Accessories → Assert dropdown visible ─
        homePage.getHeader().hoverOverAccessories();

        Assert.assertTrue(homePage.getHeader().isAccessoriesDropdownVisible(),
                "Step 2 FAILED — Accessories dropdown should be visible after hover");

        // ─── Step 3: Click Shoes → Assert Shoes page loaded ──────
        ShoesPage shoesPage = homePage.getHeader().clickShoes();

        Assert.assertTrue(shoesPage.isPageLoaded(),
                "Step 3 FAILED — Should be redirected to Shoes subcategory page");

        Assert.assertTrue(shoesPage.getPageTitle().toLowerCase().contains("shoes"),
                "Step 3 FAILED — Page title should contain 'Shoes'. Actual: " + shoesPage.getPageTitle());

        // ─── Step 4: Sort by Price Low to High → Assert sorted ───
        shoesPage.sortByPriceLowToHigh();

        List<Double> prices = shoesPage.getProductPrices();
        for (int i = 0; i < prices.size() - 1; i++) {
            Assert.assertTrue(prices.get(i) <= prices.get(i + 1),
                    "Step 4 FAILED — Prices not sorted ascending at index " + i
                            + ". [" + prices.get(i) + "] > [" + prices.get(i + 1) + "]");
        }

        // ─── Step 5: Click Dorian Shoes → Assert product page ────
        ProductDetailsPage productPage = shoesPage.clickProductByName("Dorian");

        Assert.assertTrue(productPage.isPageLoaded(),
                "Step 5 FAILED — Should be redirected to product details page");

        Assert.assertTrue(productPage.getProductName().toLowerCase().contains("dorian"),
                "Step 5 FAILED — Product name should contain 'Dorian'. Actual: " + productPage.getProductName());

        // ─── Step 6: Select Color → Assert highlighted ────────────
        String selectedColor = productPage.selectFirstAvailableColor();

        Assert.assertTrue(productPage.isColorSelected(selectedColor),
                "Step 6 FAILED — Selected color '" + selectedColor + "' should be highlighted");

        // ─── Step 6: Select Size → Assert highlighted ─────────────
        String selectedSize = productPage.selectFirstAvailableSize();

        Assert.assertTrue(productPage.isSizeSelected(selectedSize),
                "Step 6 FAILED — Selected size '" + selectedSize + "' should be highlighted");

        // ─── Step 7: Add to Cart → Assert confirmation ────────────
        CartPage cartPage = productPage.addToCart();

        Assert.assertTrue(cartPage.isPageLoaded(),
                "Step 7 FAILED — Should be redirected to Shopping Cart with success message");

        String successMsg = cartPage.getSuccessMessage();
        Assert.assertFalse(successMsg.isEmpty(),
                "Step 7 FAILED — Success message should be displayed in cart");

        Assert.assertTrue(successMsg.toLowerCase().contains("added") || successMsg.toLowerCase().contains("cart"),
                "Step 7 FAILED — Success message should confirm product added. Actual: " + successMsg);
    }
}
