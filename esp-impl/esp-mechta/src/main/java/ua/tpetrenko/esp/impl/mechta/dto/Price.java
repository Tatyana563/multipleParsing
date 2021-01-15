package ua.tpetrenko.esp.impl.mechta.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Price {
    @JsonProperty("PRICE")
    private String price;
}
