package at.steell.mystuff.acceptance.driver;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import com.microsoft.playwright.options.AriaRole;

import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MyStuffUiDriver implements MyStuffAcceptanceDriver {
    private static final boolean HEADLESS = true;
    private Playwright playwright;
    private BrowserContext context;
    private Page page;
    private ServerUrl serverUrl;

    @Override
    public String userA() {
        return "user1";
    }

    @Override
    public String userB() {
        return "user2";
    }

    private void setupContextTracing() {
        context.tracing().start(new Tracing.StartOptions()
            .setScreenshots(true)
            .setSnapshots(true)
            .setSources(true));
    }

    public void startContext() {
        context = playwright.chromium()
            .launch(new BrowserType.LaunchOptions()
                .setHeadless(HEADLESS))
            .newContext();
        setupContextTracing();
    }

    public void stopContext(final String relativeArchivePath) {
        context.tracing().stop(new Tracing.StopOptions()
            .setPath(Paths.get(relativeArchivePath)));
        context.close();
        playwright.close();
    }

    public void setupPlaywright() {
        playwright = Playwright.create();
    }

    public void closePlaywright() {
        playwright.close();
    }

    public void setServerUrl(final ServerUrl aServerUrl) {
        serverUrl = aServerUrl;
    }

    private void oauthLogin() {
        page = context.newPage();
        OauthUiLoginFlow oauthUiLoginFlow = new OauthUiLoginFlow(page, serverUrl);
        oauthUiLoginFlow.login();
    }

    @Override
    public void authenticateAsUser(final String username) {
        oauthLogin();
    }

    @Override
    public void unauthenticateUser() {
        context.clearCookies();
        page = context.newPage();
    }

    @Override
    public void assertThatUserIsAuthenticated(final String username) {
        page.navigate(serverUrl.baseUrl() + "/me");
        assertTrue(page.locator("app-me .me-handle").textContent().contains("@" + username));
    }

    private String noUserCreateAsset() {
        page = context.newPage();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Username")).isVisible();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password")).isVisible();
        return null;
    }

    private String defaultCreateAsset() {
        page.navigate(serverUrl.baseUrl() + "/assets/create");
        page.getByTestId("create-asset-button").click();
        return page.getByTestId("creation-result").locator("span.mono").textContent().trim();
    }

    @Override
    public String createAsset(final AssetOptions assetOptions) {
        return assetOptions != null && assetOptions.authenticatedUser() != null
            ? defaultCreateAsset()
            : noUserCreateAsset();
    }

    @Override
    public void assertThatAssetExists(final String assetId) {
        page.navigate(serverUrl.baseUrl() + "/assets/detail");
        page.getByRole(AriaRole.TEXTBOX).fill(assetId);
        page.getByRole(AriaRole.BUTTON).click();
        assertTrue(page.getByTestId("asset-detail").textContent().contains(assetId));
    }

    @Override
    public void assertAssetNotReadable(final String assetId) {
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Username")).isVisible();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password")).isVisible();
    }

    @Override
    public void noUserAssetsWithoutAuthentication() {
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Username")).isVisible();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password")).isVisible();
    }

    @Override
    public void listUserAssets(final String authenticatedUser) {
        CURRENT_ASSET_IDS.set(Set.of());
        page.navigate(serverUrl.baseUrl() + "/assets/list");
        page.waitForResponse("**/api/assets/list",
            () -> page.getByRole(AriaRole.BUTTON).click());

        List<String> displayedIds = page.locator("[data-testid='asset-row'] .mono")
            .allTextContents()
            .stream()
            .map(String::trim)
            .toList();
        CURRENT_ASSET_IDS.set(displayedIds);
    }

    @Override
    public void assertListOfAssetsContains(final String assetId) {
        assertTrue(CURRENT_ASSET_IDS.get().contains(assetId));
    }
}
