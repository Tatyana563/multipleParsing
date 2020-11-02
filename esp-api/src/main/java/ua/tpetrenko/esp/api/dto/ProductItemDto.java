package ua.tpetrenko.esp.api.dto;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Roman Zdoronok
 */
@ToString
@EqualsAndHashCode(callSuper = true)
public class ProductItemDto extends BaseDto {

    public ProductItemDto(String name, String url) {
        super(name, url);
    }
}
