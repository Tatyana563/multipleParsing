package ua.tpetrenko.esp.shopkz;

import org.springframework.stereotype.Component;
import ua.tpetrenko.esp.api.ShopParser;
import ua.tpetrenko.esp.api.dto.MarketInfo;

/**
 * @author Roman Zdoronok
 */
@Component
public class ShopkzParser implements ShopParser {

    private static final MarketInfo SHOPKZ = new MarketInfo("Shopkz", "https://shopkz.com/");

    @Override
    public MarketInfo getMarketInfo() {
        return SHOPKZ;
    }

    @Override
    public void parseData() {
        // Nothing to do.
    }
}
