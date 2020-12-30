package ua.tpetrenko.esp.impl.technodom.properties;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.context.annotation.Configuration;
import ua.tpetrenko.esp.configuration.properties.GlobalProperties;
import ua.tpetrenko.esp.configuration.properties.model.ChromeProperties;
import ua.tpetrenko.esp.configuration.properties.model.ConnectionProperties;
import ua.tpetrenko.esp.configuration.properties.model.ParserProperties;

/**
 * @author Roman Zdoronok
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "esp.technodom")
public class TechnodomProperties extends ParserProperties {

    @NestedConfigurationProperty
    private ChromeProperties chrome;

    @DurationUnit(ChronoUnit.MILLIS)
    private Duration modalWindowPresentTimeoutMs = Duration.ofSeconds(20);
    public TechnodomProperties(GlobalProperties globalProperties) {
        this.connection = new ConnectionProperties(globalProperties.getConnection());
        this.chrome = new ChromeProperties(globalProperties.getChrome());
    }
}
