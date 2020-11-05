package ua.tpetrenko.esp.core.api;

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
