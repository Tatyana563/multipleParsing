package ua.tpetrenko.esp.core.tasks;

import ua.tpetrenko.esp.core.api.ParserContext;
import ua.tpetrenko.esp.api.parser.SimpleMarketParser;

/**
 * @author Roman Zdoronok
 */
public class SimpleMarketParserTask extends AbstractMarketParserTask<SimpleMarketParser> {

    public SimpleMarketParserTask(SimpleMarketParser marketParser, ParserContext context) {
        super(marketParser, context);
    }

    @Override
    protected void parseItems() throws Exception {
        marketParser.parseItems(context.getProductItemHandler());
    }
}
