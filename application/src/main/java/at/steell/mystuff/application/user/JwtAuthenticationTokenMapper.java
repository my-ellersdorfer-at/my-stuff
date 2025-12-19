package at.steell.mystuff.application.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class JwtAuthenticationTokenMapper extends SimpleAuthTokenMapper {
    @Override
    public boolean supports(final Authentication authentication) {
        return authentication instanceof JwtAuthenticationToken;
    }
}
