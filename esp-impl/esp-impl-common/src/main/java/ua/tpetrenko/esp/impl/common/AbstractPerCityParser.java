package ua.tpetrenko.esp.impl.common;

import ua.tpetrenko.esp.api.dto.CityDto;
import ua.tpetrenko.esp.api.dto.MenuItemDto;
import ua.tpetrenko.esp.api.handlers.CityHandler;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;
import ua.tpetrenko.esp.api.iterators.CategoryIterator;
import ua.tpetrenko.esp.api.iterators.Iterator.Pair;
import ua.tpetrenko.esp.api.parser.ParserContext;

/**
 * @author Roman Zdoronok
 */
public abstract class AbstractPerCityParser extends AbstractParser {
    protected abstract void prepareCity(CityDto cityDto);
    protected abstract void parseCities(CityHandler cityHandler);

    @Override
    protected void parserMain(ParserContext context) {
        String parserName="";
        try {
            parserName = getMarketInfo().getName();
            log.info("{}: Подготовка парсера...", parserName);
            prepareParser();
            log.info("{}: Подготовка парсера завершена.", parserName);
            log.info("{}: Парсим главное меню...", parserName);
            parseMainMenu(context.getMenuItemHandler());
            log.info("{}: Главное меню успешно распарсили.", parserName);
            log.info("{}: Парсим доступные города...", parserName);
            parseCities(context.getCityHandler());
            log.info("{}: Города успешно распарсили.", parserName);
            log.info("{}: Парсим товары...", parserName);
            parseProductItems(context);
            log.info("{}: Товары успешно распарсили.", parserName);
        }
        catch (Exception e) {
            log.error("Возникла ошибка при парсинге " + parserName, e);
        }
        finally {
            try  {
                log.info("{}: Освобождаем ресурсы...", parserName);
                destroyParser();
                log.info("{}: Ресурсы освобождены.", parserName);
            }
            catch (Exception e) {
                log.error("Возникла ошибка освобождении ресурсов " + parserName, e);
            }
        }
    }

    @Override
    protected void parseProductItems(ParserContext context) {
        context.getCategoryPerCityIterator().forEachPage(cities -> {
            for (Pair<CityDto, CategoryIterator> city : cities) {
                prepareCity(city.getItem());
                city.getHandler().forEachPage(categories -> {
                    for (Pair<MenuItemDto, ProductItemHandler> category : categories) {
                        parseCategory(city.getItem(), category.getItem(), category.getHandler());
                    }
                });
            }
        });
    }

    protected abstract void parseCategory(CityDto cityDto, MenuItemDto menuItemDto, ProductItemHandler productItemHandler);
}
