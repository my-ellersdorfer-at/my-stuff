package at.steell.mystuff.application.configuration;

import at.steell.mystuff.domain.interactor.AssetInteractor;
import at.steell.mystuff.domain.interactor.AssetInteractor.AssetInteractorFactory;
import at.steell.mystuff.domain.store.InMemoryAssetStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

import static org.springframework.security.config.Elements.ANONYMOUS;

@Configuration
public class DomainConfiguration {
    @Bean
    public AssetInteractor assetInteractor() {
        return new AssetInteractorFactory()
            .withAssetStore(new InMemoryAssetStore())
            .withAllKindsOfAnonymous(Set.of(ANONYMOUS))
            .create();
    }
}
