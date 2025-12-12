package at.steell.mystuff.domain.interactor;

import at.steell.mystuff.domain.entity.Asset;
import at.steell.mystuff.domain.exception.NotAvailable;
import at.steell.mystuff.domain.exception.NotReadable;
import at.steell.mystuff.domain.store.AssetStore;

import java.util.Collection;

public final class AssetInteractor {
    private final AssetStore assetStore;

    private AssetInteractor(final AssetStore theAssetStore) {
        assetStore = theAssetStore;
    }

    public static final class AssetInteractorFactory {
        private AssetStore assetStore;

        public AssetInteractorFactory withAssetStore(final AssetStore arg) {
            assetStore = arg;
            return this;
        }

        public AssetInteractor create() {
            return new AssetInteractor(assetStore);
        }
    }

    public String createAsset(final String owner) {
        return assetStore.save(Asset.createAsset(owner));
    }

    public Collection<Asset> listAssets(final String authenticatedUser) {
        if (authenticatedUser == null) {
            throw new NotAvailable(null);
        }
        return assetStore.getByOwner(authenticatedUser);
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
        Asset asset = assetStore.get(assetId);
        proofAssetExists(asset, assetId);
        proofOwnerOfAsset(assetId, owner, asset);
        return asset;
    }
}
