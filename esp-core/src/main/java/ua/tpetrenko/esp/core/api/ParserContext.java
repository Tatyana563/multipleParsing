package ua.tpetrenko.esp.core.api;

import ua.tpetrenko.esp.api.handlers.CityHandler;
import ua.tpetrenko.esp.api.handlers.MenuItemHandler;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;
import ua.tpetrenko.esp.core.model.City;
import ua.tpetrenko.esp.core.model.Market;
import ua.tpetrenko.esp.core.model.MenuItem;

/**
 * @author Roman Zdoronok
 */
public interface ParserContext {
    Market getMarket();
    MenuItemHandler getMenuItemHandler();
    CityHandler getCityHandler();
    ProductItemHandler getProductItemHandler(City city, MenuItem menuItem);
}
