package ua.tpetrenko.esp.core.components;

import lombok.RequiredArgsConstructor;
import ua.tpetrenko.esp.api.dto.ProductItemDto;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;
import ua.tpetrenko.esp.core.model.City;
import ua.tpetrenko.esp.core.model.Market;
import ua.tpetrenko.esp.core.model.ProductItem;
import ua.tpetrenko.esp.core.repository.ProductItemRepository;
import ua.tpetrenko.esp.core.repository.ProductPriceRepository;

/**
 * @author Roman Zdoronok
 */
@RequiredArgsConstructor
public class ProductItemHandlerImpl implements ProductItemHandler {

    private final Market market;
    private final City city;
    private final ProductItemRepository productItemRepository;
    private final ProductPriceRepository productPriceRepository;

    @Override
    public void handle(ProductItemDto itemDto) {
        ProductItem productItem = new ProductItem();
    }
}
