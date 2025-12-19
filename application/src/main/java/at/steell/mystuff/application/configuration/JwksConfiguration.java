package at.steell.mystuff.application.configuration;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.context.annotation.Bean;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

public class JwksConfiguration {
    private static final String ALGORITHM = "RSA";
    private static final int KEY_SIZE = 4096;
    private final RSAKey rsaJwk;
    private final JWKSet publicJwkSet;

    public JwksConfiguration() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
        kpg.initialize(KEY_SIZE);
        KeyPair keyPair = kpg.generateKeyPair();

        this.rsaJwk = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
            .privateKey(keyPair.getPrivate())
            .keyUse(KeyUse.SIGNATURE)
            .algorithm(JWSAlgorithm.RS256)
            .keyID(UUID.randomUUID().toString())
            .build();

        this.publicJwkSet = new JWKSet(rsaJwk.toPublicJWK());
    }

    @Bean
    public RSAKey signingRsaJwk() {
        return rsaJwk;
    }

    @Bean
    public JWKSet publicJwkSet() {
        return publicJwkSet;
    }
}
