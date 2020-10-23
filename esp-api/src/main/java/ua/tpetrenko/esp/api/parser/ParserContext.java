package ua.tpetrenko.esp.api.parser;

import ua.tpetrenko.esp.api.handlers.CityHandler;
import ua.tpetrenko.esp.api.handlers.MenuItemHandler;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;

/**
 * @author Roman Zdoronok
 */
public interface ParserContext {
    MenuItemHandler getMenuItemHandler();
    CityHandler getCityHandler();
    ProductItemHandler getProductItemHandler();
}
