package at.steell.mystuff.application.user;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public final class UsernamePasswordAuthenticationTokenMapper extends SimpleAuthTokenMapper {
    @Override
    public boolean supports(final Authentication authentication) {
        return authentication instanceof UsernamePasswordAuthenticationToken;
    }
}
