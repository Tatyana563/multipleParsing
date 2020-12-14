package ua.tpetrenko.esp.api.parser;

import ua.tpetrenko.esp.api.dto.MarketInfo;

/**
 * @author Roman Zdoronok
 */
public interface MarketParser {
    MarketInfo getMarketInfo();
    boolean isEnabled();

    void doParse(ParserContext context);
}
