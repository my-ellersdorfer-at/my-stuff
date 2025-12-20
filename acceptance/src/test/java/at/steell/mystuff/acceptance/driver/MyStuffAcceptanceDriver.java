package at.steell.mystuff.acceptance.driver;

import at.steell.mystuff.acceptance.dsl.MyStuffAcceptanceDsl;

import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public interface MyStuffAcceptanceDriver extends MyStuffAcceptanceDsl {
    ThreadLocal<Collection<String>> CURRENT_ASSET_IDS = ThreadLocal.withInitial(Set::of);

    @Override
    default void assertListOfAssetsIsEmpty() {
        assertTrue(CURRENT_ASSET_IDS.get().isEmpty());
    }

    default String userA() {
        return "User A";
    }

    default String userB() {
        return "User B";
    }
}
