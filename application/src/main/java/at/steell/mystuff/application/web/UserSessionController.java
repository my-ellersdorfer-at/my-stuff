package at.steell.mystuff.application.web;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static org.springframework.security.config.Elements.ANONYMOUS;

@RequestMapping("/api/user-session")
@RestController
public class UserSessionController {
    public static final String ANONYMOUS_AVATAR = "--";
    private final List<UserDetailsMapper> listOfUserDetailsMappers = List.of(new DefaultOidcUserMapper(),
        new PreAuthenticatedAuthenticationTokenMapper(),
        new UsernamePasswordAuthenticationTokenMapper());

    public record UserSession(String userName, String firstName, String lastName, String avatar) {
    }

    @GetMapping("/current-user-session")
    public UserSession getCurrentUserSession() {
        return Optional.of(SecurityContextHolder.getContext())
            .map(SecurityContext::getAuthentication)
            .filter(Authentication::isAuthenticated)
            .map(this::mapAuthentication)
            .orElseGet(UserSessionController::anonymousUserSession);

    }

    private UserSession mapAuthentication(final Authentication authentication) {
        for (UserDetailsMapper userDetailsMapper : listOfUserDetailsMappers) {
            if (userDetailsMapper.supports(authentication)) {
                return userDetailsMapper.map(authentication);
            }
        }
        return anonymousUserSession();
    }

    private static UserSession anonymousUserSession() {
        return new UserSession(ANONYMOUS, "", "", ANONYMOUS_AVATAR);
    }

    private interface UserDetailsMapper {
        boolean supports(Authentication authentication);

        UserSession map(Authentication authentication);
    }

    private static final class DefaultOidcUserMapper implements UserDetailsMapper {
        @Override
        public boolean supports(final Authentication authentication) {
            return authentication != null && authentication.getPrincipal() instanceof DefaultOidcUser;
        }

        private static String buildAvatar(final String firstName, final String lastName) {
            if (firstName != null && !firstName.isEmpty() && lastName != null && !lastName.isEmpty()) {
                return firstName.substring(0, 1).toUpperCase() + lastName.substring(0, 1).toUpperCase();
            }
            return ANONYMOUS_AVATAR;
        }

        @Override
        public UserSession map(final Authentication authentication) {
            DefaultOidcUser defaultOidcUser = (DefaultOidcUser) authentication.getPrincipal();
            if (defaultOidcUser == null) {
                return anonymousUserSession();
            }
            String userName = defaultOidcUser.getSubject();
            return new UserSession(userName,
                defaultOidcUser.getGivenName(),
                defaultOidcUser.getFamilyName(),
                buildAvatar(defaultOidcUser.getGivenName(), defaultOidcUser.getFamilyName()));
        }
    }

    private abstract static class SimpleAuthTokenMapper implements UserDetailsMapper {
        @Override
        public UserSession map(final Authentication authentication) {
            String userName = authentication.getName();
            return new UserSession(userName, "", "", ANONYMOUS_AVATAR);
        }
    }

    private static final class PreAuthenticatedAuthenticationTokenMapper extends SimpleAuthTokenMapper {
        @Override
        public boolean supports(final Authentication authentication) {
            return authentication instanceof PreAuthenticatedAuthenticationToken;
        }
    }

    private static final class UsernamePasswordAuthenticationTokenMapper extends SimpleAuthTokenMapper {
        @Override
        public boolean supports(final Authentication authentication) {
            return authentication instanceof UsernamePasswordAuthenticationToken;
        }
    }

}
