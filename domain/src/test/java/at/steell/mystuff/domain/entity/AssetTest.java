package at.steell.mystuff.domain.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AssetTest {
    @Test
    public void createAsset() {
        Asset asset = Asset.createAsset();
        assertNotNull(asset);
        assertNotNull(asset.getId());
    }

    @Test
    public void setAssetName() {
        String newName = "New Asset Name";
        Asset asset = Asset.createAsset().nameAsset(newName);
        assertNotNull(asset);
        assertEquals(newName, asset.getName());
    }
}
