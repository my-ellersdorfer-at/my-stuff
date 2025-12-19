package at.steell.mystuff.acceptance.driver;

import at.steell.mystuff.application.web.AssetController.ListOfAssets;
import at.steell.mystuff.domain.entity.Asset;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.Collectors;

import static at.steell.mystuff.application.user.UserSessionFromSecurityContextHelper.fromSpringSecurityContext;
import static at.steell.mystuff.application.utils.AssetControllerSupport.createOperation;
import static at.steell.mystuff.application.utils.AssetControllerSupport.findOperation;
import static at.steell.mystuff.application.utils.AssetControllerSupport.listOperation;
import static at.steell.mystuff.application.utils.SecurityMockUtils.setupOidcUserSecurityContext;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MyStuffControllerDriver implements MyStuffAcceptanceDriver {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final MockMvc mockMvc;

    public MyStuffControllerDriver(final WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Override
    public void authenticateAsUser(final String username) {
        setupOidcUserSecurityContext(username);
    }

    @Override
    public void unauthenticateUser() {
        SecurityContextHolder.clearContext();
    }

    @Override
    public void assertThatUserIsAuthenticated(final String username) {
        assertEquals(username, fromSpringSecurityContext().userName());
    }

    private String noUserCreateAsset() {
        try {
            mockMvc.perform(createOperation())
                .andExpect(status().isUnauthorized());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private String defaultCreateAsset() {
        try {
            MvcResult response = mockMvc.perform(createOperation())
                .andExpect(status().isOk())
                .andReturn();
            return response.getResponse().getContentAsString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String createAsset(final AssetOptions assetOptions) {
        return assetOptions != null && assetOptions.authenticatedUser() != null
            ? defaultCreateAsset()
            : noUserCreateAsset();
    }

    @Override
    public void assertThatAssetExists(final String assetId) {
        try {
            mockMvc.perform(findOperation(assetId))
                .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void assertAssetNotReadable(final String assetId) {
        try {
            mockMvc.perform(findOperation(assetId))
                .andExpect(status().isForbidden());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void listUserAssets(final String authenticatedUser) {
        try {
            String response = mockMvc.perform(listOperation())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
            ListOfAssets listOfAssets = OBJECT_MAPPER.readValue(response, ListOfAssets.class);
            CURRENT_ASSET_IDS.set(listOfAssets.assets().stream().map(Asset::id)
                .collect(Collectors.toSet()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void noUserAssetsWithoutAuthentication() {
        try {
            mockMvc.perform(listOperation())
                .andExpect(status().isForbidden());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void assertListOfAssetsContains(final String assetId) {
        assertTrue(CURRENT_ASSET_IDS.get().contains(assetId));
    }

}
