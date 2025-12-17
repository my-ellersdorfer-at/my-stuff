package at.steell.mystuff.domain.entity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AssetTest {
    private Asset createDefaultAsset(final String owner) {
        return Asset.createAsset(owner);
    }

    @Test
    void createAsset() {
        String owner = UUID.randomUUID().toString();
        Asset asset = createDefaultAsset(owner);
        assertNotNull(asset);
        assertNotNull(asset.id());
    }

    @Test
    void createAsset_hasOwner() {
        String owner = UUID.randomUUID().toString();
        Asset asset = createDefaultAsset(owner);
        assertEquals(owner, asset.owner());
    }

    @Test
    void setAssetName() {
        String newName = "New Asset Name";
        Asset asset = Asset.createAsset(UUID.randomUUID().toString())
            .nameAsset(newName);
        assertNotNull(asset);
        assertEquals(newName, asset.name());
    }
}
