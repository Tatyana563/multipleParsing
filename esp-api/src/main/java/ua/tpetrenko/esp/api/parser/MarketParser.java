package ua.tpetrenko.esp.api.parser;

import ua.tpetrenko.esp.api.dto.MarketInfo;
import ua.tpetrenko.esp.api.handlers.MenuItemHandler;

/**
 * @author Roman Zdoronok
 */
public interface MarketParser {
    MarketInfo getMarketInfo();
    boolean isEnabled();

    void prepareParser() throws Exception;
    void parseMainMenu(MenuItemHandler menuItemHandler) throws Exception;
    void destroyParser();
}
