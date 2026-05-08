package com.axis.automation.constants;

/**
 * Centralized timeout constants for the automation framework.
 *
 * Using constants ensures consistent wait times across the framework
 * and makes timeout adjustments easier (single source of truth).
 *
 * All values are in seconds.
 */
public final class TimeoutConstants {

    /**
     * Short wait - for quick UI updates (button clicks, simple text changes).
     * Use when element is expected to appear/change very quickly.
     */
    public static final int SHORT_WAIT = 5;

    /**
     * Medium wait - default wait for most UI interactions.
     * Use for standard page elements, modal dialogs, dropdown menus.
     */
    public static final int MEDIUM_WAIT = 10;

    /**
     * Long wait - for slow operations.
     * Use for AJAX calls, animations, data loading from server.
     */
    public static final int LONG_WAIT = 20;

    /**
     * Page load timeout - maximum time to wait for a page to fully load.
     */
    public static final int PAGE_LOAD_TIMEOUT = 30;

    /**
     * Polling interval - how often to check the condition during waits.
     * Smaller value = faster detection but more CPU usage.
     */
    public static final int POLLING_INTERVAL_MILLIS = 500;

    /**
     * Private constructor to prevent instantiation.
     */
    private TimeoutConstants() {
        throw new UnsupportedOperationException("TimeoutConstants is a utility class");
    }
}