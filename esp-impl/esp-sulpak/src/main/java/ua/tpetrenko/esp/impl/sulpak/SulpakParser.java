package ua.tpetrenko.esp.impl.sulpak;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ua.tpetrenko.esp.api.dto.CityDto;
import ua.tpetrenko.esp.api.dto.MenuItemDto;
import ua.tpetrenko.esp.api.parser.DifferentItemsPerCityMarketParser;
import ua.tpetrenko.esp.api.dto.MarketInfo;
import ua.tpetrenko.esp.api.handlers.CityHandler;
import ua.tpetrenko.esp.api.handlers.MenuItemHandler;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;

//@Slf4j
@Component
public class SulpakParser implements DifferentItemsPerCityMarketParser {
    private static Logger log = LoggerFactory.getLogger("SULPAKLOGGER");
    private static final MarketInfo INFO = new MarketInfo("Sulpak", "https://www.sulpak.kz/");

    private Document rootPage;

    @Override
    public MarketInfo getMarketInfo() {
        return INFO;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void prepareParser() throws Exception {
        log.info("Получаем главную страницу...");
        rootPage = Jsoup.connect(INFO.getUrl()).get();
        log.info("Готово.");
    }

    @Override
    public void parseMainMenu(MenuItemHandler sectionHandler) {

        if(rootPage == null) {
            throw new IllegalStateException("Не была получена главная страница");
        }

    }

    @Override
    public void parseCities(CityHandler cityHandler) {

        if(rootPage == null) {
            throw new IllegalStateException("Не была получена главная страница");
        }

    }

    @Override
    public void parseItems(CityDto cityDto, MenuItemDto menuItemDto, ProductItemHandler productItemHandler) {
        // Nothing to do.
    }

    @Override
    public void destroyParser() {
        // Nothing to do.
    }

}
