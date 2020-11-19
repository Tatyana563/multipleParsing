package ua.tpetrenko.esp.api.parser;

import ua.tpetrenko.esp.api.dto.MenuItemDto;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;

import java.util.concurrent.CountDownLatch;

/**
 * @author Roman Zdoronok
 */
public interface SimpleMarketParser extends MarketParser {
    void parseItems(MenuItemDto menuItemDto, ProductItemHandler productItemHandler, CountDownLatch latch);
}
