package at.steell.mystuff.acceptance;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

public interface OauthTestSupport {
    String KEYCLOAK_REALM_CONFIGURATION = "keycloak/my-stuff.realm.json";

    KeycloakContainer KEYCLOAK = startKeycloak();

    private static KeycloakContainer startKeycloak() {
        KeycloakContainer container = new KeycloakContainer()
            .withRealmImportFile(KEYCLOAK_REALM_CONFIGURATION);
        container.start();
        return container;
    }

    @DynamicPropertySource
    static void setOidc(final DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.client.provider.my-stuff.issuer-uri",
            () -> KEYCLOAK.getAuthServerUrl() + "/realms/my-stuff");
    }

}
