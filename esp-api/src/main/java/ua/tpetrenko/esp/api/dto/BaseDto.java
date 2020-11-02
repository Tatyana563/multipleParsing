package ua.tpetrenko.esp.api.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * @author Roman Zdoronok
 */

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class BaseDto {
    private final String name;
    private final String url;
}
