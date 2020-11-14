package ua.tpetrenko.esp.api.parser;

import ua.tpetrenko.esp.api.dto.CityDto;
import ua.tpetrenko.esp.api.dto.MenuItemDto;
import ua.tpetrenko.esp.api.handlers.CityHandler;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;

/**
 * @author Roman Zdoronok
 */
public interface DifferentItemsPerCityMarketParser extends MarketParser{
    void parseCities(CityHandler cityHandler) throws Exception;
    void parseItems(CityDto cityDto, MenuItemDto menuItemDto, ProductItemHandler productItemHandler) throws Exception;
}
