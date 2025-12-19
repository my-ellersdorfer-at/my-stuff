package at.steell.mystuff.acceptance.scenario;

import at.steell.mystuff.acceptance.driver.MyStuffAcceptanceDriver;
import at.steell.mystuff.acceptance.dsl.MyStuffAcceptanceDsl.AssetOptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public abstract class AbstractAssetAcceptanceTest {
    private final MyStuffAcceptanceDriver driver;

    public AbstractAssetAcceptanceTest(final MyStuffAcceptanceDriver aDriver) {
        driver = aDriver;
    }

    protected MyStuffAcceptanceDriver getDriver() {
        return driver;
    }

    @Test
    void createAssetWithoutAuthentication() {
        assertDoesNotThrow(() -> driver.createAsset(new AssetOptions(null)));
    }

    @Test
    void authenticateUser() {
        driver.authenticateAsUser(driver.userA());
        driver.assertThatUserIsAuthenticated(driver.userA());
    }

    private String createAssetWithUser(final String userName) {
        driver.authenticateAsUser(userName);
        return driver.createAsset(new AssetOptions(userName));
    }

    @Test
    void createAsset() {
        driver.assertThatAssetExists(createAssetWithUser(driver.userA()));
    }

    @Test
    void unauthenticatedCannotRead() {
        String assetId = createAssetWithUser(driver.userA());
        driver.assertThatAssetExists(assetId);
        driver.unauthenticateUser();
        driver.assertAssetNotReadable(assetId);
    }

    @Test
    void otherCannotRead() {
        String assetId = createAssetWithUser(driver.userA());
        driver.assertThatAssetExists(assetId);
        driver.authenticateAsUser(driver.userB());
        driver.assertAssetNotReadable(assetId);
    }

    @Test
    void listNoAssetsWithoutAuthentication() {
        driver.authenticateAsUser(driver.userA());
        driver.createAsset(new AssetOptions(driver.userA()));
        driver.unauthenticateUser();
        assertDoesNotThrow(driver::noUserAssetsWithoutAuthentication);
    }

    @Test
    void listAssetsDifferentAuthentication_emptyResults() {
        driver.authenticateAsUser(driver.userA());
        driver.createAsset(new AssetOptions(driver.userA()));
        driver.unauthenticateUser();
        driver.authenticateAsUser(driver.userB());
        driver.listUserAssets(driver.userB());
        driver.assertListOfAssetsIsEmpty();
    }

    @Test
    void listAssets_containsCreated() {
        driver.authenticateAsUser(driver.userA());
        String assetId = driver.createAsset(new AssetOptions(driver.userA()));
        driver.listUserAssets(driver.userA());
        driver.assertListOfAssetsContains(assetId);
    }
}
