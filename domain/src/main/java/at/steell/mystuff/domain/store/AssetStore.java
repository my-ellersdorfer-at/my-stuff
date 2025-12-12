package at.steell.mystuff.domain.store;

import at.steell.mystuff.domain.entity.Asset;

import java.util.Set;

public interface AssetStore {
    Asset get(String assetId);

    Set<Asset> getByOwner(String owner);

    String save(Asset asset);
}
