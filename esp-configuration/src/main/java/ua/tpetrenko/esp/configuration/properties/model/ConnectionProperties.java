package ua.tpetrenko.esp.configuration.properties.model;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.convert.DurationUnit;

/**
 * @author Roman Zdoronok
 */
@Getter
@Setter
@NoArgsConstructor
public class ConnectionProperties {
    @DurationUnit(ChronoUnit.MILLIS)
    private Duration readTimeoutMs = Duration.ofSeconds(30);
    @Positive
    private int retryCount = 10;

    public ConnectionProperties(ConnectionProperties another) {
        this.readTimeoutMs = another.readTimeoutMs;
        this.retryCount = another.retryCount;
    }
}
