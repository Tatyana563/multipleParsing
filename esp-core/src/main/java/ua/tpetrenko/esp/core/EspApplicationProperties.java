package ua.tpetrenko.esp.core;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Positive;

/**
 * @author Roman Zdoronok
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "esp.app")
public class EspApplicationProperties {

    @Positive
    private int categoryPageSize=5000;
    @Positive
    private int parsersPoolSize=10;
    @Positive
    private int chunkSize=50;

}
