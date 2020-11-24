package ua.tpetrenko.esp.impl.mechta;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.tpetrenko.esp.api.dto.CityDto;
import ua.tpetrenko.esp.api.dto.MenuItemDto;
import ua.tpetrenko.esp.api.parser.DifferentItemsPerCityMarketParser;
import ua.tpetrenko.esp.api.dto.MarketInfo;
import ua.tpetrenko.esp.api.handlers.CityHandler;
import ua.tpetrenko.esp.api.handlers.MenuItemHandler;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;
import ua.tpetrenko.esp.impl.mechta.properties.MechtaProperties;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//@Slf4j
@Component
public class MechtaParser implements DifferentItemsPerCityMarketParser {
    private static Logger log = LoggerFactory.getLogger(MechtaParser.class);
    private static final MarketInfo INFO = new MarketInfo("Mechta.kz", "https://www.mechta.kz/");
    private static final Set<String> SECTIONS = Set.of("Смартфоны и гаджеты", "Ноутбуки и компьютеры", "Тв, аудио, видео",
            "Техника для дома", "Климат техника", "Кухонная техника", "Встраиваемая техника", "Фото и видео техника", "Игровые приставки и игрушки", "Активный отдых");
    private Document rootPage;
    @Autowired
    private MechtaProperties mechtaProperties;

    @Override
    public MarketInfo getMarketInfo() {
        return INFO;
    }

    @Override
    public boolean isEnabled() {
        return mechtaProperties.isEnabled();
    }

    @Override
    public void prepareParser() throws Exception {
        log.info("Получаем главную страницу...");
        rootPage = Jsoup.connect(INFO.getUrl()).get();
        log.info("Готово.");
    }

    @Override
    public void parseMainMenu(MenuItemHandler sectionHandler) throws IOException {

        if (rootPage == null) {
            throw new IllegalStateException("Не была получена главная страница");
        }
        Document indexPage = Jsoup.connect(INFO.getUrl()).get();
        log.info("Получили главную страницу, ищем секции...");
        Elements sectionElements = indexPage.select(".aa_hm_items_main");
        for (Element sectionElement : sectionElements) {
            Element sectionElementLink = sectionElement.selectFirst(">a");
            String text = sectionElementLink.text();
            if (SECTIONS.contains(text)) {
                log.info("Получаем {}...", text);
                String sectionUrl = sectionElementLink.absUrl("href");
                MenuItemDto sectionItem = new MenuItemDto(text, sectionUrl);
                MenuItemHandler groupHandler = sectionHandler.handleSubMenu(sectionItem);

                Elements groupElements = sectionElement.select(".aa_hm_pod2");

                for (Element groupElement : groupElements) {
                    String groupLink = groupElement.selectFirst("a").absUrl("href");
                    String groupText = groupElement.selectFirst("a").text();

                    log.info("Получаем {}...", text);
                    MenuItemDto groupItem = new MenuItemDto(groupText, groupLink);

                    MenuItemHandler categoryHandler = groupHandler.handleSubMenu(groupItem);

                    Elements categoryElements = groupElement.select(".aa_hm_pod3");
                    for (Element categoryElement : categoryElements) {
                        String categoryLink = categoryElement.selectFirst("a").absUrl("href");
                        String categoryText = categoryElement.selectFirst("a").text();

                        log.info("Получаем {}...", text);
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

    }

    @Override
    public void parseItems(CityDto cityDto, MenuItemDto menuItemDto, ProductItemHandler productItemHandler) {
        // Nothing to do.
    }

    @Override
    public void destroyParser() {
        // Nothing to do.
    }

    private Map<String, String> prepareCityCookies(CityDto cityDto) throws IOException {
        log.info("Готовим cookies для города {}", cityDto.getName());
        Map<String, String> cookies = new HashMap<>();
        String urlWithCity = INFO.getUrl() + cityDto.getUrl();
        Connection.Response response = Jsoup.connect(urlWithCity)
                .cookies(cookies)
                .method(Connection.Method.GET)
                .execute();
        cookies.putAll(response.cookies());
        return cookies;
    }
}
