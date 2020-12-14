package ua.tpetrenko.esp.core.components.iterators;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ua.tpetrenko.esp.api.dto.MenuItemDto;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;
import ua.tpetrenko.esp.api.iterators.CategoryIterator;
import ua.tpetrenko.esp.api.iterators.Iterator;
import ua.tpetrenko.esp.core.factories.ProductItemHandlerFactory;
import ua.tpetrenko.esp.core.model.City;
import ua.tpetrenko.esp.core.model.Market;
import ua.tpetrenko.esp.core.model.MenuItem;
import ua.tpetrenko.esp.core.repository.MenuItemRepository;

/**
 * @author Roman Zdoronok
 */
@Component
@RequiredArgsConstructor
public class CategoryIteratorProvider {

    private final MenuItemRepository menuItemRepository;
    private final ProductItemHandlerFactory productItemHandlerFactory;

    public CategoryIterator forMarket(Market market) {
        return new CategoryIteratorImpl(market);
    }

    public CategoryIterator forMarketAndCity(Market market, City city) {
        return new CategoryIteratorImpl(market, city);
    }

    private class CategoryIteratorImpl
        extends AbstractIterator<MenuItem, MenuItemDto, ProductItemHandler>
        implements CategoryIterator {

        private final Market market;
        private final City city;

        public CategoryIteratorImpl(Market market) {
            this(market, null);
        }

        public CategoryIteratorImpl(Market market, City city) {
            this.market = market;
            this.city = city;
        }

        @Override
        protected Page<MenuItem> getPage(Pageable pageable) {
            return menuItemRepository.findAllEndpointMenuItems(market, pageable);
        }

        @Override
        protected List<Pair<MenuItemDto, ProductItemHandler>> map(Page<MenuItem> page) {
            return page.stream()
                       .map(menuItem -> Pair.of(new MenuItemDto(menuItem.getName(), menuItem.getUrl()),
                                                productItemHandlerFactory
                                                    .getProductItemHandler(city, menuItem)))
                       .collect(Collectors.toList());
        }
    }
}

