package com.axis.automation.enums;


public enum BrowserType {

    CHROME,
    FIREFOX,
    EDGE;


    public static BrowserType fromString(String browserName) {
        if (browserName == null || browserName.isBlank()) {
            throw new IllegalArgumentException("Browser name cannot be null or empty");
        }

        try {
            return BrowserType.valueOf(browserName.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Unsupported browser: '" + browserName + "'. " +
                            "Supported browsers: CHROME, FIREFOX, EDGE"
            );
        }
    }
}