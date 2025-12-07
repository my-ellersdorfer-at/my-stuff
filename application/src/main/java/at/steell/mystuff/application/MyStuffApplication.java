package at.steell.mystuff.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SuppressWarnings("checkstyle:FinalClass")
@SpringBootApplication
public class MyStuffApplication {
    private MyStuffApplication() {
    }

    static void main(final String[] args) {
        SpringApplication.run(MyStuffApplication.class, args);
    }

}
