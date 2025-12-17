package at.steell.mystuff.acceptance.scenario;

import at.steell.mystuff.acceptance.OauthTestSupport;
import at.steell.mystuff.acceptance.driver.MyStuffControllerDriver;
import at.steell.mystuff.application.MyStuffApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MyStuffApplication.class)
public class AssetControllerAcceptanceTest extends AbstractAssetAcceptanceTest implements OauthTestSupport {
    @Autowired
    public AssetControllerAcceptanceTest(final WebApplicationContext webApplicationContext) {
        super(new MyStuffControllerDriver(webApplicationContext));
    }
}
