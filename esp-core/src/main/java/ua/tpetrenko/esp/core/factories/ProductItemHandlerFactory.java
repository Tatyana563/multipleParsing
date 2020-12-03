package ua.tpetrenko.esp.core.factories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;
import ua.tpetrenko.esp.core.components.ProductItemHandlerImpl;
import ua.tpetrenko.esp.core.mappers.ProductItemsMapper;
import ua.tpetrenko.esp.core.model.City;
import ua.tpetrenko.esp.core.model.MenuItem;
import ua.tpetrenko.esp.core.repository.ProductItemInfoRepository;
import ua.tpetrenko.esp.core.repository.ProductItemRepository;
import ua.tpetrenko.esp.core.repository.ProductPriceRepository;

/**
 * @author Roman Zdoronok
 */
@Component
@RequiredArgsConstructor
public class ProductItemHandlerFactory {

    private final ProductItemRepository productItemRepository;
    private final ProductItemInfoRepository productItemInfoRepository;
    private final ProductPriceRepository productPriceRepository;
    private final ProductItemsMapper productItemsMapper;


    public ProductItemHandler getProductItemHandler(MenuItem menuItem) {
        return getProductItemHandler(null, menuItem);
    }

    public ProductItemHandler getProductItemHandler(City city, MenuItem menuItem) {
        return new ProductItemHandlerImpl(city, menuItem, productItemRepository, productItemInfoRepository,productPriceRepository, productItemsMapper);
    }

}
