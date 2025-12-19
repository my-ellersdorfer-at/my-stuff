package at.steell.mystuff.acceptance.dsl;

public interface MyStuffAcceptanceDsl {

    void authenticateAsUser(String username);

    void unauthenticateUser();

    void assertThatUserIsAuthenticated(String username);

    record AssetOptions(String authenticatedUser) {
    }

    String createAsset(AssetOptions assetOptions);

    void assertThatAssetExists(String assetId);

    void assertAssetNotReadable(String assetId);

    void listUserAssets(String authenticatedUser);

    void noUserAssetsWithoutAuthentication();

    void assertListOfAssetsIsEmpty();

    void assertListOfAssetsContains(String assetId);
}
