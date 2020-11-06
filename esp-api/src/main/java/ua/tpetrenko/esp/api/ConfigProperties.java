package ua.tpetrenko.esp.api;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "parser")
public class ConfigProperties {
    private String path;
    @Positive
    private String timeoutMs;
    @Positive
    private String retryCount;
    @Positive
    private String modalWindowPresentTimeout;
    @Positive
    private String chunkSize;
    @Positive
    private String poolSize;
    @NotEmpty
    private String fora;
    @NotEmpty
    private String technodom;
    @NotEmpty
    private String chromePath;
}
