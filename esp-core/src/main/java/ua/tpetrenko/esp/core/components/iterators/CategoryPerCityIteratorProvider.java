package ua.tpetrenko.esp.core.components.iterators;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ua.tpetrenko.esp.api.dto.CityDto;
import ua.tpetrenko.esp.api.iterators.CategoryIterator;
import ua.tpetrenko.esp.api.iterators.CategoryPerCityIterator;
import ua.tpetrenko.esp.core.model.Market;
import ua.tpetrenko.esp.core.model.MarketCity;
import ua.tpetrenko.esp.core.repository.MarketCityRepository;

/**
 * @author Roman Zdoronok
 */
@Component
@RequiredArgsConstructor
public class CategoryPerCityIteratorProvider {

    private final MarketCityRepository marketCityRepository;
    private final CategoryIteratorProvider categoryIteratorProvider;

    public CategoryPerCityIterator forMarket(Market market) {
        return new CategoryPerCityIteratorImpl(market);
    }

    private class CategoryPerCityIteratorImpl extends
        AbstractIterator<MarketCity, CityDto, CategoryIterator>
        implements CategoryPerCityIterator {

        private final Market market;

        private CategoryPerCityIteratorImpl(Market market) {
            this.market = market;
        }

        @Override
        protected Page<MarketCity> getPage(Pageable pageable) {
            return marketCityRepository.findAllByMarket(market, pageable);
        }

        @Override
        protected List<Pair<CityDto, CategoryIterator>> map(Page<MarketCity> page) {
            return page.stream()
                       .map(city -> Pair.of(new CityDto(city.getCity().getName(), city.getUrl()),
                                            categoryIteratorProvider.forMarketAndCity(market, city.getCity())))
                       .collect(Collectors.toList());
        }
    }
}
