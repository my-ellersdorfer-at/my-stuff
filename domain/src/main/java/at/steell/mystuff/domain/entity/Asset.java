package at.steell.mystuff.domain.entity;

import java.util.UUID;

public final class Asset {
    private final String id;
    private final String name;
    private final String owner;

    private Asset(final Builder builder) {
        id = builder.id;
        name = builder.name;
        owner = builder.owner;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    private static class Builder {
        private String id;
        private String name;
        private String owner;

        public Builder id(final String theId) {
            id = theId;
            return this;
        }

        public Builder name(final String theName) {
            name = theName;
            return this;
        }

        public Builder owner(final String theOwner) {
            owner = theOwner;
            return this;
        }

        public Asset build() {
            return new Asset(this);
        }
    }

    private static Builder toBuilder(final Asset asset) {
        Builder builder = new Builder();
        builder.id = asset.id;
        builder.name = asset.name;
        builder.owner = asset.owner;
        return builder;
    }

    public static Asset createAsset(final String owner) {
        return new Builder().id(UUID.randomUUID().toString())
            .owner(owner)
            .build();
    }

    public Asset nameAsset(final String assetName) {
        return Asset.toBuilder(this).name(assetName).build();
    }
}
