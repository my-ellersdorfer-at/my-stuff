package at.steell.mystuff.application.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import static at.steell.mystuff.application.user.UserSession.ANONYMOUS_AVATAR;

public final class DefaultOidcUserMapper implements UserDetailsMapper {
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
            return UserSession.anonymousUserSession();
        }
        String userName = defaultOidcUser.getSubject();
        return new UserSession(userName,
            defaultOidcUser.getGivenName(),
            defaultOidcUser.getFamilyName(),
            buildAvatar(defaultOidcUser.getGivenName(), defaultOidcUser.getFamilyName()));
    }
}
