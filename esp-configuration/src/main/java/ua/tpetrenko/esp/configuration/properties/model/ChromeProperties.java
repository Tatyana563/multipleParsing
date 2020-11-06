package ua.tpetrenko.esp.configuration.properties.model;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Roman Zdoronok
 */
@Getter
@Setter
public class ChromeProperties {
    private String path = null;
    private List<String> arguments = Arrays.asList("--headless", "window-size=1920x1080");
}
