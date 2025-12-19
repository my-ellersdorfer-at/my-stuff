package at.steell.mystuff.application.jwt;

import at.steell.mystuff.application.configuration.JwksConfiguration;
import at.steell.mystuff.application.configuration.JwtConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitWebConfig(classes = {JwksConfiguration.class, JwtConfiguration.class})
class JwtBuilderTest {

    @Autowired
    private JwtBuilder builder;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Test
    void buildToken_code_null_exceptional() {
        assertThrows(NullPointerException.class, () -> builder.token(null, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void buildToken_code_blank_exceptional(final String subject) {
        assertThrows(NullPointerException.class, () -> builder.token(subject, null));
    }

    @Test
    void buildToken_issuer_null_exceptional() {
        assertThrows(NullPointerException.class, () -> builder.token("subject", null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void buildToken_issuer_blank_exceptional(final String issuer) {
        assertThrows(NullPointerException.class, () -> builder.token("subject", issuer));
    }

    @Test
    void buildToken() {
        final String subject = UUID.randomUUID().toString();
        final String issuer = "https://your-service-id";
        final String token = builder.token(subject, issuer);

        assertNotNull(token);
        assertEquals(subject, jwtDecoder.decode(token).getSubject());
        assertEquals(issuer, jwtDecoder.decode(token).getIssuer().toExternalForm());
    }
}
