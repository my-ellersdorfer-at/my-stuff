package at.steell.mystuff.acceptance.driver;

import com.microsoft.playwright.Page;

public final class NavigationHelper {
    private static final int REDIRECT_TIMEOUT_MS = 500;

    private NavigationHelper() {
    }

    public static Page.WaitForURLOptions halfSecondNavigationTimeout() {
        return new Page.WaitForURLOptions().setTimeout(REDIRECT_TIMEOUT_MS);
    }
}
