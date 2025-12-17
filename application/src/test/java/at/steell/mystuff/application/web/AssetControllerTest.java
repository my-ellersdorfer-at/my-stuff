package at.steell.mystuff.application.web;

import at.steell.mystuff.application.configuration.DomainConfiguration;
import at.steell.mystuff.application.configuration.WebMvcConfig;
import at.steell.mystuff.application.web.AssetController.ListOfAssets;
import at.steell.mystuff.domain.entity.Asset;
import at.steell.mystuff.domain.interactor.AssetInteractor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.UUID;

import static at.steell.mystuff.application.utils.AssetControllerSupport.createOperation;
import static at.steell.mystuff.application.utils.AssetControllerSupport.findOperation;
import static at.steell.mystuff.application.utils.AssetControllerSupport.listOperation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableWebMvc
@SpringJUnitWebConfig(classes = AssetControllerTest.ContextConfiguration.class)
class AssetControllerTest {
    private static final String MOCK_USER = "MockUser";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    protected WebApplicationContext webApplicationContext;
    protected MockMvc mockMvc;

    @Configuration
    @Import({WebMvcConfig.class, DomainConfiguration.class})
    public static class ContextConfiguration {
        @Bean
        public AssetController assetController(final AssetInteractor assetInteractor) {
            return new AssetController(assetInteractor);
        }
    }

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void find_unauthenticated_forbidden() throws Exception {
        mockMvc.perform(findOperation(UUID.randomUUID().toString()))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void find_anonymous_forbidden() throws Exception {
        mockMvc.perform(findOperation(UUID.randomUUID().toString()))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(MOCK_USER)
    void find_withoutCreation_notFound() throws Exception {
        mockMvc.perform(findOperation(UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    @Test
    void list_unauthenticated_forbidden() throws Exception {
        mockMvc.perform(listOperation())
            .andExpect(status().isForbidden())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithAnonymousUser
    void list_anonymous_forbidden() throws Exception {
        mockMvc.perform(listOperation())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(MOCK_USER)
    void list_authenticated() throws Exception {
        mockMvc.perform(listOperation())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk());
    }

    @Test
    void create_unauthenticated_forbidden() throws Exception {
        mockMvc.perform(createOperation())
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithAnonymousUser
    void create_anonymous_forbidden() throws Exception {
        mockMvc.perform(createOperation())
            .andExpect(status().isUnauthorized());
    }

    private String createAsset() throws Exception {
        MvcResult response = mockMvc.perform(createOperation())
            .andExpect(status().isOk())
            .andReturn();
        return response.getResponse().getContentAsString();
    }

    @Test
    @WithMockUser(MOCK_USER)
    void create_authenticated() throws Exception {
        assertNotNull(createAsset());
    }

    @Test
    @WithMockUser(MOCK_USER)
    void authenticated_find_created() throws Exception {
        String assetId = createAsset();
        MvcResult response = mockMvc.perform(findOperation(assetId))
            .andExpect(status().isOk())
            .andReturn();
        Asset asset = OBJECT_MAPPER.readValue(
            response.getResponse().getContentAsString(),
            Asset.class);
        assertNotNull(asset);
        assertEquals(assetId, asset.id());
    }

    @Test
    @WithMockUser(MOCK_USER)
    void authenticated_list_created() throws Exception {
        String assetId = createAsset();
        MvcResult response = mockMvc.perform(listOperation())
            .andExpect(status().isOk())
            .andReturn();
        ListOfAssets listOfAssets = OBJECT_MAPPER.readValue(
            response.getResponse().getContentAsString(),
            ListOfAssets.class);
        assertNotNull(listOfAssets);
        assertTrue(listOfAssets.assets().stream().anyMatch(asset -> asset.id().equals(assetId)));
    }
}
