package ua.tpetrenko.esp.core.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ua.tpetrenko.esp.api.dto.ProductItemDto;
import ua.tpetrenko.esp.core.model.ProductItem;

/**
 * @author Roman Zdoronok
 */
@Mapper(componentModel = "spring")
public interface ProductItemsMapper {
    ProductItem toEntity(ProductItemDto productItemDto);
    ProductItem toEntity(@MappingTarget ProductItem productItem, ProductItemDto productItemDto);
    ProductItemDto toDto(ProductItem productItem);
}
