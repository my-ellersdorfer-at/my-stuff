package at.steell.mystuff.application.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static at.steell.mystuff.application.configuration.SecurityConfiguration.ASSET_API_CHAIN_ORDER;

public class AssetApiChainConfiguration {

    @SuppressWarnings("java:S4502")
    @Bean
    @Order(ASSET_API_CHAIN_ORDER)
    SecurityFilterChain assetApiChain(final HttpSecurity http) {
        return http
            .securityMatcher(new PathMatchingRequestMatcher("/api/assets"))
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
