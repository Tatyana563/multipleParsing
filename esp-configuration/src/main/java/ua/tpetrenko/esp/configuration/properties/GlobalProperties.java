package ua.tpetrenko.esp.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import ua.tpetrenko.esp.configuration.properties.model.ChromeProperties;
import ua.tpetrenko.esp.configuration.properties.model.ConnectionProperties;

/**
 * @author Roman Zdoronok
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "esp.global")
public class GlobalProperties {

    @NestedConfigurationProperty
    private ConnectionProperties connection = new ConnectionProperties();
    @NestedConfigurationProperty
    private ChromeProperties chrome = new ChromeProperties();
    private boolean enabled;

}
