package ua.tpetrenko.esp.impl.shopkz;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Roman Zdoronok
 */
//@Slf4j
@Component
public class ShopkzParser implements DifferentItemsPerCityMarketParser {
    private static Logger log = LoggerFactory.getLogger("SHOPKZLOGGER");
    private static final MarketInfo INFO = new MarketInfo("Shop.kz", "https://shop.kz/");
    private Document rootPage;
    private static final Set<String> SECTIONS = Set.of("Смартфоны и гаджеты", "Комплектующие", "Ноутбуки и компьютеры", "Компьютерная периферия",
            "Оргтехника и расходные материалы", "Сетевое и серверное оборудование", "Телевизоры, аудио, фото, видео", "Бытовая техника и товары для дома", "Товары для геймеров");

    @Override
    public MarketInfo getMarketInfo() {
        return INFO;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void prepareParser() throws IOException {
        log.info("Получаем главную страницу...");
        rootPage = Jsoup.connect(INFO.getUrl()).get();
        log.info("Готово.");
        log.info("Подготовка " + getMarketInfo());
    }

    @Override
    public void parseMainMenu(MenuItemHandler sectionHandler) throws IOException {

        if (rootPage == null) {
            throw new IllegalStateException("Не была получена главная страница");
        }
        Document indexPage = Jsoup.connect(INFO.getUrl()).get();
        log.info("Получили главную страницу, ищем секции...");
        Elements sectionElements = indexPage.select(".bx-top-nav-container ul.bx-nav-list-1-lvl li.bx-nav-1-lvl");
        for (Element sectionElement : sectionElements) {
            Element sectionAnchor = sectionElement.selectFirst(">a");
            String text = sectionAnchor.text();
            if (SECTIONS.contains(text)) {
                log.info("Получаем {}...", text);
                String sectionUrl = sectionAnchor.absUrl("href");
                MenuItemDto sectionItem = new MenuItemDto(text, sectionUrl);

                MenuItemHandler groupHandler = sectionHandler.handleSubMenu(sectionItem);

                Elements groups = sectionElement.select("ul.bx-nav-list-2-lvl li.bx-nav-2-lvl");
                for (Element groupElement : groups) {

                    Element groupAnchor = groupElement.selectFirst(">a");
                    String groupText = groupAnchor.text();
                    MenuItemDto groupItem = new MenuItemDto(groupText, "null");
                    log.info("Группа  {}", groupText);

                    MenuItemHandler categoryHandler = groupHandler.handleSubMenu(groupItem);
                    Elements categoryElements = groupElement.select("ul.bx-nav-list-3-lvl li.bx-nav-3-lvl");
                    for (Element categoryElement : categoryElements) {
                        Element categoryAnchor = categoryElement.selectFirst(">a");
                        String categoryLink = categoryAnchor.absUrl("href");
                        String categoryText = categoryAnchor.text();
                        MenuItemDto categoryItem = new MenuItemDto(categoryText, categoryLink);
                        log.info("\tКатегория  {}", categoryText);
                        categoryHandler.handleSubMenu(categoryItem);
                    }
                }
            }
        }
    }

    @Override
    public void parseCities(CityHandler cityHandler) {

        if (rootPage == null) {
            throw new IllegalStateException("Не была получена главная страница");
        }

        Elements cityElements = rootPage.select(".js-city-select-radio");
        log.info("Найдено {} городов", cityElements.size());
        for (Element cityElement : cityElements) {
            String citySuffix = cityElement.attr("data-href").replace("/", "");
            String cityName = cityElement.parent().selectFirst("label > a").text();

            log.info("-{}", cityName);

            cityHandler.handle(new CityDto(cityName, citySuffix));
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
