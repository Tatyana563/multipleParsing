package ua.tpetrenko.esp.core.factories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.tpetrenko.esp.api.parser.DifferentItemsPerCityMarketParser;
import ua.tpetrenko.esp.api.parser.MarketParser;
import ua.tpetrenko.esp.core.api.ParserContext;
import ua.tpetrenko.esp.api.parser.SimpleMarketParser;
import ua.tpetrenko.esp.core.repository.CityRepository;
import ua.tpetrenko.esp.core.repository.MarketCityRepository;
import ua.tpetrenko.esp.core.repository.MenuItemRepository;
import ua.tpetrenko.esp.core.tasks.DifferentItemsPerCityMarketParserTask;
import ua.tpetrenko.esp.core.tasks.SimpleMarketParserTask;

/**
 * @author Roman Zdoronok
 */
@Component
@RequiredArgsConstructor
public class TaskFactory {

    private final MarketCityRepository marketCityRepository;
    private final MenuItemRepository menuItemRepository;

    public Runnable getTask(MarketParser parser, ParserContext context) {
        if (parser instanceof SimpleMarketParser) {
            return new SimpleMarketParserTask((SimpleMarketParser) parser,
                    context,
                    marketCityRepository,
                    menuItemRepository);
        }
        if (parser instanceof DifferentItemsPerCityMarketParser) {
            return new DifferentItemsPerCityMarketParserTask((DifferentItemsPerCityMarketParser) parser,
                    context,
                    marketCityRepository,
                    menuItemRepository);
        }
        throw new IllegalArgumentException("Неизвестный тип парсера");
    }
}
