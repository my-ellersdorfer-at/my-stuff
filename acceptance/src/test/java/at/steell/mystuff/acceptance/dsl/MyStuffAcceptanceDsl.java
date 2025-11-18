package at.steell.mystuff.acceptance.dsl;

import java.util.function.Supplier;

public interface MyStuffAcceptanceDsl {

    void authenticateAsUser(String username);

    void unauthenticateUser();

    void assertThatUserIsAuthenticated(String username);

    record AssetOptions(String assetName, String authenticatedUser) {
    }

    String createAsset(AssetOptions assetOptions);

    void assertThatAssetExists(String assetId);

    void assertAssetNotReadable(String assetId);

    void assertExceptional(Supplier<?> action);

    Void listUserAssets(String authenticatedUser);

    void assertListOfAssetsSize(int elementCount);
}
