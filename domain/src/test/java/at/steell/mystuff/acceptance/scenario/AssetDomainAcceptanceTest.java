package at.steell.mystuff.acceptance.scenario;

import at.steell.mystuff.acceptance.driver.DomainDriver;

class AssetDomainAcceptanceTest extends AbstractAssetAcceptanceTest {
    AssetDomainAcceptanceTest() {
        super(new DomainDriver());
    }
}
