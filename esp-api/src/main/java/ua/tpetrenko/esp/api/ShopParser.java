package ua.tpetrenko.esp.api;

import ua.tpetrenko.esp.api.dto.ShopInfo;

/**
 * @author Roman Zdoronok
 */
public interface ShopParser {
    ShopInfo getShopInfo();
    void parseData();
}
