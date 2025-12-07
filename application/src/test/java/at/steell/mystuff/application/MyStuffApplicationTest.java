package at.steell.mystuff.application;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MyStuffApplication.class)
class MyStuffApplicationTest {
    @Test
    void verifyMain() {
        assertDoesNotThrow(() -> MyStuffApplication.main(new String[]{}));
    }

    @Test
    void contextConfiguration() {
    }
}
