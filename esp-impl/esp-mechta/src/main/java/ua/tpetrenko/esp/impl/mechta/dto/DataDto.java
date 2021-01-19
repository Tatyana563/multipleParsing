package ua.tpetrenko.esp.impl.mechta.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DataDto {
    @JsonProperty("ITEMS")
    private List<ItemDto> items;

    @JsonProperty("ALL_ITEMS")
    private Integer size;

    @JsonProperty("PAGE_NUMBER")
    private Integer pageNumber;
}
