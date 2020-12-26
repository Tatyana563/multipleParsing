package ua.tpetrenko.esp.impl.fora.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ua.tpetrenko.esp.configuration.properties.GlobalProperties;
import ua.tpetrenko.esp.configuration.properties.model.ConnectionProperties;
import ua.tpetrenko.esp.configuration.properties.model.ParserProperties;

import java.time.Duration;

/**
 * @author Roman Zdoronok
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "esp.fora")
public class ForaProperties extends ParserProperties {

    public ForaProperties(GlobalProperties globalProperties) {
        this.enabled = globalProperties.isEnabled();
//        this.connection = new ConnectionProperties(globalProperties.getConnection().getReadTimeoutMs(), globalProperties.getConnection().getRetryCount());
        this.connection = new ConnectionProperties(globalProperties.getConnection());
    }

}
