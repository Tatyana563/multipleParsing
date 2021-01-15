package ua.tpetrenko.esp.impl.mechta.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDto {
    @JsonProperty("ID")
    private String id;
    @JsonProperty("NAME")
    private String name;
    @JsonProperty("PHOTO")
    private Photo photo;
    @JsonProperty("PRICE")
    private Price price;

}
