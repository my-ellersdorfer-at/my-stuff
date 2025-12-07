package at.steell.mystuff.application;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.servlet.context.ServletWebServerApplicationContext;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MyStuffApplication.class)
class MyStuffApplicationTest {
    @Autowired
    private ServletWebServerApplicationContext servletWebServerApplicationContext;

    @Test
    void verifyMain() {
        assertDoesNotThrow(() -> MyStuffApplication.main(new String[]{}));
    }

    @Test
    void contextConfiguration() {
        assertTrue(requireNonNull(servletWebServerApplicationContext.getWebServer()).getPort() > 0);
    }
}
