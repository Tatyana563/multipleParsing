package ua.tpetrenko.esp.impl.mechta;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ua.tpetrenko.esp.api.dto.CityDto;
import ua.tpetrenko.esp.api.dto.MenuItemDto;
import ua.tpetrenko.esp.api.parser.DifferentItemsPerCityMarketParser;
import ua.tpetrenko.esp.api.dto.MarketInfo;
import ua.tpetrenko.esp.api.handlers.CityHandler;
import ua.tpetrenko.esp.api.handlers.MenuItemHandler;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;
import ua.tpetrenko.esp.impl.mechta.properties.MechtaProperties;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

//@Slf4j
@Component
public class MechtaParser implements DifferentItemsPerCityMarketParser {
    private static Logger log = LoggerFactory.getLogger(MechtaParser.class);
    public static final MarketInfo INFO = new MarketInfo("Mechta.kz", "https://www.mechta.kz/");
    private Document rootPage;
    private WebDriver webDriver;
    private final MechtaProperties mechtaProperties;

    public MechtaParser(MechtaProperties mechtaProperties) {
        this.mechtaProperties = mechtaProperties;
    }

    @Autowired
    private RestTemplate restTemplate;


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
        Elements sectionElements = indexPage.select(".header-first-menu .aa_hm_items_main");
        for (Element sectionElement : sectionElements) {
            Element sectionElementLink = sectionElement.selectFirst(">a");
            String text = sectionElementLink.text();
            if(mechtaProperties.getCategoriesWhitelist().contains(text)) {
                log.info("Получаем {}...", text);
                String sectionUrl = sectionElementLink.absUrl("href");
                MenuItemDto sectionItem = new MenuItemDto(text, sectionUrl);
                MenuItemHandler groupHandler = sectionHandler.handleSubMenu(sectionItem);
                String sectionRel = sectionElement.attr("rel");

                Elements groupElements = indexPage.select(".header-second-menu #jq_aa_th_pod" + sectionRel + " .jq_aa_hm_tab");

                for (Element groupElement : groupElements) {
                    Element groupAnchor = groupElement.selectFirst(".aa_hm_pod2");
                    String groupLink = groupAnchor.absUrl("href");
                    String groupText = groupAnchor.text();

                    log.info("Получаем {}...", text);
                    MenuItemDto groupItem = new MenuItemDto(groupText, groupLink);

                    MenuItemHandler categoryHandler = groupHandler.handleSubMenu(groupItem);

                    Elements categoryElements = groupElement.select(".aa_hm_pod3");
                    for (Element categoryElement : categoryElements) {
                        String categoryLink = categoryElement.absUrl("href");
                        String categoryText = categoryElement.text();

                        log.info("Получаем {}...", text);
                        MenuItemDto categoryItem = new MenuItemDto(categoryText, categoryLink);
                        log.info("\tКатегория  {}", categoryText);
                        categoryHandler.handle(categoryItem);

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

        Elements cityElements = rootPage.select(".aa_htcity_cities > a");
        log.info("Парсим города:");
        for (Element cityElement : cityElements) {
            String cityName = cityElement.text();
            log.info("\t-{}", cityName);
            cityHandler.handle(new CityDto(cityName, cityElement.attr("href")));

        }
    }

    @Override
    public void parseItems(CityDto cityDto, MenuItemDto menuItemDto, ProductItemHandler productItemHandler) throws IOException {
        new SingleCategoryProcessor(cityDto, menuItemDto, productItemHandler, prepareCityCookies(cityDto), restTemplate).run();
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
