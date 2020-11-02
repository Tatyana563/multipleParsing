package ua.tpetrenko.esp.core.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ua.tpetrenko.esp.api.dto.MenuItemDto;
import ua.tpetrenko.esp.core.model.MenuItem;

/**
 * @author Roman Zdoronok
 */
@Mapper(componentModel = "spring")
public interface MenuItemsMapper {
    MenuItem toEntity(MenuItemDto menuItemDto);
    MenuItemDto toDto(MenuItem menuItemDto);
}
