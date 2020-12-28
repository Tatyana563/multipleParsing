package ua.tpetrenko.esp.impl.shopkz.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ua.tpetrenko.esp.configuration.properties.GlobalProperties;
import ua.tpetrenko.esp.configuration.properties.model.ConnectionProperties;
import ua.tpetrenko.esp.configuration.properties.model.ParserProperties;


@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "esp.shopkz")
public class ShopkzProperties extends ParserProperties {

    public ShopkzProperties(GlobalProperties globalProperties) {
        this.enabled = globalProperties.isEnabled();
        //TODO: create connection properties copy
        this.connection = new ConnectionProperties(globalProperties.getConnection());
    }
}




