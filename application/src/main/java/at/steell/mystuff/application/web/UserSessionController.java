package at.steell.mystuff.application.web;

import at.steell.mystuff.application.user.UserSession;
import at.steell.mystuff.application.user.UserSessionFromSecurityContextHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/user-session")
@RestController
public class UserSessionController {

    @GetMapping("/current-user-session")
    public UserSession getCurrentUserSession() {
        return UserSessionFromSecurityContextHelper.fromSpringSecurityContext();
    }
}
