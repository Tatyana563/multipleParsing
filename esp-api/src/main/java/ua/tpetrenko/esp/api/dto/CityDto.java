package ua.tpetrenko.esp.api.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * @author Roman Zdoronok
 */

@ToString
@EqualsAndHashCode(callSuper = true)
public class CityDto extends BaseDto {

    public CityDto(String name, String url) {
        super(name, url);
    }
}
