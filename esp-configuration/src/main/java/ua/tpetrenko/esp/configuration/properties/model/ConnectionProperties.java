package ua.tpetrenko.esp.configuration.properties.model;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.convert.DurationUnit;

/**
 * @author Roman Zdoronok
 */
@Getter
@Setter
public class ConnectionProperties {
    @DurationUnit(ChronoUnit.MILLIS)
    private Duration readTimeoutMs = Duration.ofSeconds(30);
    @Positive
    private int retryCount = 10;

}
