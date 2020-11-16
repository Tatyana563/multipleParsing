package ua.tpetrenko.esp.core.tasks;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ua.tpetrenko.esp.core.api.ParserContext;
import ua.tpetrenko.esp.api.parser.SimpleMarketParser;
import ua.tpetrenko.esp.core.model.MenuItem;
import ua.tpetrenko.esp.core.properties.CoreProperties;
import ua.tpetrenko.esp.core.repository.MarketCityRepository;
import ua.tpetrenko.esp.core.repository.MenuItemRepository;

/**
 * @author Roman Zdoronok
 */
public class SimpleMarketParserTask extends AbstractMarketParserTask<SimpleMarketParser> {

    public SimpleMarketParserTask(SimpleMarketParser marketParser,
                                  ParserContext context,
                                  MarketCityRepository marketCityRepository,
                                  MenuItemRepository menuItemRepository,
                                  CoreProperties coreProperties) {
        super(marketParser, context, marketCityRepository, menuItemRepository, coreProperties);
    }

    @Override
    protected void parseItems() throws Exception {
        Page<MenuItem> categories;
        int page = 0;
        // TODO: use common configuration properties (move properties package from app to core)
        while (!(categories = menuItemRepository.findAllEndpointMenuItems(context.getMarket(), PageRequest.of(page++,  coreProperties.getCategoryPageSize()))).isEmpty()) {
            for (MenuItem category : categories) {
                marketParser.parseItems(context.getProductItemHandler(null, category));
            }

        }
    }
}
