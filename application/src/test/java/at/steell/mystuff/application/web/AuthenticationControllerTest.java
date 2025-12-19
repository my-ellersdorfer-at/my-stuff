package at.steell.mystuff.application.web;

import at.steell.mystuff.application.configuration.AssetApiChainConfiguration;
import at.steell.mystuff.application.configuration.JwksConfiguration;
import at.steell.mystuff.application.configuration.JwtConfiguration;
import at.steell.mystuff.application.configuration.WebMvcConfig;
import at.steell.mystuff.application.jwt.JwtBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.UUID;

import static at.steell.mystuff.application.user.UserSessionFromSecurityContextHelper.fromSpringSecurityContext;
import static at.steell.mystuff.application.utils.SecurityMockUtils.setupSecurityContext;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableWebSecurity
@EnableWebMvc
@SpringJUnitWebConfig(classes = AuthenticationControllerTest.ContextConfiguration.class)
class AuthenticationControllerTest {
    private static final String ISSUER_URL = "https://your-service-id." + UUID.randomUUID();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JwtDecoder jwtDecoder;

    private MockMvc mockMvc;

    @DynamicPropertySource
    static void setProperties(final DynamicPropertyRegistry registry) {
        registry.add("app.jwt.issuer", () -> ISSUER_URL);
    }

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Configuration
    @Import({WebMvcConfig.class, JwksConfiguration.class, JwtConfiguration.class,
        AssetApiChainConfiguration.class})
    public static class ContextConfiguration {
        @Bean
        public AuthenticationController authenticationController(
            final JwtBuilder jwtBuilder) {
            AuthenticationController authenticationController = new AuthenticationController(jwtBuilder);
            authenticationController.setIssuerUrl(ISSUER_URL);
            return authenticationController;
        }

        @Bean
        public AuthenticationControllerTest.SecuredController securedController() {
            return new AuthenticationControllerTest.SecuredController();
        }
    }

    @RequestMapping("/api/assets")
    @RestController
    public static class SecuredController {
        @GetMapping("/test-auth/{user}")
        public void needsAuthentication(@PathVariable("user") final String expectedUser) {
            assertEquals(fromSpringSecurityContext().userName(), expectedUser);
        }
    }

    private ResultActions tokenRequest() throws Exception {
        return mockMvc.perform(get("/api/authentication/token"));
    }

    private String tokenResponse() throws Exception {
        final MvcResult response = tokenRequest()
                .andExpect(status().isOk())
                .andReturn();
        return response.getResponse().getContentAsString();
    }

    private Jwt decodeToken(final String tokenResponse) {
        return jwtDecoder.decode(tokenResponse);
    }

    @Test
    void noTokenForAnonymous_notAllowed() throws Exception {
        SecurityContextHolder.clearContext();
        tokenRequest().andExpect(status().isForbidden());
    }

    @Test
    void tokenForAuthenticated() throws Exception {
        setupSecurityContext("authenticatedUser");
        final String tokenResponse = tokenResponse();
        Jwt jwt = decodeToken(tokenResponse);
        assertEquals(ISSUER_URL, jwt.getIssuer().toString());
    }

    @Test
    void authenticated_isIdentified() throws Exception {
        setupSecurityContext("authenticatedUser");
        String tokenResponse = tokenResponse();
        Jwt jwt = decodeToken(tokenResponse);
        mockMvc.perform(get("/api/assets/test-auth/" + jwt.getSubject())
                .param(AUTHORIZATION, "Bearer " + tokenResponse)
            )
            .andExpect(status().isOk());
    }
}
