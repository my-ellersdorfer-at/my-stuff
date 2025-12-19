package at.steell.mystuff.application.jwt;

import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;

public class JwtBuilder {
    private final JwtEncoder jwtEncoder;

    public JwtBuilder(final JwtEncoder theJwtEncoder) {
        jwtEncoder = theJwtEncoder;
    }

    private JwsHeader jwsHeader() {
        return JwsHeader.with(SignatureAlgorithm.RS256).build();
    }

    private String buildToken(final String subject, final String issuer) {
        Instant now = Instant.now();
        final long expiresInSeconds = 3600;

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer(issuer)
            .subject(subject)
            .issuedAt(now)
            .expiresAt(now.plusSeconds(expiresInSeconds))
            .build();

        return jwtEncoder
            .encode(JwtEncoderParameters.from(jwsHeader(), claims))
            .getTokenValue();
    }

    public String token(final String subject, final String issuer) {
        if (subject == null || subject.isBlank()) {
            throw new NullPointerException("subject is null");
        }
        if (issuer == null || issuer.isBlank()) {
            throw new NullPointerException("issuer is null");
        }
        return buildToken(subject, issuer);
    }
}
