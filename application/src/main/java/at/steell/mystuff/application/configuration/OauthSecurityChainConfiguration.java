package at.steell.mystuff.application.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

public class OauthSecurityChainConfiguration {

    private String contentSecurityPolicyDirective() {
        return "default-src 'self'; "
            + "style-src 'self' 'unsafe-inline'; "
            + "script-src 'self' 'unsafe-inline'; "
            + "img-src 'self' 'unsafe-inline'; "
            + "font-src 'self' data:; "
            + "worker-src 'self' blob: data:; "
            + "connect-src 'self' https: wss:; "
            + "object-src 'none'; "
            + "child-src 'none'; "
            + "form-action 'none'; "
            + "frame-ancestors 'none';";
    }

    @Bean
    SecurityFilterChain securityFilterChain(final HttpSecurity http) {
        return http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll())
            .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer
                .contentSecurityPolicy(cspConfig -> cspConfig
                    .policyDirectives(contentSecurityPolicyDirective())
                    .reportOnly()))
            .oauth2Login(withDefaults())
            .build();
    }
}
