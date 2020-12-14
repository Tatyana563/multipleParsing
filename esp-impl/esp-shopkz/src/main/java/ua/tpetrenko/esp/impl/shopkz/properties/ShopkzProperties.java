package ua.tpetrenko.esp.impl.shopkz.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import ua.tpetrenko.esp.configuration.properties.GlobalProperties;
import ua.tpetrenko.esp.configuration.properties.model.ConnectionProperties;
import ua.tpetrenko.esp.configuration.properties.model.ParserProperties;
import ua.tpetrenko.esp.impl.shopkz.ShopkzParser;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "esp.shopkz")
public class ShopkzProperties extends ParserProperties {

    //TODO ~
    @NestedConfigurationProperty
    private ConnectionProperties connection;

    public ShopkzProperties(GlobalProperties globalProperties) {
        this.enabled = globalProperties.isEnabled();
        this.connection = globalProperties.getConnection();
    }
}




