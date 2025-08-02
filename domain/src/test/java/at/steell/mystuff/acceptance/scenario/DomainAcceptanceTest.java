package at.steell.mystuff.acceptance.scenario;

import at.steell.mystuff.acceptance.driver.DomainDriver;

class DomainAcceptanceTest extends AbstractMyStuffAcceptanceTest {
    DomainAcceptanceTest() {
        super(new DomainDriver());
    }
}
