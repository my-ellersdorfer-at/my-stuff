package at.steell.mystuff.domain.interactor;

import at.steell.mystuff.domain.entity.Asset;
import at.steell.mystuff.domain.exception.NotAvailable;
import at.steell.mystuff.domain.exception.NotReadable;
import at.steell.mystuff.domain.store.AssetStore;

import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElseGet;

public final class AssetInteractor {
    private final AssetStore assetStore;
    private final Set<String> allKindsOfAnonymous;

    private AssetInteractor(final AssetStore theAssetStore,
                            final Set<String> externalKindsOfAnonymous) {
        assetStore = theAssetStore;
        allKindsOfAnonymous = requireNonNullElseGet(externalKindsOfAnonymous, Set::of);
    }

    public static final class AssetInteractorFactory {
        private AssetStore assetStore;
        private Set<String> allKindsOfAnonymous;

        public AssetInteractorFactory withAssetStore(final AssetStore arg) {
            assetStore = arg;
            return this;
        }

        public AssetInteractorFactory withAllKindsOfAnonymous(final Set<String> arg) {
            allKindsOfAnonymous = arg;
            return this;
        }

        public AssetInteractor create() {
            return new AssetInteractor(
                requireNonNull(assetStore, "asset store must be defined"),
                allKindsOfAnonymous);
        }
    }

    public String createAsset(final String owner) {
        validateWithOwner(owner, () -> new NoOwner(allKindsOfAnonymous));
        return assetStore.save(Asset.createAsset(owner));
    }

    public Collection<Asset> listAssets(final String authenticatedUser) {
        validateWithOwner(authenticatedUser, () -> new NotAvailable(authenticatedUser));
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

    public static class NoOwner extends IllegalStateException {
        public NoOwner(final Set<String> allKindsOfAnonymous) {
            super("Owner cannot be " + allKindsOfAnonymous);
        }
    }

    private void validateWithOwner(final String owner, final Supplier<RuntimeException> exceptionSupplier) {
        if (owner == null || owner.isBlank() || allKindsOfAnonymous.contains(owner)) {
            throw exceptionSupplier.get();
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
        if (!owner.equals(asset.owner())) {
            throw  new NotReadable(assetId);
        }
    }

    public Asset find(final String assetId, final String owner) {
        validateWithOwner(owner, () -> new NotReadable(assetId));
        validateAssetId(assetId);
        Asset asset = assetStore.get(assetId);
        proofAssetExists(asset, assetId);
        proofOwnerOfAsset(assetId, owner, asset);
        return asset;
    }
}
