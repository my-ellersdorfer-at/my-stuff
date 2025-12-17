package at.steell.mystuff.application.user;

import org.springframework.security.core.Authentication;

public interface UserDetailsMapper {
    boolean supports(Authentication authentication);

    UserSession map(Authentication authentication);
}
