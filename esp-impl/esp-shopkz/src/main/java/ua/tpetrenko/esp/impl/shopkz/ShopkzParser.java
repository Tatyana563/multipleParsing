package ua.tpetrenko.esp.impl.shopkz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import ua.tpetrenko.esp.api.dto.MenuItemDto;
import ua.tpetrenko.esp.api.dto.MarketInfo;
import ua.tpetrenko.esp.api.handlers.MenuItemHandler;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;
import ua.tpetrenko.esp.api.parser.SimpleMarketParser;
import ua.tpetrenko.esp.impl.shopkz.properties.ShopkzProperties;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author Roman Zdoronok
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ShopkzParser implements SimpleMarketParser {
    public static final MarketInfo INFO = new MarketInfo("Shop.kz", "https://shop.kz/");

    private final ShopkzProperties shopkzProperties;

    private Document rootPage;

    @Override
    public MarketInfo getMarketInfo() {
        return INFO;
    }

    @Override
    public boolean isEnabled() {
        return shopkzProperties.isEnabled();
    }

    @Override
    public void prepareParser() throws IOException {
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
        Elements sectionElements = indexPage.select(".bx-top-nav-container ul.bx-nav-list-1-lvl li.bx-nav-1-lvl");
        for (Element sectionElement : sectionElements) {
            Element sectionAnchor = sectionElement.selectFirst(">a");
            String text = sectionAnchor.text();
            if (shopkzProperties.getCategoriesWhitelist().contains(text)) {
                log.info("Получаем {}...", text);
                String sectionUrl = sectionAnchor.absUrl("href");
                MenuItemDto sectionItem = new MenuItemDto(text, sectionUrl);

                MenuItemHandler groupHandler = sectionHandler.handleSubMenu(sectionItem);

                Elements groups = sectionElement.select("ul.bx-nav-list-2-lvl li.bx-nav-2-lvl");
                for (Element groupElement : groups) {

                    Element groupAnchor = groupElement.selectFirst(">a");
                    String groupText = groupAnchor.text();
                    MenuItemDto groupItem = new MenuItemDto(groupText, null);
                    log.info("Группа  {}", groupText);

                    MenuItemHandler categoryHandler = groupHandler.handleSubMenu(groupItem);
                    Elements categoryElements = groupElement.select("ul.bx-nav-list-3-lvl li.bx-nav-3-lvl");
                    for (Element categoryElement : categoryElements) {
                        Element categoryAnchor = categoryElement.selectFirst(">a");
                        String categoryLink = categoryAnchor.absUrl("href");
                        String categoryText = categoryAnchor.text();
                        MenuItemDto categoryItem = new MenuItemDto(categoryText, categoryLink);
                        log.info("\tКатегория  {}", categoryText);
                        categoryHandler.handle(categoryItem);
                    }
                }
            }
        }
    }

    @Override
    public void destroyParser() {

    }

    @Override
    public void parseItems(MenuItemDto menuItemDto, ProductItemHandler productItemHandler, CountDownLatch latch) {
        new SingleCategoryProcessor(menuItemDto, productItemHandler, latch).run();
    }
}
//  @Override
//    public void parseItems(CityDto cityDto, MenuItemDto menuItemDto, ProductItemHandler productItemHandler) throws IOException {
//        new SingleCategoryProcessor(cityDto, menuItemDto, productItemHandler, prepareCityCookies(cityDto)).run();
//    }