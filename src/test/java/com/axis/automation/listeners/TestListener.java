package com.axis.automation.listeners;

import com.axis.automation.utils.LoggerUtils;
import com.axis.automation.utils.ScreenshotUtils;
import io.qameta.allure.Allure;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener that handles test lifecycle events.
 * Captures screenshots on failure and logs test results.
 */
public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        LoggerUtils.info("▶️ TEST STARTED: " + result.getName());
        Allure.description("Test: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LoggerUtils.info("✅ TEST PASSED: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LoggerUtils.error("❌ TEST FAILED: " + result.getName(), result.getThrowable());
        ScreenshotUtils.captureAndAttach("FAILED - " + result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LoggerUtils.warn("⏭️ TEST SKIPPED: " + result.getName());
    }
}