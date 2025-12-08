package at.steell.mystuff.application;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MyStuffApplication.class)
class MyStuffApplicationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    void verifyMain() {
        assertDoesNotThrow(() -> MyStuffApplication.main(new String[]{}));
    }

    @Test
    void contextConfiguration() {
        assertNotNull(webApplicationContext.getApplicationName());
    }
}
