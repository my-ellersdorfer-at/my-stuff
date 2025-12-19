package at.steell.mystuff.application.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
public class AssetApiChainConfiguration {
    private static final class AssetsApiRequestMatcher implements RequestMatcher {
        @Override
        public boolean matches(final HttpServletRequest request) {
            return request.getServletPath().contains("/api/assets");
        }
    }

    @SuppressWarnings("java:S4502")
    @Bean
    @Order(1)
    SecurityFilterChain assetApiChain(final HttpSecurity http) throws Exception {
        return http
            .securityMatcher(new AssetsApiRequestMatcher())
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(sessionManagementConfigurer ->
                sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(resourceServerConfigurer ->
                resourceServerConfigurer.jwt(Customizer.withDefaults()))
            .build();
    }
}
