package ua.tpetrenko.esp.app.configuration.properties;

import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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

}
