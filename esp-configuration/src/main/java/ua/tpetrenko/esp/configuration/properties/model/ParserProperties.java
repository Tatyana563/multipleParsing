package ua.tpetrenko.esp.configuration.properties.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.List;
@Getter
@Setter
public class ParserProperties {
    protected boolean enabled;
    protected List<String> categoriesWhitelist;
    protected List<String> categoriesBlacklist;

    @NestedConfigurationProperty
    protected ConnectionProperties connection;
}
