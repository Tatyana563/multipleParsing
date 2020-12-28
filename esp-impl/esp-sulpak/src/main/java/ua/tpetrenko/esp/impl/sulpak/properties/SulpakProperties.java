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

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "esp.sulpak")
public class SulpakProperties extends ParserProperties {
    @NestedConfigurationProperty
    private ChromeProperties chrome;

    public SulpakProperties(GlobalProperties globalProperties) {
        //TODO: create connection properties copy
        this.connection = new ConnectionProperties(globalProperties.getConnection().getReadTimeoutMs(), globalProperties.getConnection().getRetryCount());
        //TODO: create crome properties copy
        this.chrome = new ChromeProperties(globalProperties.getChrome());
    }
}
