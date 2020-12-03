package ua.tpetrenko.esp.core.factories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.tpetrenko.esp.api.parser.DifferentItemsPerCityMarketParser;
import ua.tpetrenko.esp.api.parser.DifferentItemsPerCityMarketParserSelenium;
import ua.tpetrenko.esp.api.parser.MarketParser;
import ua.tpetrenko.esp.core.api.ParserContext;
import ua.tpetrenko.esp.api.parser.SimpleMarketParser;
import ua.tpetrenko.esp.core.properties.CoreProperties;
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
    private final CoreProperties coreProperties;

    public Runnable getTask(MarketParser parser, ParserContext context) {
        if (parser instanceof SimpleMarketParser) {
            return new SimpleMarketParserTask((SimpleMarketParser) parser,
                    context,
                    marketCityRepository,
                    menuItemRepository,
                    coreProperties);
        }
        if (parser instanceof DifferentItemsPerCityMarketParser||parser instanceof DifferentItemsPerCityMarketParserSelenium) {
            return new DifferentItemsPerCityMarketParserTask((DifferentItemsPerCityMarketParser) parser,
                    context,
                    marketCityRepository,
                    menuItemRepository,
                    coreProperties);
        }
        throw new IllegalArgumentException("Неизвестный тип парсера");
    }
}
