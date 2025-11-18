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
        String assetName = "Test Asset";
        driver.assertExceptional(() -> driver.createAsset(
            new AssetOptions(assetName, null)));
    }

    @Test
    void authenticateUser() {
        String userName = "Test User";
        driver.authenticateAsUser(userName);
        driver.assertThatUserIsAuthenticated(userName);
    }

    private String createAssetWithUser(final String userName, final String assetName) {
        driver.authenticateAsUser(userName);
        return driver.createAsset(new AssetOptions(assetName, userName));
    }

    @Test
    void createAsset() {
        driver.assertThatAssetExists(createAssetWithUser("Test User", "Test Asset"));
    }

    @Test
    void unauthenticatedCannotRead() {
        String assetId = createAssetWithUser("User A", "Test Asset");
        driver.assertThatAssetExists(assetId);
        driver.unauthenticateUser();
        driver.assertAssetNotReadable(assetId);
    }

    @Test
    void otherCannotRead() {
        String assetId = createAssetWithUser("User A", "Test Asset");
        driver.assertThatAssetExists(assetId);
        driver.authenticateAsUser("User B");
        driver.assertAssetNotReadable(assetId);
    }

    @Test
    void listAssetsWithoutAuthentication_exceptional() {
        driver.authenticateAsUser("User A");
        driver.createAsset(new AssetOptions("Test Asset", "User A"));
        driver.unauthenticateUser();
        driver.assertExceptional(() -> driver.listUserAssets(null));
    }


    @Test
    void listAssetsDifferentAuthentication_emptyResults() {
        driver.authenticateAsUser("User A");
        driver.createAsset(new AssetOptions("Test Asset", "User A"));
        driver.unauthenticateUser();
        driver.listUserAssets("User B");
        driver.assertListOfAssetsSize(0);
    }

    @Test
    void listAssets_containsCreated() {
        String user = "User A";
        driver.authenticateAsUser(user);
        driver.createAsset(new AssetOptions("Test Asset", user));
        driver.listUserAssets(user);
        driver.assertListOfAssetsSize(1);
    }
}
