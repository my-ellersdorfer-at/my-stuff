package at.steell.mystuff.application.configuration;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

public class JwtConfiguration {


    @Bean
    public JwtDecoder jwtDecoder(final JWKSet publicJwkSet) throws JOSEException {
        RSAKey rsa = (RSAKey) publicJwkSet.getKeys().getFirst();
        return NimbusJwtDecoder
            .withPublicKey(rsa.toRSAPublicKey())
            .build();
    }
}
