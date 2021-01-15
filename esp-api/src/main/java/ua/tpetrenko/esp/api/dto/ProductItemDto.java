package ua.tpetrenko.esp.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author Roman Zdoronok
 */
@ToString
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductItemDto extends BaseDto {

    public ProductItemDto(String name, String url) {
        super(name, url);
    }

    private String code;
    private String externalId;
    private String imageUrl;
    private String description;
    private Double price;
}
