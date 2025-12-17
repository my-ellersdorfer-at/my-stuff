package at.steell.mystuff.acceptance;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public interface OauthTestSupport {
    String KEYCLOAK_REALM_CONFIGURATION = "keycloak/my-stuff.realm.json";

    @Container
    KeycloakContainer KEYCLOAK = new KeycloakContainer()
        .withRealmImportFile(KEYCLOAK_REALM_CONFIGURATION);

    @DynamicPropertySource
    static void setOidc(final DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.client.provider.my-stuff.issuer-uri",
            () -> KEYCLOAK.getAuthServerUrl() +  "/realms/my-stuff");
    }

}
