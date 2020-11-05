package ua.tpetrenko.esp.core.factories;

import org.springframework.stereotype.Component;
import ua.tpetrenko.esp.api.parser.DifferentItemsPerCityMarketParser;
import ua.tpetrenko.esp.api.parser.MarketParser;
import ua.tpetrenko.esp.core.api.ParserContext;
import ua.tpetrenko.esp.api.parser.SimpleMarketParser;
import ua.tpetrenko.esp.core.tasks.DifferentItemsPerCityMarketParserTask;
import ua.tpetrenko.esp.core.tasks.SimpleMarketParserTask;

/**
 * @author Roman Zdoronok
 */
@Component
public class TaskFactory {

    public Runnable getTask(MarketParser parser, ParserContext context) {
        if (parser instanceof SimpleMarketParser) {
            return new SimpleMarketParserTask((SimpleMarketParser) parser, context);
        }
        if (parser instanceof DifferentItemsPerCityMarketParser) {
            return new DifferentItemsPerCityMarketParserTask((DifferentItemsPerCityMarketParser) parser, context);
        }
        throw new IllegalArgumentException("Неизвестный тип парсера");
    }
}
