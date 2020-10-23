package ua.tpetrenko.esp.core.tasks;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.tpetrenko.esp.api.MarketParser;
import ua.tpetrenko.esp.api.parser.ParserContext;

/**
 * @author Roman Zdoronok
 */
@Slf4j
@RequiredArgsConstructor
public class SingleMarketParseTask implements Runnable {

    private final MarketParser marketParser;
    private final ParserContext context;

    @Override
    public void run() {
        try {
            marketParser.prepareParser();
            marketParser.parseMainMenu(context.getMenuItemHandler());
            marketParser.parseCities(context.getCityHandler());
            marketParser.parseItems(context.getProductItemHandler());
        } catch (Exception e) {
            log.error("Возникла проблема при парсинге магазина " + marketParser.getMarketInfo(), e);
        } finally {
            marketParser.destroyParser();
        }
    }
}
