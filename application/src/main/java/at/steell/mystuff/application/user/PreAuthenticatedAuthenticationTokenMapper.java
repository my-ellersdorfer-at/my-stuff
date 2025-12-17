package at.steell.mystuff.application.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public final class PreAuthenticatedAuthenticationTokenMapper extends SimpleAuthTokenMapper {
    @Override
    public boolean supports(final Authentication authentication) {
        return authentication instanceof PreAuthenticatedAuthenticationToken;
    }
}
