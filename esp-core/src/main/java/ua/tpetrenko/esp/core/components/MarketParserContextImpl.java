package ua.tpetrenko.esp.core.components;

import lombok.RequiredArgsConstructor;
import ua.tpetrenko.esp.api.handlers.CityHandler;
import ua.tpetrenko.esp.api.handlers.MenuItemHandler;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;
import ua.tpetrenko.esp.api.parser.ParserContext;
import ua.tpetrenko.esp.core.model.Market;

/**
 * @author Roman Zdoronok
 */

@RequiredArgsConstructor
public class MarketParserContextImpl implements ParserContext {

    private final Market market;
    private final MenuItemHandler rootMenuItemHandler;
    private final CityHandler cityHandler;
    private final ProductItemHandler productItemHandler;

    public Market getMarket() {
        return market;
    }

    @Override
    public MenuItemHandler getMenuItemHandler() {
        return rootMenuItemHandler;
    }

    @Override
    public CityHandler getCityHandler() {
        return cityHandler;
    }

    @Override
    public ProductItemHandler getProductItemHandler() {
        return productItemHandler;
    }
}
