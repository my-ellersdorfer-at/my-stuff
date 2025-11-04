package at.steell.mystuff.domain.interactor;

import at.steell.mystuff.domain.entity.Asset;
import at.steell.mystuff.domain.exception.NotReadable;
import at.steell.mystuff.domain.interactor.AssetInteractor.AssetNotFound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AssetInteractorTest {
    AssetInteractor interactor = new AssetInteractor();

    @Test
    void instantiate() {
        assertNotNull(interactor);
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
        assertEquals(assetId, asset.getId());
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

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void findAsset_ownerBlank_assetId_notReadable(final String owner) {
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
}
