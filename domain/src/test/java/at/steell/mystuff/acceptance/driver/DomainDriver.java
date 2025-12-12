package at.steell.mystuff.acceptance.driver;

import at.steell.mystuff.domain.entity.Asset;
import at.steell.mystuff.domain.exception.NotReadable;
import at.steell.mystuff.domain.interactor.AssetInteractor;
import at.steell.mystuff.domain.interactor.AssetInteractor.AssetInteractorFactory;
import at.steell.mystuff.domain.store.InMemoryAssetStore;

import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DomainDriver implements MyStuffAcceptanceDriver {
    private static final Logger LOGGER = Logger.getLogger(DomainDriver.class.getName());
    private final AssetInteractor assetInteractor = new AssetInteractorFactory()
        .withAssetStore(new InMemoryAssetStore())
        .create();

    private String currentUsername = null;
    private ThreadLocal<Collection<Asset>> currentAssets = ThreadLocal.withInitial(Set::of);

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

    private void npeIfCurrentUserName_null() {
        if (currentUsername == null) {
            throw new NullPointerException();
        }
    }

    @Override
    public String createAsset(final AssetOptions assetOptions) {
        currentUsername = assetOptions.authenticatedUser();
        npeIfCurrentUserName_null();
        return assetInteractor.createAsset(assetOptions.authenticatedUser());
    }

    @Override
    public void assertThatAssetExists(final String assetId) {
        Asset asset = assetInteractor.find(assetId, currentUsername);
        assertNotNull(asset);
        assertEquals(assetId, asset.getId());
    }

    @Override
    public void assertAssetNotReadable(final String assetId) {
        assertThrows(NotReadable.class, () -> assetInteractor.find(assetId, currentUsername));
    }

    @Override
    public void assertExceptional(final Supplier<?> action) {
        assertThrows(Exception.class, action::get);
    }

    @Override
    public Void listUserAssets(final String authenticatedUser) {
        currentAssets.set(assetInteractor.listAssets(authenticatedUser));
        return null;
    }

    @Override
    public void assertListOfAssetsSize(final int elementCount) {
        assertEquals(elementCount, currentAssets.get().size());
    }
}
