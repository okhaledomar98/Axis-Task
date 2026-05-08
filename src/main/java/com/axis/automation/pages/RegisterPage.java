package com.axis.automation.pages;

import com.axis.automation.core.BasePage;
import com.axis.automation.data.User;
import com.axis.automation.utils.LoggerUtils;
import com.axis.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for the Registration Page.
 * Handles all interactions with the create account form.
 */
public class RegisterPage extends BasePage {

    // ─── Locators ────────────────────────────────────────────────
    private final By firstNameInput   = By.id("firstname");
    private final By lastNameInput    = By.id("lastname");
    private final By emailInput       = By.id("email_address");
    private final By passwordInput    = By.id("password");
    private final By confirmPassInput = By.id("confirmation");
    private final By submitButton     = By.cssSelector("button[title='Register']");

    // ─── Error Messages ──────────────────────────────────────────
    private final By firstNameError   = By.cssSelector("#advice-required-entry-firstname");
    private final By lastNameError    = By.cssSelector("#advice-required-entry-lastname");
    private final By emailError       = By.cssSelector("#advice-validate-email-email_address, #advice-required-entry-email_address");
    private final By passwordError    = By.cssSelector("#advice-required-entry-password, #advice-validate-password-password");
    private final By confirmPassError = By.cssSelector("#advice-validate-cpassword-confirmation");

    // ─── Constructor ─────────────────────────────────────────────
    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    // ─── Actions — Method Chaining ───────────────────────────────

    public RegisterPage enterFirstName(String firstName) {
        type(firstNameInput, firstName);
        LoggerUtils.info("Entered first name: " + firstName);
        return this;
    }

    public RegisterPage enterLastName(String lastName) {
        type(lastNameInput, lastName);
        LoggerUtils.info("Entered last name: " + lastName);
        return this;
    }

    public RegisterPage enterEmail(String email) {
        type(emailInput, email);
        LoggerUtils.info("Entered email: " + email);
        return this;
    }

    public RegisterPage enterPassword(String password) {
        type(passwordInput, password);
        LoggerUtils.info("Entered password");
        return this;
    }

    public RegisterPage enterConfirmPassword(String confirmPassword) {
        type(confirmPassInput, confirmPassword);
        LoggerUtils.info("Entered confirm password");
        return this;
    }

    // ─── Success Flow — Returns Next Page ────────────────────────

    /**
     * Fills entire form, submits, and returns AccountDashboardPage.
     * Use this for SUCCESSFUL registration scenarios only.
     */
    public AccountDashboardPage fillFormAndSubmit(User user) {
        enterFirstName(user.getFirstName());
        enterLastName(user.getLastName());
        enterEmail(user.getEmail());
        enterPassword(user.getPassword());
        enterConfirmPassword(user.getConfirmPassword());
        scrollToElement(submitButton);
        clickWithJs(submitButton);
        LoggerUtils.info("Registration submitted — transitioning to Account Dashboard");
        return new AccountDashboardPage(driver);
    }

    /**
     * Fills form only — without submitting.
     * Use this for INVALID scenarios where you need to check errors.
     */
    public RegisterPage fillForm(User user) {
        enterFirstName(user.getFirstName());
        enterLastName(user.getLastName());
        enterEmail(user.getEmail());
        enterPassword(user.getPassword());
        enterConfirmPassword(user.getConfirmPassword());
        return this;
    }

    /**
     * Submits the form — use after fillForm() in invalid scenarios.
     */
    public void submit() {
        scrollToElement(submitButton);
        clickWithJs(submitButton);
        LoggerUtils.info("Clicked Register submit button");
    }

    // ─── Error Getters ───────────────────────────────────────────

    public String getFirstNameError() {
        WaitUtils.waitForVisibility(driver, firstNameError);
        return getText(firstNameError);
    }

    public String getLastNameError() {
        WaitUtils.waitForVisibility(driver, lastNameError);
        return getText(lastNameError);
    }

    public String getEmailError() {
        WaitUtils.waitForVisibility(driver, emailError);
        return getText(emailError);
    }

    public String getPasswordError() {
        WaitUtils.waitForVisibility(driver, passwordError);
        return getText(passwordError);
    }

    public String getConfirmPasswordError() {
        WaitUtils.waitForVisibility(driver, confirmPassError);
        return getText(confirmPassError);
    }

    // ─── Page Verification ───────────────────────────────────────

    public boolean isPageLoaded() {
        return isDisplayed(firstNameInput);
    }
}