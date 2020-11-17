package ua.tpetrenko.esp.impl.fora.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import ua.tpetrenko.esp.configuration.properties.GlobalProperties;
import ua.tpetrenko.esp.configuration.properties.model.ConnectionProperties;

import java.util.List;

/**
 * @author Roman Zdoronok
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "esp.fora")
public class ForaProperties {
    private boolean enabled;
    private List<String> categoriesWhitelist;
    @NestedConfigurationProperty
    private ConnectionProperties connection;

    public ForaProperties(GlobalProperties globalProperties) {
        this.connection = globalProperties.getConnection();
//        this.enabled = globalProperties.isEnabled();
    }

}
