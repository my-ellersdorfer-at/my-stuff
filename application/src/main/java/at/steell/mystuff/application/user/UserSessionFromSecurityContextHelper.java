package at.steell.mystuff.application.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static at.steell.mystuff.application.user.UserSession.anonymousUserSession;

public final class UserSessionFromSecurityContextHelper {
    private static final List<UserDetailsMapper> USER_DETAILS_MAPPERS = List.of(new DefaultOidcUserMapper(),
        new PreAuthenticatedAuthenticationTokenMapper(),
        new UsernamePasswordAuthenticationTokenMapper(),
        new JwtAuthenticationTokenMapper());

    private UserSessionFromSecurityContextHelper() {
    }

    public static UserSession fromSpringSecurityContext() {
        return Optional.of(SecurityContextHolder.getContext())
            .map(SecurityContext::getAuthentication)
            .filter(Authentication::isAuthenticated)
            .map(UserSessionFromSecurityContextHelper::mapAuthentication)
            .orElseGet(UserSession::anonymousUserSession);
    }

    private static UserSession mapAuthentication(final Authentication authentication) {
        for (UserDetailsMapper userDetailsMapper : USER_DETAILS_MAPPERS) {
            if (userDetailsMapper.supports(authentication)) {
                return userDetailsMapper.map(authentication);
            }
        }
        return anonymousUserSession();
    }
}
