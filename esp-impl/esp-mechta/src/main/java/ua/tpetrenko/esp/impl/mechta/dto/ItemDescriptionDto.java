package ua.tpetrenko.esp.impl.mechta.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ItemDescriptionDto {
    @JsonProperty("PROP_NAME")
    private String name;

    @JsonProperty("PROP_VALUE")
    private String value;
}
