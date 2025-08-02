package at.steell.mystuff.acceptance.driver;

import java.util.function.Supplier;

public class DomainDriver implements MyStuffAcceptanceDriver {
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
        return "";
    }

    @Override
    public void assertThatAssetExists(final String assetId) {

    }

    @Override
    public void assertAssetNotReadable(final String assetId) {

    }

    @Override
    public void assertExceptional(final Supplier<?> action) {

    }
}
