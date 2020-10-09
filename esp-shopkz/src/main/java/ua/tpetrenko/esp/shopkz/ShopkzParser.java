package ua.tpetrenko.esp.shopkz;

import org.springframework.stereotype.Component;
import ua.tpetrenko.esp.api.ShopParser;
import ua.tpetrenko.esp.api.dto.ShopInfo;

/**
 * @author Roman Zdoronok
 */
@Component
public class ShopkzParser implements ShopParser {

    private static final ShopInfo SHOPKZ = new ShopInfo("Shopkz", "https://shopkz.com/");

    @Override
    public ShopInfo getShopInfo() {
        return SHOPKZ;
    }

    @Override
    public void parseData() {
        // Nothing to do.
    }
}
