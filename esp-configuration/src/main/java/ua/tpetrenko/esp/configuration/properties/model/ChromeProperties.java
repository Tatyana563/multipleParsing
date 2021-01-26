package ua.tpetrenko.esp.configuration.properties.model;

import java.util.ArrayList;
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
    private List<String> arguments = Arrays.asList("--headless", "window-size=1920,1080");

    public ChromeProperties(ChromeProperties another) {
        this.path = another.getPath();
        this.arguments = new ArrayList<>(another.getArguments());
    }
}
