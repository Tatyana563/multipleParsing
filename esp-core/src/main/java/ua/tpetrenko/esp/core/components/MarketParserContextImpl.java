package ua.tpetrenko.esp.core.components;

import lombok.RequiredArgsConstructor;
import ua.tpetrenko.esp.api.handlers.CityHandler;
import ua.tpetrenko.esp.api.handlers.MenuItemHandler;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;
import ua.tpetrenko.esp.api.parser.ParserContext;
import ua.tpetrenko.esp.core.factories.CityHandlerFactory;
import ua.tpetrenko.esp.core.factories.MenuItemHandlerFactory;
import ua.tpetrenko.esp.core.factories.ProductItemHandlerFactory;
import ua.tpetrenko.esp.core.model.Market;

/**
 * @author Roman Zdoronok
 */

@RequiredArgsConstructor
public class MarketParserContextImpl implements ParserContext {

    private final Market market;
    private final MenuItemHandlerFactory menuItemHandlerFactory;
    private final CityHandlerFactory cityHandlerFactory;
    private final ProductItemHandlerFactory productItemHandlerFactory;

    public Market getMarket() {
        return market;
    }

    @Override
    public MenuItemHandler getMenuItemHandler() {
        return menuItemHandlerFactory.getMenuItemHandler(market);
    }

    @Override
    public CityHandler getCityHandler() {
        return cityHandlerFactory.getCityHandler(market);
    }

    @Override
    public ProductItemHandler getProductItemHandler() {
        return productItemHandlerFactory.getProductItemHandler(market);
    }
}
