package ua.tpetrenko.esp.api.dto;

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
