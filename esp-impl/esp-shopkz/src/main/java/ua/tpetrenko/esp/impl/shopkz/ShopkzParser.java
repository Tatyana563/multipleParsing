package ua.tpetrenko.esp.impl.shopkz;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ua.tpetrenko.esp.api.parser.DifferentItemsPerCityMarketParser;
import ua.tpetrenko.esp.api.dto.MarketInfo;
import ua.tpetrenko.esp.api.handlers.CityHandler;
import ua.tpetrenko.esp.api.handlers.MenuItemHandler;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;

/**
 * @author Roman Zdoronok
 */
//@Slf4j
@Component
public class ShopkzParser implements DifferentItemsPerCityMarketParser {
    private static Logger log = LoggerFactory.getLogger("SHOPKZLOGGER");
    private static final MarketInfo INFO = new MarketInfo("Shop.kz", "https://shop.kz/");

    @Override
    public MarketInfo getMarketInfo() {
        return INFO;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void prepareParser() {
        log.info("Подготовка " + getMarketInfo());
    }

    @Override
    public void parseMainMenu(MenuItemHandler menuItemHandler) {
        // Nothing to do.
    }

    @Override
    public void parseCities(CityHandler cityHandler) {
        // Nothing to do.
    }

    @Override
    public void parseItems(ProductItemHandler productItemHandler) {
        // Nothing to do.
    }

    @Override
    public void destroyParser() {
        // Nothing to do.
    }

}
