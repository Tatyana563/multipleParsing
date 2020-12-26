package ua.tpetrenko.esp.impl.mechta.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import ua.tpetrenko.esp.configuration.properties.GlobalProperties;
import ua.tpetrenko.esp.configuration.properties.model.ConnectionProperties;
import ua.tpetrenko.esp.configuration.properties.model.ParserProperties;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "esp.mechta")
public class MechtaProperties extends ParserProperties {

    public MechtaProperties(GlobalProperties globalProperties) {
        this.enabled = globalProperties.isEnabled();
        this.connection = new ConnectionProperties(globalProperties.getConnection());
    }
}
