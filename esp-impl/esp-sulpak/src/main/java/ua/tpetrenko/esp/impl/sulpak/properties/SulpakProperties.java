package ua.tpetrenko.esp.impl.sulpak.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import ua.tpetrenko.esp.configuration.properties.GlobalProperties;
import ua.tpetrenko.esp.configuration.properties.model.ConnectionProperties;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "esp.sulpak")
public class SulpakProperties {
    private boolean enabled;
    private String[] categoriesWhitelist;
    @NestedConfigurationProperty
    private ConnectionProperties connection;

    public SulpakProperties(GlobalProperties globalProperties) {
        this.connection = globalProperties.getConnection();
//        this.enabled = globalProperties.isEnabled();
    }
}
