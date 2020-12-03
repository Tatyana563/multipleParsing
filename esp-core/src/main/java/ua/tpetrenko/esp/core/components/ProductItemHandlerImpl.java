package ua.tpetrenko.esp.core.components;

import lombok.RequiredArgsConstructor;
import ua.tpetrenko.esp.api.dto.ProductItemDto;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;
import ua.tpetrenko.esp.core.mappers.ProductItemsMapper;
import ua.tpetrenko.esp.core.model.*;
import ua.tpetrenko.esp.core.repository.ProductItemInfoRepository;
import ua.tpetrenko.esp.core.repository.ProductItemRepository;
import ua.tpetrenko.esp.core.repository.ProductPriceRepository;

/**
 * @author Roman Zdoronok
 */
//@RequiredArgsConstructor
public class ProductItemHandlerImpl implements ProductItemHandler {

    private final City city;
    private final MenuItem menuItem;
    private final ProductItemRepository productItemRepository;
    private final ProductItemInfoRepository productItemInfoRepository;
    private final ProductPriceRepository productPriceRepository;
    private final ProductItemsMapper productItemsMapper;

    public ProductItemHandlerImpl(City city, MenuItem menuItem, ProductItemRepository productItemRepository, ProductItemInfoRepository productItemInfoRepository, ProductPriceRepository productPriceRepository, ProductItemsMapper productItemsMapper) {
        this.city = city;
        this.menuItem = menuItem;
        this.productItemRepository = productItemRepository;
        this.productItemInfoRepository = productItemInfoRepository;
        this.productPriceRepository = productPriceRepository;
        this.productItemsMapper = productItemsMapper;
    }

    @Override
    public void handle(ProductItemDto itemDto) {

        ProductItem productItem = productItemRepository.findOneByMenuItemAndUrl(menuItem, itemDto.getUrl())
                .orElseGet(() -> new ProductItem().setMenuItem(menuItem));
        productItem = productItemsMapper.toEntity(productItem, itemDto);
        ProductItem updatedItem = productItemRepository.save(productItem);

        ProductItemInfo productItemInfo = productItemInfoRepository.findOneByProductItem(productItem)
                .orElseGet(() -> new ProductItemInfo().setProductItem(updatedItem)
                        .setDescription(itemDto.getDescription())
                .setExternalId(itemDto.getExternalId()));
        productItemInfoRepository.save(productItemInfo);

        ProductPrice productPrice = productPriceRepository.findOneByProductItemAndCity(productItem, city)
                .orElseGet(() -> new ProductPrice()
                        .setProductItem(updatedItem)
                        .setCity(city));
        productPrice.setPrice(itemDto.getPrice());
        productPriceRepository.save(productPrice);
    }
}
