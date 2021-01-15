package ua.tpetrenko.esp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

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

        //TODO: create custom jacksonMapperHttpConverter to handle response header Content-type: text/html
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.TEXT_HTML, MediaType.APPLICATION_JSON));
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, converter);
        return restTemplate;
    }
}
