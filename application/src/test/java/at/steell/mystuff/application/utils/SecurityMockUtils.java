package at.steell.mystuff.application.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

public final class SecurityMockUtils {
    public static final String FAMILY_NAME = "User";
    public static final String GIVEN_NAME = "Mock";

    private static final int OIDC_EXPIRATION_SECONDS = 10;

    private SecurityMockUtils() {
    }

    public static void setupSecurityContext(final String userName) {
        Authentication authentication = new PreAuthenticatedAuthenticationToken(
            userName, null);
        authentication.setAuthenticated(true);
        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
    }

    public static void setupOidcUserSecurityContext(final String userName) {
        Instant expiration = Instant.now().plusSeconds(OIDC_EXPIRATION_SECONDS);
        OidcIdToken idToken = new OidcIdToken("tokenValue",
            Instant.now(),
            expiration,
            Map.of("sub", userName,
                "exp", expiration.getEpochSecond(),
                "given_name", SecurityMockUtils.GIVEN_NAME,
                "family_name", SecurityMockUtils.FAMILY_NAME));
        OAuth2AuthenticationToken oauthToken = new OAuth2AuthenticationToken(
            new DefaultOidcUser(Set.of(), idToken),
            Set.of(),
            "authorizedClientRegistrationId");
        SecurityContext
            securityContext = new SecurityContextImpl(oauthToken);
        SecurityContextHolder.setContext(securityContext);
    }
}
