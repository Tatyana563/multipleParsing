package ua.tpetrenko.esp.core.factories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.tpetrenko.esp.api.dto.MarketInfo;
import ua.tpetrenko.esp.api.parser.ParserContext;
import ua.tpetrenko.esp.core.components.MarketParserContextImpl;
import ua.tpetrenko.esp.core.components.iterators.CategoryIteratorProvider;
import ua.tpetrenko.esp.core.components.iterators.CategoryPerCityIteratorProvider;
import ua.tpetrenko.esp.core.model.Market;
import ua.tpetrenko.esp.core.repository.MarketRepository;

/**
 * @author Roman Zdoronok
 */
@Component
@RequiredArgsConstructor
public class ParserContextFactory {

    private final MarketRepository marketRepository;
    private final MenuItemHandlerFactory menuItemHandlerFactory;
    private final CityHandlerFactory cityHandlerFactory;
    private final ProductItemHandlerFactory productItemHandlerFactory;
    private final CategoryIteratorProvider categoryIteratorProvider;
    private final CategoryPerCityIteratorProvider categoryPerCityIteratorProvider;


    public ParserContext getParserContext(MarketInfo marketInfo) {
        Market market = marketRepository.findOneByName(marketInfo.getName())
                                      .orElseGet(() -> {
                                          Market newMarket = new Market(marketInfo.getName(), marketInfo.getUrl());
                                          return marketRepository.save(newMarket);
                                      });

        return new MarketParserContextImpl(market,
                                           menuItemHandlerFactory,
                                           cityHandlerFactory,
                                           categoryIteratorProvider,
                                           categoryPerCityIteratorProvider);
    }


}
