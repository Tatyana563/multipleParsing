package ua.tpetrenko.esp.configuration.properties.model;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Roman Zdoronok
 */
@Getter
@Setter
@NoArgsConstructor
public class ChromeProperties {
    private String path = null;
    private List<String> arguments = Arrays.asList("--headless", "window-size=1920x1080");
    //TODO: copy fields

    public ChromeProperties(ChromeProperties another) {
        this.path = another.getPath();
        this.arguments = another.getArguments();
    }
}
