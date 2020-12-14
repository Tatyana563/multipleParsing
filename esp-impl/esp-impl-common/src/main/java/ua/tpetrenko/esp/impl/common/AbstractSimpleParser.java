package ua.tpetrenko.esp.impl.common;

import ua.tpetrenko.esp.api.dto.MenuItemDto;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;
import ua.tpetrenko.esp.api.parser.ParserContext;

/**
 * @author Roman Zdoronok
 */
public abstract class AbstractSimpleParser extends AbstractParser {

    @Override
    protected final void parserMain(ParserContext context) {
        String parserName="";
        try {
            parserName = getMarketInfo().getName();
            log.info("{}: Подготовка парсера...", parserName);
            prepareParser();
            log.info("{}: Подготовка парсера завершена.", parserName);
            log.info("{}: Парсим главное меню...", parserName);
            parseMainMenu(context.getMenuItemHandler());
            log.info("{}: Главное меню успешно распарсили.", parserName);
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
    protected final void parseProductItems(ParserContext context) {
        context.getCategoryIterator()
               .forEachPage(pairs -> pairs.forEach(pair -> parseCategory(pair.getItem(), pair.getHandler())));
    }

    protected abstract void parseCategory(MenuItemDto menuItemDto, ProductItemHandler productItemHandler);
}
