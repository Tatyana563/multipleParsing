package ua.tpetrenko.esp.core.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import ua.tpetrenko.esp.api.dto.ProductItemDto;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;
import ua.tpetrenko.esp.core.mappers.ProductItemsMapper;
import ua.tpetrenko.esp.core.model.City;
import ua.tpetrenko.esp.core.model.MenuItem;
import ua.tpetrenko.esp.core.model.ProductItem;
import ua.tpetrenko.esp.core.model.ProductPrice;
import ua.tpetrenko.esp.core.repository.ProductItemRepository;
import ua.tpetrenko.esp.core.repository.ProductPriceRepository;

/**
 * @author Roman Zdoronok
 */
@Slf4j
public class ProductItemHandlerImpl extends AbstractHandler<ProductItemDto> implements ProductItemHandler {

    private final City city;
    private final MenuItem menuItem;
    private final ProductItemRepository productItemRepository;
    private final ProductPriceRepository productPriceRepository;
    private final ProductItemsMapper productItemsMapper;

    public ProductItemHandlerImpl(City city, MenuItem menuItem, ProductItemRepository productItemRepository, ProductPriceRepository productPriceRepository, ProductItemsMapper productItemsMapper, PlatformTransactionManager transactionManager) {
        super(transactionManager);
        this.city = city;
        this.menuItem = menuItem;
        this.productItemRepository = productItemRepository;
        this.productPriceRepository = productPriceRepository;
        this.productItemsMapper = productItemsMapper;
    }

    @Override
    public void doHandle(ProductItemDto itemDto) {
        ProductItem productItem = productItemRepository.findOneByMenuItemAndUrl(menuItem, itemDto.getUrl())
                                                       .orElseGet(() -> new ProductItem().setMenuItem(menuItem));
        productItem = productItemsMapper.toEntity(productItem, itemDto);
        ProductItem updatedItem = productItemRepository.save(productItem);
        ProductPrice productPrice = productPriceRepository.findOneByProductItemAndCity(productItem, city)
                                                          .orElseGet(() -> new ProductPrice()
                                                              .setProductItem(updatedItem)
                                                              .setCity(city));
        productPrice.setPrice(itemDto.getPrice());
        productPriceRepository.save(productPrice);
    }
}
