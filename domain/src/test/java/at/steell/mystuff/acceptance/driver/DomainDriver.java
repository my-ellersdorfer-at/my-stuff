package at.steell.mystuff.acceptance.driver;

import at.steell.mystuff.domain.entity.Asset;
import at.steell.mystuff.domain.interactor.AssetInteractor;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DomainDriver implements MyStuffAcceptanceDriver {
    private final AssetInteractor assetInteractor = new AssetInteractor();

    @Override
    public void authenticateAsUser(final String username) {

    }

    @Override
    public void unauthenticateUser() {

    }

    @Override
    public void assertThatUserIsAuthenticated(final String username) {

    }

    @Override
    public String createAsset(final AssetOptions assetOptions) {
        return assetInteractor.createAsset();
    }

    @Override
    public void assertThatAssetExists(final String assetId) {
        Asset asset = assetInteractor.find(assetId);
        assertNotNull(asset);
        assertEquals(assetId, asset.getId());
    }

    @Override
    public void assertAssetNotReadable(final String assetId) {

    }

    @Override
    public void assertExceptional(final Supplier<?> action) {

    }
}
