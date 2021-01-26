package ua.tpetrenko.esp.impl.mechta.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ItemDto {
    @JsonProperty("ID")
    private String id;
    @JsonProperty("NAME")
    private String name;
    @JsonProperty("PHOTO")
    private List<String> photos;
    @JsonProperty("PRICE")
    private Price price;
    @JsonProperty("CODE")
    private String code;
    @JsonProperty("MAIN_PROPERTIES")
    private List<ItemDescriptionDto> description;
}