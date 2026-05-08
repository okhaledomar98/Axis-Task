package com.axis.automation.pages;

import com.axis.automation.core.BasePage;
import com.axis.automation.data.User;
import com.axis.automation.utils.LoggerUtils;
import com.axis.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for the Login Page.
 * Handles interactions with the registered users login form.
 */
public class LoginPage extends BasePage {

    // ─── Locators ────────────────────────────────────────────────
    private final By emailInput       = By.id("email");
    private final By passwordInput    = By.id("pass");
    private final By loginButton      = By.id("send2");

    // ─── Error Messages ──────────────────────────────────────────
    private final By loginErrorMsg    = By.cssSelector(".error-msg span");
    private final By emailError       = By.cssSelector("#advice-required-entry-email, #advice-validate-email-email");
    private final By passwordError    = By.cssSelector("#advice-required-entry-pass");

    // ─── Constructor ─────────────────────────────────────────────
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // ─── Actions — Method Chaining ───────────────────────────────

    public LoginPage enterEmail(String email) {
        type(emailInput, email);
        LoggerUtils.info("Entered email: " + email);
        return this;
    }

    public LoginPage enterPassword(String password) {
        type(passwordInput, password);
        LoggerUtils.info("Entered password");
        return this;
    }

    public void clickLogin() {
        click(loginButton);
        LoggerUtils.info("Clicked Login button");
    }

    // ─── Success Flow — Returns Next Page ────────────────────────

    /**
     * Fills login form, submits, and returns AccountDashboardPage.
     * Use this for SUCCESSFUL login scenarios only.
     */
    public AccountDashboardPage login(User user) {
        enterEmail(user.getEmail());
        enterPassword(user.getPassword());
        clickLogin();
        LoggerUtils.info("Login submitted — transitioning to Account Dashboard");
        return new AccountDashboardPage(driver);
    }

    // ─── Error Getters ───────────────────────────────────────────

    /**
     * Returns general login error message (wrong credentials).
     */
    public String getLoginErrorMessage() {
        WaitUtils.waitForVisibility(driver, loginErrorMsg);
        return getText(loginErrorMsg);
    }

    public String getEmailError() {
        WaitUtils.waitForVisibility(driver, emailError);
        return getText(emailError);
    }

    public String getPasswordError() {
        WaitUtils.waitForVisibility(driver, passwordError);
        return getText(passwordError);
    }

    // ─── Page Verification ───────────────────────────────────────

    public boolean isPageLoaded() {
        return isDisplayed(emailInput) && isDisplayed(passwordInput);
    }
}