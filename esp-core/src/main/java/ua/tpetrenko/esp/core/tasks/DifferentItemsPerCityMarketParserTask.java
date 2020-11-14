package ua.tpetrenko.esp.core.tasks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ua.tpetrenko.esp.api.dto.CityDto;
import ua.tpetrenko.esp.api.dto.MenuItemDto;
import ua.tpetrenko.esp.api.parser.DifferentItemsPerCityMarketParser;
import ua.tpetrenko.esp.core.api.ParserContext;
import ua.tpetrenko.esp.core.model.MarketCity;
import ua.tpetrenko.esp.core.model.MenuItem;
import ua.tpetrenko.esp.core.repository.CityRepository;
import ua.tpetrenko.esp.core.repository.MarketCityRepository;
import ua.tpetrenko.esp.core.repository.MenuItemRepository;

import java.util.List;

/**
 * @author Roman Zdoronok
 */
@Slf4j
public class DifferentItemsPerCityMarketParserTask extends AbstractMarketParserTask<DifferentItemsPerCityMarketParser> {

    public DifferentItemsPerCityMarketParserTask(DifferentItemsPerCityMarketParser marketParser,
                                                 ParserContext context,
                                                 MarketCityRepository marketCityRepository,
                                                 MenuItemRepository menuItemRepository) {
        super(marketParser, context, marketCityRepository, menuItemRepository);
    }

    @Override
    protected void parseItems() throws Exception {
        marketParser.parseCities(context.getCityHandler());

        //TODO: use common configuration properties
        Page<MarketCity> cities = marketCityRepository.findAllByMarket(context.getMarket(), Pageable.unpaged());
        for (MarketCity city : cities) {
            Page<MenuItem> categories;
            int page = 0;
            // TODO: use common configuration properties (move properties package from app to core)
            while (!(categories = menuItemRepository.findAllEndpointMenuItems(context.getMarket(), PageRequest.of(page++, 50))).isEmpty()) {
                for (MenuItem category : categories) {
                    marketParser.parseItems(new CityDto(city.getCity().getName(), city.getUrl()),
                            new MenuItemDto(category.getName(), category.getUrl()),
                            context.getProductItemHandler(city.getCity(), category));
                }

            }
        }

    }

}
