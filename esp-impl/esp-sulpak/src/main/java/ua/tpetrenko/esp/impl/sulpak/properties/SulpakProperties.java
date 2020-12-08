package ua.tpetrenko.esp.impl.sulpak.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import ua.tpetrenko.esp.configuration.properties.GlobalProperties;
import ua.tpetrenko.esp.configuration.properties.model.ChromeProperties;
import ua.tpetrenko.esp.configuration.properties.model.ConnectionProperties;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "esp.sulpak")
public class SulpakProperties {
    private boolean enabled;
    //TODO ~
    private List<String> categoriesWhitelist;
    private List<String> categoriesBlacklist;
    @NestedConfigurationProperty
    private ConnectionProperties connection;
    @NestedConfigurationProperty
    private ChromeProperties chrome;

    public SulpakProperties(GlobalProperties globalProperties) {
        this.enabled = globalProperties.isEnabled();
        this.connection = globalProperties.getConnection();
        this.chrome = globalProperties.getChrome();
    }
}
