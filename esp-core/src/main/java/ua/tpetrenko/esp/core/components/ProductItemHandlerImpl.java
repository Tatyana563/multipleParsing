package ua.tpetrenko.esp.core.components;

import lombok.RequiredArgsConstructor;
import ua.tpetrenko.esp.api.dto.ProductItemDto;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;
import ua.tpetrenko.esp.core.mappers.ProductItemsMapper;
import ua.tpetrenko.esp.core.model.*;
import ua.tpetrenko.esp.core.repository.ProductItemRepository;
import ua.tpetrenko.esp.core.repository.ProductPriceRepository;

/**
 * @author Roman Zdoronok
 */
@RequiredArgsConstructor
public class ProductItemHandlerImpl implements ProductItemHandler {

    private final MarketCity marketCity;
    private final MenuItem menuItem;
    private final ProductItemRepository productItemRepository;
    private final ProductPriceRepository productPriceRepository;
    private final ProductItemsMapper productItemsMapper;

    @Override
    public void handle(ProductItemDto itemDto) {

        ProductItem productItem = productItemRepository.findOneByMenuItemAndExternalId(menuItem, itemDto.getExternalId())
                                                       .orElseGet(() -> new ProductItem().setMenuItem(menuItem));
        productItem = productItemsMapper.toEntity(productItem, itemDto);
        ProductItem updatedItem = productItemRepository.save(productItem);
        ProductPrice productPrice = productPriceRepository.findOneByProductItemAndMarketCity(productItem, marketCity)
                                                          .orElseGet(() -> new ProductPrice()
                                                              .setProductItem(updatedItem)
                                                              .setMarketCity(marketCity));
        productPrice.setPrice(itemDto.getPrice());
        productPriceRepository.save(productPrice);
    }
}
