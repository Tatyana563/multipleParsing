package ua.tpetrenko.esp.impl.mechta.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CatalogDto {
    @JsonProperty("data")
    private DataDto data;
}
