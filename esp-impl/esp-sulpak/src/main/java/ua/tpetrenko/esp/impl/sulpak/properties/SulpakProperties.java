package ua.tpetrenko.esp.impl.sulpak.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import ua.tpetrenko.esp.configuration.properties.GlobalProperties;
import ua.tpetrenko.esp.configuration.properties.model.ChromeProperties;
import ua.tpetrenko.esp.configuration.properties.model.ConnectionProperties;
import ua.tpetrenko.esp.configuration.properties.model.ParserProperties;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "esp.sulpak")
public class SulpakProperties extends ParserProperties {
    //TODO ~
    private List<String> categoriesBlacklist;
    @NestedConfigurationProperty
    private ConnectionProperties connection;
    @NestedConfigurationProperty
    private ChromeProperties chrome;

    public SulpakProperties(GlobalProperties globalProperties) {
        this.connection = globalProperties.getConnection();
        this.chrome = globalProperties.getChrome();
    }
}
