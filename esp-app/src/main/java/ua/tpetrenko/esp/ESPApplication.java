package ua.tpetrenko.esp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author Roman Zdoronok
 */

@SpringBootApplication
@EnableConfigurationProperties
public class ESPApplication {

    public static void main(String[] args) {
        SpringApplication.run(ESPApplication.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
        //TODO: create custom jacksonMapperHttpConverter to handle response header Content-type: text/html

    }
}
