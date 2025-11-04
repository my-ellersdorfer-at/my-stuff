package at.steell.mystuff.domain.exception;

public class NotReadable extends RuntimeException {
    private final String assetId;

    public NotReadable(final String theAssetId) {
        super(String.format("Asset with ID '%s' is not readable.", theAssetId));
        assetId = theAssetId;
    }

    public String getAssetId() {
        return assetId;
    }
}
