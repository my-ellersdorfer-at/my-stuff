package at.steell.mystuff.application.configuration;

import at.steell.mystuff.application.jwt.JwtBuilder;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

public class JwtConfiguration {

    @Bean
    public JwtDecoder jwtDecoder(final JWKSet publicJwkSet) throws JOSEException {
        RSAKey rsa = (RSAKey) publicJwkSet.getKeys().getFirst();
        return NimbusJwtDecoder
            .withPublicKey(rsa.toRSAPublicKey())
            .build();
    }

    @Bean
    public JwtEncoder jwtEncoder(final RSAKey signingRsaJwk) {
        JWKSet jwkSet = new JWKSet(signingRsaJwk);
        JWKSource<SecurityContext> jwkSource =
            (selector, context) -> selector.select(jwkSet);

        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtBuilder bearerTokenBuilder(final JwtEncoder jwtEncoder) {
        return new JwtBuilder(jwtEncoder);
    }
}
