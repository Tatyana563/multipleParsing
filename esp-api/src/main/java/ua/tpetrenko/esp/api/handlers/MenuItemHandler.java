package ua.tpetrenko.esp.api.handlers;


import ua.tpetrenko.esp.api.dto.MenuItemDto;

/**
 * @author Roman Zdoronok
 */
public interface MenuItemHandler extends EntityHandler<MenuItemDto> {

    MenuItemHandler handleSubMenu(MenuItemDto menuItem);
}
