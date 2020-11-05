package ua.tpetrenko.esp.api.parser;

import ua.tpetrenko.esp.api.handlers.ProductItemHandler;

/**
 * @author Roman Zdoronok
 */
public interface SimpleMarketParser extends MarketParser {
    void parseItems(ProductItemHandler productItemHandler) throws Exception;
}
