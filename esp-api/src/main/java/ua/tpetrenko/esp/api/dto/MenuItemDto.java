package ua.tpetrenko.esp.api.dto;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Roman Zdoronok
 */
@ToString
@EqualsAndHashCode(callSuper = true)
public class MenuItemDto extends BaseDto {

    public MenuItemDto(String name, String url) {
        super(name, url);
    }
}
