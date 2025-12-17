package at.steell.mystuff.application.user;

import static org.springframework.security.config.Elements.ANONYMOUS;

public record UserSession(String userName, String firstName, String lastName, String avatar) {
    public static final String ANONYMOUS_AVATAR = "--";

    public static UserSession anonymousUserSession() {
        return new UserSession(ANONYMOUS, "", "", ANONYMOUS_AVATAR);
    }
}
