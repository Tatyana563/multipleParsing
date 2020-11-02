package ua.tpetrenko.esp.api.dto;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Roman Zdoronok
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MarketInfo extends BaseDto {

    public MarketInfo(String name, String url) {
        super(name, url);
    }
}
