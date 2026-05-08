package com.axis.automation.listeners;

import com.axis.automation.utils.LoggerUtils;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retry analyzer for handling flaky tests.
 * Retries failed tests up to MAX_RETRY_COUNT times.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final int MAX_RETRY_COUNT = 2;
    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            LoggerUtils.warn("Retrying test [" + result.getName() + "] - Attempt: " + retryCount);
            return true;
        }
        return false;
    }
}