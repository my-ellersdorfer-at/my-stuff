package at.steell.mystuff.domain.store;

import at.steell.mystuff.domain.entity.Asset;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class InMemoryAssetStore implements AssetStore {
    private final Map<String, Asset> assets = new HashMap<>();

    @Override
    public Asset get(final String assetId) {
        return assets.get(assetId);
    }

    @Override
    public String save(final Asset asset) {
        assets.put(asset.getId(), asset);
        return asset.getId();
    }

    @Override
    public Set<Asset> getByOwner(final String owner) {
        return assets.values().stream()
            .filter(asset -> owner.equals(asset.getOwner()))
            .collect(Collectors.toSet());
    }
}
