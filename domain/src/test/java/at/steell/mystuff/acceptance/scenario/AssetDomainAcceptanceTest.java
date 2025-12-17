package at.steell.mystuff.acceptance.scenario;

import at.steell.mystuff.acceptance.driver.MyStuffDomainDriver;

class AssetDomainAcceptanceTest extends AbstractAssetAcceptanceTest {
    AssetDomainAcceptanceTest() {
        super(new MyStuffDomainDriver());
    }
}
