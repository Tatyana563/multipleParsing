package ua.tpetrenko.esp.api.iterators;

import ua.tpetrenko.esp.api.dto.MenuItemDto;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;

/**
 * @author Roman Zdoronok
 */
public interface CategoryIterator extends Iterator<MenuItemDto, ProductItemHandler> {
    //Marker interface.
}
