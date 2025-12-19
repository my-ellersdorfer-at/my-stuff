package at.steell.mystuff.application.web;

import at.steell.mystuff.application.configuration.JwksConfiguration;
import at.steell.mystuff.application.configuration.WebMvcConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWKSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@EnableWebMvc
@SpringJUnitWebConfig(classes = JwksControllerTest.ContextConfiguration.class)
class JwksControllerTest {

    @Autowired
    private JWKSet jwkSet;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Configuration
    @Import({WebMvcConfig.class, JwksConfiguration.class})
    public static class ContextConfiguration {
        @Bean
        public JwksController jwksController(final JWKSet jwkSet) {
            return new JwksController(jwkSet);
        }
    }

    @BeforeEach
    void setupMvc() {
        if (mockMvc == null) {
            mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        }
    }

    @Test
    void jwks() throws Exception {
        MvcResult result =
            this.mockMvc.perform(get("/.well-known/jwks.json"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        assertNotNull(result);
        assertEquals(objectMapper.writeValueAsString(jwkSet.toJSONObject()),
            result.getResponse().getContentAsString());
    }
}
