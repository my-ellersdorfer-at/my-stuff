package at.steell.mystuff.acceptance.driver;

import at.steell.mystuff.application.web.AssetController.ListOfAssets;
import at.steell.mystuff.domain.entity.Asset;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.function.Supplier;

import static at.steell.mystuff.application.user.UserSessionFromSecurityContextHelper.fromSpringSecurityContext;
import static at.steell.mystuff.application.utils.AssetControllerSupport.createOperation;
import static at.steell.mystuff.application.utils.AssetControllerSupport.findOperation;
import static at.steell.mystuff.application.utils.AssetControllerSupport.listOperation;
import static at.steell.mystuff.application.utils.SecurityMockUtils.setupOidcUserSecurityContext;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MyStuffControllerDriver implements MyStuffAcceptanceDriver {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final MockMvc mockMvc;

    public MyStuffControllerDriver(final WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Override
    public void assertExceptional(final Supplier<?> action) {
        assertThrows(AssertionError.class, action::get);
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

    @Override
    public String createAsset(final AssetOptions assetOptions) {
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
    public Void listUserAssets(final String authenticatedUser) {
        try {
            String response = mockMvc.perform(listOperation())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
            ListOfAssets listOfAssets = OBJECT_MAPPER.readValue(response, ListOfAssets.class);
            CURRENT_ASSETS.set(listOfAssets.assets());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void assertListOfAssetsContains(final String assetId) {
        assertTrue(CURRENT_ASSETS.get()
            .stream()
            .map(Asset.class::cast)
            .anyMatch(asset -> asset.id().equals(assetId)));
    }

}
