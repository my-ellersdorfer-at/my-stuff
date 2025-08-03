package at.steell.mystuff.domain.interactor;

import at.steell.mystuff.domain.entity.Asset;
import at.steell.mystuff.domain.interactor.AssetInteractor.AssetNotFound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AssetInteractorTest {
    AssetInteractor interactor = new AssetInteractor();

    @Test
    void instantiate() {
        assertNotNull(interactor);
    }

    @Test
    void createAsset() {
        String assetId = interactor.createAsset();
        assertNotNull(assetId);
    }

    @Test
    void findAsset() {
        String assetId = interactor.createAsset();
        Asset asset = interactor.find(assetId);
        assertNotNull(asset);
        assertEquals(assetId, asset.getId());
    }

    @Test
    void findAsset_nullId() {
        assertThrows(IllegalArgumentException.class, () -> interactor.find(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void findAsset_blankId(final String id) {
        assertThrows(IllegalArgumentException.class, () -> interactor.find(id));
    }

    @Test
    void findAsset_notFound() {
        String assetId = "non-existing-id";
        Exception expected = null;
        try {
            interactor.find(assetId);
        } catch (final Exception e) {
            expected = e;
        }
        assertInstanceOf(AssetNotFound.class, expected);
        assertEquals(assetId, ((AssetNotFound) expected).getAssetId());
    }


}
