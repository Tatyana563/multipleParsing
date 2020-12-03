package ua.tpetrenko.esp.impl.fora;

import lombok.RequiredArgsConstructor;
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
import ua.tpetrenko.esp.api.dto.MarketInfo;
import ua.tpetrenko.esp.api.dto.MenuItemDto;
import ua.tpetrenko.esp.api.handlers.CityHandler;
import ua.tpetrenko.esp.api.handlers.MenuItemHandler;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;
import ua.tpetrenko.esp.api.parser.DifferentItemsPerCityMarketParser;
import ua.tpetrenko.esp.impl.fora.properties.ForaProperties;

import java.io.IOException;
import java.util.*;

//@Slf4j
@Component
@RequiredArgsConstructor
public class ForaParser implements DifferentItemsPerCityMarketParser {
    private static Logger log = LoggerFactory.getLogger(ForaParser.class);
    private static final MarketInfo INFO = new MarketInfo("Fora.kz", "https://fora.kz/");
    @Autowired
    private final ForaProperties foraProperties;

    private Document rootPage;

    @Override
    public MarketInfo getMarketInfo() {
        return INFO;
    }

    @Override
    public boolean isEnabled() {
        return foraProperties.isEnabled();
    }

    @Override
    public void prepareParser() throws Exception {
        log.info("Получаем главную страницу...");
        rootPage = Jsoup.connect(INFO.getUrl()).get();
        log.info("Готово.");
    }

    @Override
    public void parseMainMenu(MenuItemHandler sectionHandler) {

        if (rootPage == null) {
            throw new IllegalStateException("Не была получена главная страница");
        }

        //TODO: filter 'enabled' sections
        Elements sectionElements = rootPage.select("#js-categories-menu>li");
        log.info("Найдено {} секций.", sectionElements.size());
        for (Element sectionElement : sectionElements) {
            Element sectionElementLink = sectionElement.selectFirst(">a");
            String text = sectionElementLink.text();
            if (Arrays.asList(foraProperties.getCategoriesWhitelist()).contains(text)) {
                String sectionUrl = sectionElementLink.absUrl("href");
                String sectionUrlWithoutCity = URLUtil.removeCityFromUrl(sectionUrl);

                log.info("-{}", text);

                MenuItemDto sectionItem = new MenuItemDto(text, sectionUrlWithoutCity);
                MenuItemHandler groupHandler = sectionHandler.handleSubMenu(sectionItem);
                Elements groupsAndCategories = sectionElement.select(".category-submenu li");
                Element currentGroup = null;
                List<Element> categories = new ArrayList<>();
                for (Element element : groupsAndCategories) {
                    if (element.hasClass("parent-category")) {
                        // element is group
                        // 1. process previously found group and categories
                        // 2. reset group and list
                        processGroupWithCategories(groupHandler, currentGroup, categories);
                        currentGroup = element;
                        categories.clear();
                    } else {
                        // element is category
                        categories.add(element);
                    }
                }
                processGroupWithCategories(groupHandler, currentGroup, categories);
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
    public void parseItems(CityDto cityDto, MenuItemDto menuItemDto, ProductItemHandler productItemHandler) throws IOException {
        new SingleCategoryProcessor(cityDto, menuItemDto, productItemHandler, prepareCityCookies(cityDto)).run();
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

    private void processGroupWithCategories(MenuItemHandler groupHandler, Element currentGroup, List<Element> categories) {
        if (currentGroup == null) {
            return;
        }
        Element groupLink = currentGroup.selectFirst(">a");

        String groupUrl = groupLink.absUrl("href");
        String groupText = groupLink.text();
        String groupUrlWithoutCity = URLUtil.removeCityFromUrl(groupUrl);
        log.info("\t- {}", groupText);
        MenuItemDto group = new MenuItemDto(groupText, groupUrlWithoutCity);
        if (categories.isEmpty()) {
            groupHandler.handle(group);
        } else {
            MenuItemHandler categoryHandler = groupHandler.handleSubMenu(group);
            for (Element categoryElement : categories) {
                Element categoryLink = categoryElement.selectFirst(">a");
                String categoryUrl = categoryLink.absUrl("href");
                String categoryUrlWithoutCity = URLUtil.removeCityFromUrl(categoryUrl);
                String categoryText = categoryLink.text();
                log.info("\t\t- {}", categoryText);
                MenuItemDto category = new MenuItemDto(categoryText, categoryUrlWithoutCity);
                categoryHandler.handle(category);
            }
        }
    }
}
