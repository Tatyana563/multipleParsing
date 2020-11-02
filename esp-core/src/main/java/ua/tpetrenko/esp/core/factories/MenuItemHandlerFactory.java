package ua.tpetrenko.esp.core.factories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.tpetrenko.esp.api.handlers.MenuItemHandler;
import ua.tpetrenko.esp.core.components.MenuItemHandlerImpl;
import ua.tpetrenko.esp.core.mappers.MenuItemsMapper;
import ua.tpetrenko.esp.core.model.Market;
import ua.tpetrenko.esp.core.model.MenuItem;
import ua.tpetrenko.esp.core.repository.MenuItemRepository;

/**
 * @author Roman Zdoronok
 */
@Component
@RequiredArgsConstructor
public class MenuItemHandlerFactory {
    private final MenuItemRepository menuItemRepository;
    private final MenuItemsMapper menuItemsMapper;

    public MenuItemHandler getMenuItemHandler(Market market) {
        return getMenuItemHandler(market, null);
    }

    public MenuItemHandler getMenuItemHandler(Market market, MenuItem parentMenuItem) {
        return new MenuItemHandlerImpl(menuItemRepository, menuItemsMapper, market, parentMenuItem);
    }

}
