package at.steell.mystuff.application.user;

import org.springframework.security.core.Authentication;

import static at.steell.mystuff.application.user.UserSession.ANONYMOUS_AVATAR;

public abstract class SimpleAuthTokenMapper implements UserDetailsMapper {
    @Override
    public UserSession map(final Authentication authentication) {
        String userName = authentication.getName();
        return new UserSession(userName, "", "", ANONYMOUS_AVATAR);
    }
}
