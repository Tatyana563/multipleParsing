package ua.tpetrenko.esp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Roman Zdoronok
 */

@SpringBootApplication
@EnableConfigurationProperties
public class ESPApplication {

    public static void main(String[] args) {
        SpringApplication.run(ESPApplication.class, args);
    }
}
