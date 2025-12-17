package at.steell.mystuff.application.utils;

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

public final class AssetControllerSupport {
    private AssetControllerSupport() {
    }

    public static MockHttpServletRequestBuilder findOperation(final String assetId) {
        return get("/api/assets/{id}", assetId);
    }

    public static MockHttpServletRequestBuilder listOperation() {
        return get("/api/assets/list");
    }

    public static MockHttpServletRequestBuilder createOperation() {
        return put("/api/assets/create");
    }
}
