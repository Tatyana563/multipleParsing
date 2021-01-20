package ua.tpetrenko.esp.impl.mechta;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ua.tpetrenko.esp.api.dto.CityDto;
import ua.tpetrenko.esp.api.dto.MarketInfo;
import ua.tpetrenko.esp.api.dto.MenuItemDto;
import ua.tpetrenko.esp.api.handlers.CityHandler;
import ua.tpetrenko.esp.api.handlers.MenuItemHandler;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;
import ua.tpetrenko.esp.api.parser.DifferentItemsPerCityMarketParser;
import ua.tpetrenko.esp.impl.mechta.properties.MechtaProperties;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class MechtaParser implements DifferentItemsPerCityMarketParser {
    public static final MarketInfo INFO = new MarketInfo("Mechta.kz", "https://www.mechta.kz/");

    private final MechtaProperties mechtaProperties;
    private final RestTemplate restTemplate;

    private Document rootPage;
    private WebDriver webDriver;

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

        log.info("Инициализируем webDriver...");
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        //       options.setBinary(mechtaProperties.getChrome().getPath());
//        options.addArguments("--headless");
        options.addArguments("window-size=1920x1080");
        webDriver = new ChromeDriver(options);
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        log.info("Получаем главную страницу...");
        webDriver.get(INFO.getUrl());
        //TODO: wait for select cities popup. Close it

        Wait<WebDriver> wait = new FluentWait<>(webDriver)
                .withMessage("City popup not found")
                .withTimeout(Duration.ofSeconds(120))
                .pollingEvery(Duration.ofMillis(200));

        try {
            log.info("Ожидаем доступности модального окна выбора городов");
            String cityButton = ".aa_htcity_items_first .aa_htc_item";
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(cityButton)));
            List<WebElement> cityButtons = webDriver.findElements(By.cssSelector(cityButton));
            for (WebElement button : cityButtons) {
                if (button.getText().contains("Нур-Султан")) {
                    button.click();
                    return;
                }
            }
        } catch (Exception e) {
            log.warn("Не дождались модального окна выбора города", e);
        }
        log.info("Закрываем модальное окноб выбрали Нур-Султан");
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
            if (mechtaProperties.getCategoriesWhitelist().contains(text)) {
                log.info("Получаем секцию {}...", text);
                String sectionUrl = sectionElementLink.absUrl("href");
                MenuItemDto sectionItem = new MenuItemDto(text, sectionUrl);
                MenuItemHandler groupHandler = sectionHandler.handleSubMenu(sectionItem);
                String sectionRel = sectionElement.attr("rel");

                Elements groupElements = indexPage.select(".header-second-menu #jq_aa_th_pod" + sectionRel + " .jq_aa_hm_tab");

                for (Element groupElement : groupElements) {
                    Element groupAnchor = groupElement.selectFirst(".aa_hm_pod2");
                    String groupLink = groupAnchor.absUrl("href");
                    String groupText = groupAnchor.text();

                    log.info("Получаем группу {}...", text);
                    MenuItemDto groupItem = new MenuItemDto(groupText, groupLink);

                    MenuItemHandler categoryHandler = groupHandler.handleSubMenu(groupItem);

                    Elements categoryElements = groupElement.select(".aa_hm_pod3");
                    for (Element categoryElement : categoryElements) {
                        String categoryLink = categoryElement.absUrl("href");
                        String categoryText = categoryElement.text();

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
            cityHandler.handle(new CityDto(cityName, cityElement.text()));

        }
    }

    @Override
    public void parseItems(CityDto cityDto, MenuItemDto menuItemDto, ProductItemHandler productItemHandler) throws IOException {
        new SingleCategoryProcessor(cityDto, menuItemDto, productItemHandler, webDriver, restTemplate).run();
    }

    @Override
    public void destroyParser() {
        // Nothing to do.
    }

//    private Map<String, String> prepareCityCookies(CityDto cityDto) throws IOException {
//        log.info("Готовим cookies для города {}", cityDto.getName());
//        Map<String, String> cookies = new HashMap<>();
//        String urlWithCity = INFO.getUrl() + cityDto.getUrl();
//        Connection.Response response = Jsoup.connect(urlWithCity)
//                .cookies(cookies)
//                .method(Connection.Method.GET)
//                .execute();
//        cookies.putAll(response.cookies());
//        return cookies;
//    }
}
