package ua.tpetrenko.esp.core.tasks;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ua.tpetrenko.esp.api.dto.MenuItemDto;
import ua.tpetrenko.esp.core.api.ParserContext;
import ua.tpetrenko.esp.api.parser.SimpleMarketParser;
import ua.tpetrenko.esp.core.model.MenuItem;
import ua.tpetrenko.esp.core.properties.CoreProperties;
import ua.tpetrenko.esp.core.repository.MarketCityRepository;
import ua.tpetrenko.esp.core.repository.MenuItemRepository;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    protected void parseItems() throws InterruptedException {
        //   log.info("Получаем дополнитульную информацию о товарe...");
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        Page<MenuItem> categories;
        int page = 0;
        // TODO: use common configuration properties (move properties package from app to core)

        while (!(categories = menuItemRepository.findAllEndpointMenuItems(context.getMarket(), PageRequest.of(page++, coreProperties.getCategoryPageSize()))).isEmpty()) {
            CountDownLatch latch = new CountDownLatch(categories.getSize());
            for (MenuItem category : categories) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        marketParser.parseItems(new MenuItemDto(category.getName(), category.getUrl()), context.getProductItemHandler(null, category), latch);
                    }
                });
            }

            latch.await();
        }

        executorService.shutdown();
    }
}
