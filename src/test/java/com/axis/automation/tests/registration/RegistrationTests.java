package com.axis.automation.tests.registration;

import com.axis.automation.core.BaseTest;
import com.axis.automation.data.User;
import com.axis.automation.data.TestDataReader;
import com.axis.automation.pages.RegisterPage;
import com.axis.automation.pages.AccountDashboardPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.axis.automation.listeners.RetryAnalyzer;
import com.axis.automation.listeners.TestListener;

@Listeners(TestListener.class)
@Epic("User Account Management")
@Feature("Registration")
public class RegistrationTests extends BaseTest {

    private RegisterPage registerPage;

    // ============================================================
    // @BeforeMethod — Navigate to Register Page before each test
    // WHY: BaseTest.setUp() opens base.url only. Navigation
    //      to register page is test-specific, not global concern.
    // ============================================================
    @BeforeMethod (dependsOnMethods = "setUp")
    public void navigateToRegisterPage() {
        registerPage = homePage.getHeader().clickRegister();
        Assert.assertTrue(registerPage.isPageLoaded(),
                "Register page should be loaded before test starts");
    }

    // ============================================================
    // TC01 — Valid Registration
    // ============================================================

    @Test(
            priority = 1,
            description = "Successful registration with valid user data",
            retryAnalyzer = RetryAnalyzer.class
    )
    @Story("Successful Registration")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a new user can register successfully with valid data and lands on Account Dashboard")
    public void tc01_validRegistrationWithValidCredentials() {

        // -- Arrange --
        User validUser = TestDataReader.getValidUser();

        // -- Act --
        AccountDashboardPage dashboardPage = registerPage.fillFormAndSubmit(validUser);

        // -- Assert --
        Assert.assertTrue(dashboardPage.isPageLoaded(),
                "Account dashboard should be loaded after successful registration");

        String welcomeMsg = dashboardPage.getWelcomeMessage();
        Assert.assertTrue(welcomeMsg.toLowerCase().contains("hello"),
                "Welcome message should greet the user. Actual: " + welcomeMsg);

        Assert.assertTrue(
                welcomeMsg.contains(validUser.getFirstName()),
                "Welcome message should contain user's first name. Actual: " + welcomeMsg
        );
    }

    // ============================================================
    // TC02 — Missing Required Fields
    // ============================================================

    @Test(
            priority = 2,
            description = "Registration fails when all required fields are empty"
    )
    @Story("Registration Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that all required field validation errors appear when form is submitted empty")
    public void tc03_missingRequiredFieldsShowsAllErrors() {

        // -- Arrange --
        User emptyUser = TestDataReader.getMissingFieldsUser();

        // -- Act --
        registerPage.fillForm(emptyUser).submit();

        // -- Assert --
        Assert.assertTrue(registerPage.isPageLoaded(),
                "Should remain on register page after empty form submission");

        Assert.assertFalse(registerPage.getFirstNameError().isEmpty(),
                "First name error should appear");

        Assert.assertFalse(registerPage.getLastNameError().isEmpty(),
                "Last name error should appear");

        Assert.assertFalse(registerPage.getEmailError().isEmpty(),
                "Email error should appear");

        Assert.assertFalse(registerPage.getPasswordError().isEmpty(),
                "Password error should appear");
    }
}