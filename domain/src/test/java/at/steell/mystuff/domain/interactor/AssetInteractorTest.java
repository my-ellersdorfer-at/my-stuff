package at.steell.mystuff.domain.interactor;

import at.steell.mystuff.domain.entity.Asset;
import at.steell.mystuff.domain.exception.NotAvailable;
import at.steell.mystuff.domain.exception.NotReadable;
import at.steell.mystuff.domain.interactor.AssetInteractor.AssetInteractorFactory;
import at.steell.mystuff.domain.interactor.AssetInteractor.AssetNotFound;
import at.steell.mystuff.domain.interactor.AssetInteractor.NoOwner;
import at.steell.mystuff.domain.store.InMemoryAssetStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AssetInteractorTest {
    AssetInteractor interactor = new AssetInteractorFactory()
        .withAssetStore(new InMemoryAssetStore())
        .withAllKindsOfAnonymous(allKindsOfAnonymous())
        .create();

    @Test
    void instantiate() {
        assertNotNull(interactor);
    }

    @Test
    void withoutAssetStore() {
        Exception exception = null;
        try {
            new AssetInteractorFactory().create();
        } catch (Exception e) {
            exception = e;
        }
        assertInstanceOf(NullPointerException.class, exception);
    }

    private AssetInteractor assetInteractorWithoutAllKindsOfAnonymous() {
        return new AssetInteractorFactory()
            .withAssetStore(new InMemoryAssetStore())
            .create();
    }

    @Test
    void withoutAllKindsOfAnonymous() {
        assertDoesNotThrow(this::assetInteractorWithoutAllKindsOfAnonymous);
    }

    private AssetInteractor assetInteractorWithAllKindsOfAnonymousNull() {
        return new AssetInteractorFactory()
            .withAssetStore(new InMemoryAssetStore())
            .withAllKindsOfAnonymous(null)
            .create();
    }

    @Test
    void withAllKindsOfAnonymous_null() {
        assertDoesNotThrow(this::assetInteractorWithAllKindsOfAnonymousNull);
    }

    @Test
    void createAsset() {
        String assetId = interactor.createAsset(UUID.randomUUID().toString());
        assertNotNull(assetId);
    }

    @Test
    void findAsset() {
        String owner = UUID.randomUUID().toString();
        String assetId = interactor.createAsset(owner);
        Asset asset = interactor.find(assetId, owner);
        assertNotNull(asset);
        assertEquals(assetId, asset.id());
    }

    @Test
    void findAsset_nullId() {
        assertThrows(IllegalArgumentException.class, () -> interactor.find(null, "owner"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void findAsset_blankId(final String id) {
        assertThrows(IllegalArgumentException.class, () -> interactor.find(id, "owner"));
    }

    private Exception findExceptional(final String assetId, final String owner) {
        Exception expected = null;
        try {
            interactor.find(assetId, owner);
        } catch (final Exception e) {
            expected = e;
        }
        return expected;
    }

    @Test
    void findAsset_notFound() {
        String assetId = "non-existing-id";
        Exception expected = findExceptional(assetId, assetId);
        assertInstanceOf(AssetNotFound.class, expected);
        assertEquals(assetId, ((AssetNotFound) expected).getAssetId());
    }

    @Test
    void findAsset_ownerNull_assetId_notReadable() {
        Exception expected = findExceptional(null, null);
        assertInstanceOf(NotReadable.class, expected);
        assertNull(((NotReadable) expected).getAssetId());
    }


    private static Set<String> allKindsOfAnonymous() {
        return Set.of("", " ", "   ", "anonymous", "guest", "null");
    }

    @ParameterizedTest
    @MethodSource("allKindsOfAnonymous")
    void findAsset_anonymous_assetId_notReadable(final String owner) {
        Exception expected = findExceptional(null, owner);
        assertInstanceOf(NotReadable.class, expected);
        assertNull(((NotReadable) expected).getAssetId());
    }

    @Test
    void findAsset_ownerNull_withAssetId_notReadable() {
        String assetId = UUID.randomUUID().toString();
        Exception expected = findExceptional(assetId, null);
        assertInstanceOf(NotReadable.class, expected);
        assertEquals(assetId, ((NotReadable) expected).getAssetId());
    }

    @Test
    void findAsset_differentOwner_notReadable() {
        String assetId = interactor.createAsset(UUID.randomUUID().toString());
        Exception expected = findExceptional(assetId, UUID.randomUUID().toString());
        assertInstanceOf(NotReadable.class, expected);
        assertEquals(assetId, ((NotReadable) expected).getAssetId());
    }

    private Exception listAssets_notAuthenticated(
        final AssetInteractor assetInteractor, final String userName) {
        try {
            assetInteractor.listAssets(userName);
        } catch (final Exception e) {
            return e;
        }
        return null;
    }

    private Exception listAssets_notAuthenticated(final String userName) {
        return listAssets_notAuthenticated(interactor, userName);
    }

    @Test
    void listAssets_nullUser_notAvailable() {
        assertInstanceOf(NotAvailable.class, listAssets_notAuthenticated(null));
    }

    @Test
    void listAssets_allKindsOfAnonymous_null_nullUser_notAvailable() {
        assertInstanceOf(NotAvailable.class, listAssets_notAuthenticated(
            assetInteractorWithAllKindsOfAnonymousNull(), null));
    }

    @Test
    void listAssets_withoutAllKindsOfAnonymous_nullUser_notAvailable() {
        assertInstanceOf(NotAvailable.class, listAssets_notAuthenticated(
            assetInteractorWithoutAllKindsOfAnonymous(), null));
    }

    @ParameterizedTest
    @MethodSource("allKindsOfAnonymous")
    void listAssets_allKindsOfAnonymous_notAvailable(final String userName) {
        assertInstanceOf(NotAvailable.class, listAssets_notAuthenticated(userName));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void listAssets_allKindsOfAnonymous_null_notAvailable(final String userName) {
        assertInstanceOf(NotAvailable.class, listAssets_notAuthenticated(
            assetInteractorWithAllKindsOfAnonymousNull(), userName));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void listAssets_withoutAllKindsOfAnonymous_notAvailable(final String userName) {
        assertInstanceOf(NotAvailable.class, listAssets_notAuthenticated(
            assetInteractorWithoutAllKindsOfAnonymous(), userName));
    }

    @Test
    void listAssets_noAssets() {
        assertTrue(interactor.listAssets(UUID.randomUUID().toString()).isEmpty());
    }

    @Test
    void listAssets_withAssets() {
        String owner = UUID.randomUUID().toString();
        String assetId = interactor.createAsset(owner);
        Collection<Asset> assets = interactor.listAssets(owner);
        assertEquals(1, assets.size());
        assertEquals(assetId, assets.iterator().next().id());
    }

    @Test
    void createAsset_null() {
        assertThrows(NoOwner.class, () -> interactor.createAsset(null));
    }
}
