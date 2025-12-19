package at.steell.mystuff.acceptance.driver;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.logging.Level;
import java.util.logging.Logger;

import static at.steell.mystuff.acceptance.driver.NavigationHelper.halfSecondNavigationTimeout;

public class OauthUiLoginFlow {
    private static final Logger LOGGER = Logger.getLogger(OauthUiLoginFlow.class.getName());

    private final Page page;
    private final ServerUrl serverUrl;

    public OauthUiLoginFlow(final Page playwrightPage, final ServerUrl aServerUrl) {
        page = playwrightPage;
        serverUrl = aServerUrl;
    }

    public void login() {
        try {
            oauthLoginFlow();
        } catch (final Exception e) {
            LOGGER.log(Level.WARNING,
                "OAuth login might have failed, check if test execution fails during post execution.");
        }
    }

    private void oauthLoginFlow() {
        navigateToOauthLogin();
        submitLoginForm();
        awaitLoginResponse();
        awaitApplicationUrl();
    }

    private void navigateToOauthLogin() {
        page.navigate(serverUrl.oauthLoginUrl());
        page.waitForURL(url -> url.contains("/protocol/openid-connect"),
            halfSecondNavigationTimeout());
    }

    private void submitLoginForm() {
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Username"))
            .fill("user1");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password"))
            .fill("this-is-a-very-strong-password");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
    }

    private void awaitLoginResponse() {
        page.waitForURL(url -> url.contains(serverUrl.baseUrl() + "/login/oauth2/code"),
            halfSecondNavigationTimeout());
    }

    private void awaitApplicationUrl() {
        page.waitForURL(url -> url.contains(serverUrl.buildApplicationUrl()),
            halfSecondNavigationTimeout());
    }
}
