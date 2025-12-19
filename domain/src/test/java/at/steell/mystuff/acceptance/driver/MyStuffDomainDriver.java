package at.steell.mystuff.acceptance.driver;

import at.steell.mystuff.domain.entity.Asset;
import at.steell.mystuff.domain.exception.NotReadable;
import at.steell.mystuff.domain.interactor.AssetInteractor;
import at.steell.mystuff.domain.interactor.AssetInteractor.AssetInteractorFactory;
import at.steell.mystuff.domain.interactor.AssetInteractor.NoOwner;
import at.steell.mystuff.domain.store.InMemoryAssetStore;

import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MyStuffDomainDriver implements MyStuffAcceptanceDriver {
    private static final Logger LOGGER = Logger.getLogger(MyStuffDomainDriver.class.getName());
    private final AssetInteractor assetInteractor = new AssetInteractorFactory()
        .withAssetStore(new InMemoryAssetStore())
        .create();

    private String currentUsername = null;

    @Override
    public void authenticateAsUser(final String username) {
        currentUsername = username;
        LOGGER.info("authenticating as user " + username);
        assertEquals(username, currentUsername);
    }

    @Override
    public void unauthenticateUser() {
        LOGGER.info("unauthenticating " + currentUsername);
        currentUsername = null;
    }

    @Override
    public void assertThatUserIsAuthenticated(final String username) {
        LOGGER.info("authenticated user: " + currentUsername
            + ", asserted user: " + username);
        assertEquals(currentUsername, username);
    }

    private String noUserCreateAsset() {
        assertThrows(NoOwner.class, () -> assetInteractor.createAsset(null));
        return null;
    }

    private String defaultCreateAsset() {
        return assetInteractor.createAsset(currentUsername);
    }

    @Override
    public String createAsset(final AssetOptions assetOptions) {
        return assetOptions != null && assetOptions.authenticatedUser() != null
            ? defaultCreateAsset()
            : noUserCreateAsset();
    }

    @Override
    public void assertThatAssetExists(final String assetId) {
        Asset asset = assetInteractor.find(assetId, currentUsername);
        assertNotNull(asset);
        assertEquals(assetId, asset.id());
    }

    @Override
    public void assertAssetNotReadable(final String assetId) {
        assertThrows(NotReadable.class, () -> assetInteractor.find(assetId, currentUsername));
    }

    @Override
    public void listUserAssets(final String authenticatedUser) {
        CURRENT_ASSET_IDS.set(assetInteractor.listAssets(authenticatedUser)
            .stream().map(Asset::id)
            .collect(Collectors.toSet()));
    }

    @Override
    public void noUserAssetsWithoutAuthentication() {
        assertThrows(RuntimeException.class, () -> assetInteractor.listAssets(null));
    }

    @Override
    public void assertListOfAssetsContains(final String assetId) {
        assertTrue(CURRENT_ASSET_IDS.get().contains(assetId));
    }
}
