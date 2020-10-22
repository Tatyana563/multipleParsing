package ua.tpetrenko.esp.api;

import ua.tpetrenko.esp.api.dto.MarketInfo;

/**
 * @author Roman Zdoronok
 */
public interface ShopParser {
    MarketInfo getMarketInfo();
    void parseData();
}
