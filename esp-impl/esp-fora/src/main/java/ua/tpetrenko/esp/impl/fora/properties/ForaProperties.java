package ua.tpetrenko.esp.impl.fora.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import ua.tpetrenko.esp.configuration.properties.GlobalProperties;
import ua.tpetrenko.esp.configuration.properties.model.ConnectionProperties;
import ua.tpetrenko.esp.configuration.properties.model.ParserProperties;

/**
 * @author Roman Zdoronok
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "esp.fora")
public class ForaProperties extends ParserProperties {

    @NestedConfigurationProperty
    private ConnectionProperties connection;

    public ForaProperties(GlobalProperties globalProperties) {
        this.enabled = globalProperties.isEnabled();
        this.connection = globalProperties.getConnection();
    }

}
