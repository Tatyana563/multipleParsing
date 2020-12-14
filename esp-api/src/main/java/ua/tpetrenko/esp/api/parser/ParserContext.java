package ua.tpetrenko.esp.api.parser;

import ua.tpetrenko.esp.api.handlers.CityHandler;
import ua.tpetrenko.esp.api.handlers.MenuItemHandler;
import ua.tpetrenko.esp.api.iterators.CategoryIterator;
import ua.tpetrenko.esp.api.iterators.CategoryPerCityIterator;

/**
 * @author Roman Zdoronok
 */
public interface ParserContext {
    MenuItemHandler getMenuItemHandler();
    CityHandler getCityHandler();
    CategoryIterator getCategoryIterator();
    CategoryPerCityIterator getCategoryPerCityIterator();
}
