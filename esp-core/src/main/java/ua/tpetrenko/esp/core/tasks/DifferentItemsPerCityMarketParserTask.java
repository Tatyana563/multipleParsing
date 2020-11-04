package ua.tpetrenko.esp.core.tasks;

import lombok.extern.slf4j.Slf4j;
import ua.tpetrenko.esp.api.parser.DifferentItemsPerCityMarketParser;
import ua.tpetrenko.esp.api.parser.ParserContext;

/**
 * @author Roman Zdoronok
 */
@Slf4j
public class DifferentItemsPerCityMarketParserTask extends AbstractMarketParserTask<DifferentItemsPerCityMarketParser> {

    public DifferentItemsPerCityMarketParserTask(DifferentItemsPerCityMarketParser marketParser,
                                                 ParserContext context) {
        super(marketParser, context);
    }

    @Override
    protected void parseItems() throws Exception {
        marketParser.parseCities(context.getCityHandler());
        marketParser.parseItems(context.getProductItemHandler());
    }

}
