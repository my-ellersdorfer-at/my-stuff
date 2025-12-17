package at.steell.mystuff.acceptance.scenario;

import at.steell.mystuff.acceptance.driver.MyStuffAcceptanceDriver;
import at.steell.mystuff.acceptance.dsl.MyStuffAcceptanceDsl.AssetOptions;
import org.junit.jupiter.api.Test;

public abstract class AbstractAssetAcceptanceTest {
    private final MyStuffAcceptanceDriver driver;

    public AbstractAssetAcceptanceTest(final MyStuffAcceptanceDriver aDriver) {
        driver = aDriver;
    }

    @Test
    void createAssetWithoutAuthentication() {
        driver.assertExceptional(() -> driver.createAsset(
            new AssetOptions(null)));
    }

    @Test
    void authenticateUser() {
        String userName = "Test User";
        driver.authenticateAsUser(userName);
        driver.assertThatUserIsAuthenticated(userName);
    }

    private String createAssetWithUser(final String userName) {
        driver.authenticateAsUser(userName);
        return driver.createAsset(new AssetOptions(userName));
    }

    @Test
    void createAsset() {
        driver.assertThatAssetExists(createAssetWithUser("Test User"));
    }

    @Test
    void unauthenticatedCannotRead() {
        String assetId = createAssetWithUser("User A");
        driver.assertThatAssetExists(assetId);
        driver.unauthenticateUser();
        driver.assertAssetNotReadable(assetId);
    }

    @Test
    void otherCannotRead() {
        String assetId = createAssetWithUser("User A");
        driver.assertThatAssetExists(assetId);
        driver.authenticateAsUser("User B");
        driver.assertAssetNotReadable(assetId);
    }

    @Test
    void listAssetsWithoutAuthentication_exceptional() {
        driver.authenticateAsUser("User A");
        driver.createAsset(new AssetOptions("User A"));
        driver.unauthenticateUser();
        driver.assertExceptional(() -> driver.listUserAssets(null));
    }

    @Test
    void listAssetsDifferentAuthentication_emptyResults() {
        driver.authenticateAsUser("User A");
        driver.createAsset(new AssetOptions("User A"));
        driver.unauthenticateUser();
        driver.authenticateAsUser("User B");
        driver.listUserAssets("User B");
        driver.assertListOfAssetsIsEmpty();
    }

    @Test
    void listAssets_containsCreated() {
        String user = "User A";
        driver.authenticateAsUser(user);
        String assetId = driver.createAsset(new AssetOptions(user));
        driver.listUserAssets(user);
        driver.assertListOfAssetsContains(assetId);
    }
}
