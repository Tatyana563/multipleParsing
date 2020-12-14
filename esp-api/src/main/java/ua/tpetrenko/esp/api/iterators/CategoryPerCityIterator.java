package ua.tpetrenko.esp.api.iterators;

import ua.tpetrenko.esp.api.dto.CityDto;

/**
 * @author Roman Zdoronok
 */
public interface CategoryPerCityIterator extends Iterator<CityDto, CategoryIterator> {
    //Marker interface.
}
