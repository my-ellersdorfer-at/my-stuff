package at.steell.mystuff.acceptance.dsl;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertThrows;

public interface MyStuffAcceptanceDsl {

    void authenticateAsUser(String username);

    void unauthenticateUser();

    void assertThatUserIsAuthenticated(String username);

    record AssetOptions(String authenticatedUser) {
    }

    String createAsset(AssetOptions assetOptions);

    void assertThatAssetExists(String assetId);

    void assertAssetNotReadable(String assetId);

    default void assertExceptional(Supplier<?> action) {
        assertThrows(Exception.class, action::get);
    }

    Void listUserAssets(String authenticatedUser);

    void assertListOfAssetsIsEmpty();

    void assertListOfAssetsContains(String assetId);
}
