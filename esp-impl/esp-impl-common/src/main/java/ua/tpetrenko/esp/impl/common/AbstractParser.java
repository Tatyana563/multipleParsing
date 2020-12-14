package ua.tpetrenko.esp.impl.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.tpetrenko.esp.api.handlers.MenuItemHandler;
import ua.tpetrenko.esp.api.parser.MarketParser;
import ua.tpetrenko.esp.api.parser.ParserContext;

/**
 * @author Roman Zdoronok
 */
public abstract class AbstractParser implements MarketParser {

    protected Logger log = LoggerFactory.getLogger(getClass());

    protected abstract void parseMainMenu(MenuItemHandler menuItemHandler);
    protected abstract void parseProductItems(ParserContext context);

    /**
     * Abstract parser entrypoint.
     * @param context
     */
    protected abstract void parserMain(ParserContext context);


    protected void prepareParser() throws Exception {

    }

    protected void destroyParser() throws Exception {

    }

}
