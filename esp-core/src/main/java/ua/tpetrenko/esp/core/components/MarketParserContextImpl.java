package ua.tpetrenko.esp.core.components;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ua.tpetrenko.esp.api.dto.MarketInfo;
import ua.tpetrenko.esp.api.handlers.CityHandler;
import ua.tpetrenko.esp.api.handlers.MenuItemHandler;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;
import ua.tpetrenko.esp.api.parser.ParserContext;
import ua.tpetrenko.esp.core.model.Market;
import ua.tpetrenko.esp.core.repository.MarketRepository;

/**
 * @author Roman Zdoronok
 */

@Component
@Scope("prototype")
public class MarketParserContextImpl implements ParserContext {

    private final MarketRepository marketRepository;
    private final Market market;


    public MarketParserContextImpl(MarketRepository marketRepository, MarketInfo marketInfo) {
        this.marketRepository = marketRepository;
        this.market = marketRepository.findOneByName(marketInfo.getName())
                                      .orElseGet(() -> {
                                          Market newMarket = new Market(marketInfo.getName(), marketInfo.getUrl());
                                          return marketRepository.save(newMarket);
                                      });
    }

    @Override
    public MenuItemHandler getMenuItemHandler() {
        return null;
    }

    @Override
    public CityHandler getCityHandler() {
        return null;
    }

    @Override
    public ProductItemHandler getProductItemHandler() {
        return null;
    }
}
