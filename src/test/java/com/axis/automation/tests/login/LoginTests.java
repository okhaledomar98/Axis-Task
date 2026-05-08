package com.axis.automation.tests.login;

import com.axis.automation.core.BaseTest;
import com.axis.automation.data.User;
import com.axis.automation.data.TestDataReader;
import com.axis.automation.listeners.RetryAnalyzer;
import com.axis.automation.listeners.TestListener;
import com.axis.automation.pages.AccountDashboardPage;
import com.axis.automation.pages.LoginPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(TestListener.class)
@Epic("User Account Management")
@Feature("Login")
public class LoginTests extends BaseTest {

    private LoginPage loginPage;

    // ============================================================
    // @BeforeMethod — Navigate to Login Page before each test
    // ============================================================
    @BeforeMethod(dependsOnMethods = "setUp")
    public void navigateToLoginPage() {
        loginPage = homePage.getHeader().clickLogin();
        Assert.assertTrue(loginPage.isPageLoaded(),
                "Login page should be loaded before test starts");
    }

    // ============================================================
    // TC01 — Valid Login
    // ============================================================
    @Test(
            priority = 1,
            description = "Successful login with valid credentials",
            retryAnalyzer = RetryAnalyzer.class
    )
    @Story("Successful Login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a registered user can log in successfully and lands on My Dashboard")
    public void tc01_validLoginWithValidCredentials() {

        // -- Arrange --
        User existingUser = TestDataReader.getExistingUser();

        // -- Act --
        AccountDashboardPage dashboardPage = loginPage.login(existingUser);

        // -- Assert --
        Assert.assertTrue(dashboardPage.isPageLoaded(),
                "My Dashboard should be loaded after successful login");

        String helloMsg = dashboardPage.getWelcomeMessage();
        Assert.assertTrue(helloMsg.toLowerCase().contains("hello"),
                "Hello message should greet the user. Actual: " + helloMsg);
    }

    // ============================================================
    // TC02 — Wrong Password
    // ============================================================
    @Test(
            priority = 2,
            description = "Login fails with wrong password and shows error message"
    )
    @Story("Login Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that an appropriate error message appears when login is attempted with wrong password")
    public void tc02_invalidLoginWithWrongPassword() {

        // -- Arrange --
        User invalidUser = TestDataReader.getInvalidLoginUser();

        // -- Act --
        loginPage.enterEmail(invalidUser.getEmail())
                 .enterPassword(invalidUser.getPassword())
                 .clickLogin();

        // -- Assert --
        Assert.assertTrue(loginPage.isPageLoaded(),
                "Should remain on login page after failed login attempt");

        String errorMsg = loginPage.getLoginErrorMessage();
        Assert.assertFalse(errorMsg.isEmpty(),
                "Error message should be displayed for wrong password");

        Assert.assertTrue(errorMsg.toLowerCase().contains("invalid"),
                "Error should say 'Invalid login or password'. Actual: " + errorMsg);
    }
}
