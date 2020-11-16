package ua.tpetrenko.esp.core.tasks;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.tpetrenko.esp.api.dto.MarketInfo;
import ua.tpetrenko.esp.api.parser.MarketParser;
import ua.tpetrenko.esp.core.api.ParserContext;
import ua.tpetrenko.esp.core.properties.CoreProperties;
import ua.tpetrenko.esp.core.repository.MarketCityRepository;
import ua.tpetrenko.esp.core.repository.MenuItemRepository;

/**
 * @author Roman Zdoronok
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public abstract class AbstractMarketParserTask<P extends MarketParser> implements Runnable {
    protected final P marketParser;
    protected final ParserContext context;
    protected final MarketCityRepository marketCityRepository;
    protected final MenuItemRepository menuItemRepository;
    protected final CoreProperties coreProperties;

    protected abstract void parseItems() throws Exception;

    @Override
    public final void run() {
        MarketInfo marketInfo = marketParser.getMarketInfo();
        try {
            log.info("Подготовка парсера для {}", marketInfo.getName());
            marketParser.prepareParser();
            marketParser.parseMainMenu(context.getMenuItemHandler());
            parseItems();
        } catch (Exception e) {
            log.error("Возникла проблема при парсинге магазина " + marketInfo.getName(), e);
        } finally {
            marketParser.destroyParser();
        }
    }

}
