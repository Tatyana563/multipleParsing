package ua.tpetrenko.esp.api.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * @author Roman Zdoronok
 */
@RequiredArgsConstructor
@Getter
@ToString
public class MarketInfo {
    private final String name;
    private final String url;
}
