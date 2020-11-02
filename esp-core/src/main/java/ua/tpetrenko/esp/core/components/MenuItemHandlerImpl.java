package ua.tpetrenko.esp.core.components;

import lombok.RequiredArgsConstructor;
import ua.tpetrenko.esp.api.dto.MenuItemDto;
import ua.tpetrenko.esp.api.handlers.MenuItemHandler;
import ua.tpetrenko.esp.core.mappers.MenuItemsMapper;
import ua.tpetrenko.esp.core.model.Market;
import ua.tpetrenko.esp.core.model.MenuItem;
import ua.tpetrenko.esp.core.repository.MenuItemRepository;

/**
 * @author Roman Zdoronok
 */
@RequiredArgsConstructor
public class MenuItemHandlerImpl implements MenuItemHandler {

    private final MenuItemRepository menuItemRepository;
    private final MenuItemsMapper menuItemsMapper;
    private final Market market;
    private final MenuItem parentMenuItem;

    @Override
    public MenuItemHandler handleSubMenu(MenuItemDto menuItemDto) {
        MenuItem menuItem = findOrCreateMenuItem(menuItemDto);
        return new MenuItemHandlerImpl(menuItemRepository, menuItemsMapper, market, menuItem);
    }

    @Override
    public void handle(MenuItemDto menuItemDto) {
        checkOrCreateMenuItem(menuItemDto);
    }

    private void checkOrCreateMenuItem(MenuItemDto menuItemDto) {
        if (!menuItemRepository.existsByNameAndParentItemAndMarket(menuItemDto.getName(), parentMenuItem, market)) {
            createNewMenuItem(menuItemDto);
        }
    }

    private MenuItem findOrCreateMenuItem(MenuItemDto menuItemDto) {
        return menuItemRepository.findOneByNameAndParentItemAndMarket(menuItemDto.getName(), parentMenuItem, market)
                                 .orElseGet(() -> createNewMenuItem(menuItemDto));
    }

    private MenuItem createNewMenuItem(MenuItemDto menuItemDto) {
        MenuItem newMenuItem = menuItemsMapper.toEntity(menuItemDto);
        newMenuItem.setMarket(market);
        newMenuItem.setParentItem(parentMenuItem);
        return menuItemRepository.save(newMenuItem);
    }
}
