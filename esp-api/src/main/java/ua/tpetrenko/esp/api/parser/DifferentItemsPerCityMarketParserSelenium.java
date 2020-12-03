package ua.tpetrenko.esp.api.parser;

import ua.tpetrenko.esp.api.dto.CityDto;
import ua.tpetrenko.esp.api.dto.MenuItemDto;
import ua.tpetrenko.esp.api.handlers.CityHandler;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;

public interface DifferentItemsPerCityMarketParserSelenium extends MarketParser{
    void parseCities(long loaded, CityHandler cityHandler) throws Exception;
    void parseItems(CityDto cityDto, MenuItemDto menuItemDto, ProductItemHandler productItemHandler) throws Exception;
}


