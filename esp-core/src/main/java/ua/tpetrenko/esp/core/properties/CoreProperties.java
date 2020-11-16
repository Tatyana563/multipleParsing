package ua.tpetrenko.esp.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.Positive;

@Configuration
@ConfigurationProperties(prefix = "esp.core")
@Getter
@Setter
public class CoreProperties {
    @Positive
    private int categoryPageSize=50;
}
