package at.steell.mystuff.application.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.VersionResourceResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private static final String ALL_PATHS = "/**";
    private static final String STATIC_RESOURCES_LOCATION = "classpath:/static/";

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler(ALL_PATHS)
            .addResourceLocations(STATIC_RESOURCES_LOCATION)
            .resourceChain(true)
            .addResolver(new VersionResourceResolver()
                .addContentVersionStrategy(ALL_PATHS));
    }
}
