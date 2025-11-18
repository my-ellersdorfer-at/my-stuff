package at.steell.mystuff.domain.interactor;

import at.steell.mystuff.domain.entity.Asset;
import at.steell.mystuff.domain.exception.NotAvailable;
import at.steell.mystuff.domain.exception.NotReadable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class AssetInteractor {
    private final Map<String, Asset> assets = new HashMap<>();

    public String createAsset(final String owner) {
        Asset asset = Asset.createAsset(owner);
        assets.put(asset.getId(), asset);
        return asset.getId();
    }

    public Collection<Asset> listAssets(final String authenticatedUser) {
        if (authenticatedUser == null) {
            throw new NotAvailable(null);
        }
        return assets.values().stream()
            .filter(asset -> authenticatedUser.equals(asset.getOwner()))
            .collect(Collectors.toSet());
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

    private void validateWithOwner(final String assetId, final String owner) {
        if (owner == null || owner.isBlank()) {
            throw new NotReadable(assetId);
        }
    }

    private void validateAssetId(final String assetId) {
        if (assetId == null || assetId.isBlank()) {
            throw new IllegalArgumentException("Asset ID cannot be null/blank.");
        }
    }

    private void proofAssetExists(final Asset asset, final String assetId) {
        if (asset == null) {
            throw new AssetNotFound(assetId);
        }
    }

    private void proofOwnerOfAsset(final String assetId, final String owner, final Asset asset) {
        if (!owner.equals(asset.getOwner())) {
            throw  new NotReadable(assetId);
        }
    }

    public Asset find(final String assetId, final String owner) {
        validateWithOwner(assetId, owner);
        validateAssetId(assetId);
        Asset asset = assets.get(assetId);
        proofAssetExists(asset, assetId);
        proofOwnerOfAsset(assetId, owner, asset);
        return asset;
    }
}
