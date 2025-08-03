package at.steell.mystuff.domain.interactor;

import at.steell.mystuff.domain.entity.Asset;

import java.util.HashMap;
import java.util.Map;

public class AssetInteractor {
    private final Map<String, Asset> assets = new HashMap<>();

    public String createAsset() {
        Asset asset = Asset.createAsset();
        assets.put(asset.getId(), asset);
        return asset.getId();
    }

    public static class AssetNotFound extends RuntimeException {
        private final String assetId;

        public AssetNotFound(final String notFoundAssetId) {
            super(String.format("Asset with ID '%s' could not be found.", notFoundAssetId));
            assetId = notFoundAssetId;
        }

        public String getAssetId() {
            return assetId;
        }
    }

    public Asset find(final String assetId) {
        if (assetId == null || assetId.isBlank()) {
            throw new IllegalArgumentException("Asset ID cannot be null/blank.");
        }
        Asset asset = assets.get(assetId);
        if (asset == null) {
            throw new AssetNotFound(assetId);
        }
        return asset;
    }
}
