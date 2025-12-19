package at.steell.mystuff.acceptance.scenario;

import at.steell.mystuff.acceptance.OauthTestSupport;
import at.steell.mystuff.acceptance.driver.MyStuffUiDriver;
import at.steell.mystuff.acceptance.driver.ServerUrl;
import at.steell.mystuff.application.MyStuffApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.servlet.context.ServletWebServerApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("acceptanceTest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MyStuffApplication.class)
class AssetUiAcceptanceTest extends AbstractAssetAcceptanceTest implements OauthTestSupport {
    private final ServletWebServerApplicationContext servletWebServerApplicationContext;
    private final WebApplicationContext webApplicationContext;


    @Autowired
    AssetUiAcceptanceTest(
        final ServletWebServerApplicationContext testServletWebServerApplicationContext,
        final WebApplicationContext testWebApplicationContext) {
        super(new MyStuffUiDriver());
        servletWebServerApplicationContext = testServletWebServerApplicationContext;
        webApplicationContext = testWebApplicationContext;
    }

    @BeforeEach
    void setupPlaywright() {
        getUiDriver().setupPlaywright();
        getUiDriver().startContext();
        setupServerPortAndContextPath();
    }

    private MyStuffUiDriver getUiDriver() {
        return (MyStuffUiDriver) getDriver();
    }

    private String testArchiveRelativePath(final TestInfo testInfo) {
        return "target/" + testInfo.getTestMethod().orElseThrow().getName() + ".zip";
    }

    @AfterEach
    void stopTracing(final TestInfo testInfo) {
        getUiDriver().stopContext(testArchiveRelativePath(testInfo));
        getUiDriver().closePlaywright();
    }

    private void setupServerPortAndContextPath() {
        int serverPort = servletWebServerApplicationContext.getWebServer().getPort();
        String contextPath = webApplicationContext.getEnvironment()
            .getProperty("server.servlet.context-path", "");
        getUiDriver().setServerUrl(new ServerUrl(serverPort, contextPath));
    }

    private void unreachable() {
        throw new UnsupportedOperationException("This is not reachable in the user interface");
    }

    @Override
    @Test
    void listAssetsDifferentAuthentication_emptyResults() {
        assertThrows(UnsupportedOperationException.class, this::unreachable);
    }
}
