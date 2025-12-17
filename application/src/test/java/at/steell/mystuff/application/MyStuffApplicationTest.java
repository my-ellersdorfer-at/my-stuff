package at.steell.mystuff.application;


import at.steell.mystuff.acceptance.OauthTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MyStuffApplication.class)
class MyStuffApplicationTest implements OauthTestSupport {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    void contextConfiguration() {
        assertNotNull(webApplicationContext.getApplicationName());
    }
}
