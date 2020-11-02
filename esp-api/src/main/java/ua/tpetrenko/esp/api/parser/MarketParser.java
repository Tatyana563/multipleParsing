package ua.tpetrenko.esp.api.parser;

import ua.tpetrenko.esp.api.dto.MarketInfo;
import ua.tpetrenko.esp.api.handlers.CityHandler;
import ua.tpetrenko.esp.api.handlers.MenuItemHandler;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;

/**
 * @author Roman Zdoronok
 */
public interface MarketParser {
    MarketInfo getMarketInfo();
    boolean isEnabled();

    void prepareParser() throws Exception;
    void parseMainMenu(MenuItemHandler menuItemHandler) throws Exception;
    void parseCities(CityHandler cityHandler) throws Exception;
    void parseItems(ProductItemHandler productItemHandler) throws Exception;
    void destroyParser();
}
