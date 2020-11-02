package ua.tpetrenko.esp.fora;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import ua.tpetrenko.esp.api.dto.MenuItemDto;
import ua.tpetrenko.esp.api.parser.MarketParser;
import ua.tpetrenko.esp.api.dto.MarketInfo;
import ua.tpetrenko.esp.api.handlers.CityHandler;
import ua.tpetrenko.esp.api.handlers.MenuItemHandler;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;

@Slf4j
@Component
public class ForaParser implements MarketParser {

    private static final MarketInfo INFO = new MarketInfo("Fora", "https://fora.kz/");

    @Override
    public MarketInfo getMarketInfo() {
        return INFO;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void prepareParser() {
        // Nothing to do.
    }

    @Override
    public void parseMainMenu(MenuItemHandler sectionHandler) throws Exception {
        Document newsPage = Jsoup.connect(INFO.getUrl()).get();
        log.info("Получили главную страницу, ищем секции...");
        Elements sectionElements = newsPage.select("#js-categories-menu>li");
        log.info("Найдено {} секций.", sectionElements.size());
        for (Element sectionElement : sectionElements) {
            Element sectionElementLink = sectionElement.selectFirst(">a");
            String sectionUrl = sectionElementLink.absUrl("href");
            String sectionUrlWithoutCity = URLUtil.removeCityFromUrl(sectionUrl);
            String text = sectionElementLink.text();
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

    @Override
    public void parseCities(CityHandler cityHandler) {
        // Nothing to do.
    }

    @Override
    public void parseItems(ProductItemHandler productItemHandler) {
        // Nothing to do.
    }

    @Override
    public void destroyParser() {
        // Nothing to do.
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
        MenuItemHandler categoryHandler = groupHandler.handleSubMenu(group);
        if (categories.isEmpty()) {
            categoryHandler.handle(group);
        } else {
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
