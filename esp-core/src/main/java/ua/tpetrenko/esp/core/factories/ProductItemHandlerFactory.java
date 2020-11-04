package ua.tpetrenko.esp.core.factories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;
import ua.tpetrenko.esp.core.components.ProductItemHandlerImpl;
import ua.tpetrenko.esp.core.model.City;
import ua.tpetrenko.esp.core.model.Market;
import ua.tpetrenko.esp.core.repository.ProductItemRepository;
import ua.tpetrenko.esp.core.repository.ProductPriceRepository;

/**
 * @author Roman Zdoronok
 */
@Component
@RequiredArgsConstructor
public class ProductItemHandlerFactory {

    private final ProductItemRepository productItemRepository;
    private final ProductPriceRepository productPriceRepository;

    public ProductItemHandler getProductItemHandler(Market market) {
        return getProductItemHandler(market, null);
    }

    public ProductItemHandler getProductItemHandler(Market market, City city) {
        return new ProductItemHandlerImpl(market, city, productItemRepository, productPriceRepository);
    }
}
