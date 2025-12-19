package at.steell.mystuff.application.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@Import({
    OauthSecurityChainConfiguration.class,
    AssetApiChainConfiguration.class,
    JwksConfiguration.class,
    JwtConfiguration.class})
public class SecurityConfiguration {
    static final int ASSET_API_CHAIN_ORDER = 1;
    static final int OAUTH_SECURITY_CHAIN_ORDER = 2;
}
