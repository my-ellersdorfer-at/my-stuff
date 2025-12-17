package at.steell.mystuff.acceptance.driver;

import at.steell.mystuff.acceptance.dsl.MyStuffAcceptanceDsl;

import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public interface MyStuffAcceptanceDriver extends MyStuffAcceptanceDsl {
    ThreadLocal<Collection<?>> CURRENT_ASSETS = ThreadLocal.withInitial(Set::of);

    default void assertListOfAssetsIsEmpty() {
        assertTrue(CURRENT_ASSETS.get().isEmpty());
    }
}
