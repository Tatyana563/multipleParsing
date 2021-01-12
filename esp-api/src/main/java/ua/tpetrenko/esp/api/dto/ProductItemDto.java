package ua.tpetrenko.esp.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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

    @JsonProperty()
    private String code;
    @JsonProperty()
    private String externalId;
    @JsonProperty("PHOTO")
    private String imageUrl;
    @JsonProperty()
    private String description;
    @JsonProperty("PRICE_WITHOUT_DISCOUNT")
    private Double price;
}
