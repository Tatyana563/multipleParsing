package ua.tpetrenko.esp.configuration.properties.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ParserProperties {
    protected boolean enabled;
    protected List<String> categoriesWhitelist;
}
