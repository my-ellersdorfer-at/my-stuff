package at.steell.mystuff.application.web;

import at.steell.mystuff.application.jwt.JwtBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static at.steell.mystuff.application.configuration.DomainConfiguration.ALL_KINDS_OF_ANONYMOUS;
import static at.steell.mystuff.application.user.UserSessionFromSecurityContextHelper.fromSpringSecurityContext;

@RequestMapping("/api/authentication")
@RestController
public class AuthenticationController {
    private final JwtBuilder jwtBuilder;
    private String issuerUrl;

    public AuthenticationController(
        final JwtBuilder thejJwtBuilder) {
        jwtBuilder = thejJwtBuilder;
    }

    @Value("${app.jwt.issuer}")
    public void setIssuerUrl(final String theIssuerUrl) {
        issuerUrl = theIssuerUrl;
    }

    @GetMapping(path = "/token", produces = "text/plain")
    String token() {
        String userName = fromSpringSecurityContext().userName();
        if (ALL_KINDS_OF_ANONYMOUS.contains(userName)) {
            throw new AnonymousUserTokenRequest();
        }
        return jwtBuilder.token(userName, issuerUrl);
    }

    public static class AnonymousUserTokenRequest extends RuntimeException {
        public AnonymousUserTokenRequest() {
            super("Anonymous users are not allowed to request a token.");
        }
    }
}
