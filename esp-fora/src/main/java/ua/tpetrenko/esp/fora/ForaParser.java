package ua.tpetrenko.esp.fora;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.tpetrenko.esp.api.MarketParser;
import ua.tpetrenko.esp.api.dto.MarketInfo;
import ua.tpetrenko.esp.api.handlers.CityHandler;
import ua.tpetrenko.esp.api.handlers.MenuItemHandler;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;

@Slf4j
@Component
public class ForaParser implements MarketParser {

    @Override
    public MarketInfo getMarketInfo() {
        return new MarketInfo("Fora", "https://fora.kz/");
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
