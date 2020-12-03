package ua.tpetrenko.esp.impl.mechta.properties;

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
@ConfigurationProperties(prefix = "esp.mechta")
public class MechtaProperties {
    private boolean enabled;
    //TOO: change type
    private String[] categoriesWhitelist;
    @NestedConfigurationProperty
    private ConnectionProperties connection;

    public MechtaProperties(GlobalProperties globalProperties) {
        this.enabled = globalProperties.isEnabled();
        this.connection = globalProperties.getConnection();
    }
}
