package ua.tpetrenko.esp.api;

import ua.tpetrenko.esp.api.dto.MarketInfo;
import ua.tpetrenko.esp.api.handlers.CityHandler;
import ua.tpetrenko.esp.api.handlers.MenuItemHandler;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;
import ua.tpetrenko.esp.api.parser.ParserContext;

/**
 * @author Roman Zdoronok
 */
public interface MarketParser {
    MarketInfo getMarketInfo();

    void prepareParser();
    void parseMainMenu(MenuItemHandler menuItemHandler);
    void parseCities(CityHandler cityHandler);
    void parseItems(ProductItemHandler productItemHandler);
    void destroyParser();
}
