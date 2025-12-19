package at.steell.mystuff.application.web;

import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/.well-known")
public class JwksController {

    private final JWKSet publicJwkSet;

    public JwksController(final JWKSet jwkSet) {
        this.publicJwkSet = jwkSet;
    }

    @GetMapping(path = "/jwks.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> jwks() {
        Map<String, Object> body = publicJwkSet.toJSONObject();
        return ResponseEntity
            .ok()
            .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic())
            .body(body);
    }
}
