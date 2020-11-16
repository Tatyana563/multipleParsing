package ua.tpetrenko.esp.impl.sulpak;

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
import java.util.Set;

//@Slf4j
@Component
public class SulpakParser implements DifferentItemsPerCityMarketParser {
    private static Logger log = LoggerFactory.getLogger("SULPAKLOGGER");
    private static final MarketInfo INFO = new MarketInfo("Sulpak", "https://www.sulpak.kz/");
    private static final Set<String> SECTIONS = Set.of("Телефоны и гаджеты", "Теле и аудио техника", "Ноутбуки и компьютеры", "Фото и видео техника",
            "Игры и развлечения", "Техника для дома", "Техника для кухни", "Встраиваемая техника");
    private static final Set<String> GROUPS_EXCEPTIONS = Set.of("Купить дешевле");
    private Document rootPage;

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
        Elements sectionElements = indexPage.select(".catalog-category-item a");
        for (Element sectionElement : sectionElements) {
            String text = sectionElement.text();
            if (SECTIONS.contains(text)) {
                log.info("Получаем {}...", text);
                String sectionUrl = sectionElement.absUrl("href");
                MenuItemDto sectionItem = new MenuItemDto(text, sectionUrl);
                MenuItemHandler groupHandler = sectionHandler.handleSubMenu(sectionItem);

                Document groupPage = Jsoup.connect(sectionUrl).get();
                log.info("Получили {}, ищем группы...", text);
                Elements groupElements = groupPage.select(".portal-menu-title a");
                for (Element groupElement : groupElements) {
                    String groupUrl = groupElement.absUrl("href");
                    String groupText = groupElement.text();
                    log.info("Группа  {}", groupText);
                    if (!GROUPS_EXCEPTIONS.contains(groupText)) {
                        MenuItemDto groupItem = new MenuItemDto(groupText, groupUrl);
                        log.info("Группа  {}", groupText);
                        MenuItemHandler categoryHandler = groupHandler.handleSubMenu(groupItem);
                        Document categoryPage = Jsoup.connect(groupUrl).get();
                        Elements categoryElements = categoryPage.select(".portal-parts-list a");
                        for (Element categoryElement : categoryElements) {
                            String categoryLink = categoryElement.absUrl("href");
                            String categoryText = categoryElement.text();
                            log.info("\tКатегория  {}", categoryText);
                            MenuItemDto categoryItem = new MenuItemDto(categoryText, categoryLink);
                            log.info("\tКатегория  {}", categoryText);
                            categoryHandler.handleSubMenu(categoryItem);
                        }
                    }
                }
            }
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
