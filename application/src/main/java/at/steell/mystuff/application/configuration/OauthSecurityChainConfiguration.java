package at.steell.mystuff.application.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.csrf.CookieCsrfTokenRepository.withHttpOnlyFalse;

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

    private static final class CustomerRequestMatcher implements RequestMatcher {
        private static final Set<String> ROUTES = new HashSet<>();

        static {
            ROUTES.add("me");
            ROUTES.add("assets");
        }

        public static Set<String> getPathComponents(final HttpServletRequest request) {
            return Set.of(request.getServletPath().split("/"));
        }

        @Override
        public boolean matches(final HttpServletRequest request) {
            Set<String> pathComponents = getPathComponents(request);
            for (final String route :ROUTES) {
                if (pathComponents.contains(route)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Bean
    SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler csrfRequestHandler = new CsrfTokenRequestAttributeHandler();
        csrfRequestHandler.setCsrfRequestAttributeName(null);

        return http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(new CustomerRequestMatcher()).authenticated()
                .anyRequest().permitAll())
            .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer
                .contentSecurityPolicy(cspConfig -> cspConfig
                    .policyDirectives(contentSecurityPolicyDirective())
                    .reportOnly()))
            .csrf(csrf -> csrf
                .csrfTokenRequestHandler(csrfRequestHandler)
                .csrfTokenRepository(withHttpOnlyFalse()))
            .oauth2Login(withDefaults())
            .build();
    }
}
