package ua.tpetrenko.esp.impl.sulpak;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.stereotype.Component;
import ua.tpetrenko.esp.api.dto.CityDto;
import ua.tpetrenko.esp.api.dto.MenuItemDto;
import ua.tpetrenko.esp.api.parser.DifferentItemsPerCityMarketParser;
import ua.tpetrenko.esp.api.dto.MarketInfo;
import ua.tpetrenko.esp.api.handlers.CityHandler;
import ua.tpetrenko.esp.api.handlers.MenuItemHandler;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;
import ua.tpetrenko.esp.impl.sulpak.properties.SulpakProperties;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class SulpakParser implements DifferentItemsPerCityMarketParser {
    private static final MarketInfo INFO = new MarketInfo("Sulpak", "https://www.sulpak.kz/");

    private final SulpakProperties sulpakProperties;

    private Document rootPage;

    @Override
    public MarketInfo getMarketInfo() {
        return INFO;
    }

    @Override
    public boolean isEnabled() {
        return sulpakProperties.isEnabled();
    }

    private WebDriver driver = null;

    @Override
    public void prepareParser() throws IOException {
        log.info("Инициализируем webDriver...");
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.setBinary(sulpakProperties.getChrome().getPath());
//        options.addArguments("--headless");
        options.addArguments("window-size=1920x1080");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        log.info("Получаем главную страницу...");
        driver.get(INFO.getUrl());
        log.info("Готово.");
        Actions action = new Actions(driver);
        action.moveToElement(driver.findElement(By.cssSelector(".cart-block")));

        for (int i = 0; i < 10; i++) {
            action.moveByOffset(0, 1);
        }

        log.info("Выполняем перемещение мышки..");
        action.perform();
        log.info("Завершиии перемещение мышки");
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(200));

        try {
            String yesButton = "button#location-window-button-yes";
            log.info("Ожидаем появления заветной кнопки");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(yesButton)));
            log.info("Дождались кнопку");
            driver.findElement(By.cssSelector(yesButton)).click();
        } catch (Exception e) {
            log.warn("Не дождались модального окна выбора города", e);
        }
        rootPage = Jsoup.parse(driver.getPageSource());
    }

    @Override
    public void parseMainMenu(MenuItemHandler sectionHandler) throws IOException {

        if (rootPage == null) {
            throw new IllegalStateException("Не была получена главная страница");
        }
        Document indexPage = loadDocument(INFO.getUrl());
        log.info("Получили главную страницу, ищем секции...");
        Elements sectionElements = indexPage.select(".catalog-category-item a");
        for (Element sectionElement : sectionElements) {
            String text = sectionElement.text();
            if(sulpakProperties.getCategoriesWhitelist().contains(text)){
                log.info("Получаем {}...", text);
                String sectionUrl = sectionElement.absUrl("href");
                MenuItemDto sectionItem = new MenuItemDto(text, sectionUrl);
                MenuItemHandler groupHandler = sectionHandler.handleSubMenu(sectionItem);

                Document groupPage = loadDocument(sectionUrl);
                log.info("Получили {}, ищем группы...", text);
                Elements groupElements = groupPage.select(".portal-menu-title a");
                for (Element groupElement : groupElements) {
                    String groupUrl = groupElement.absUrl("href");
                    String groupText = groupElement.text();
                    if (!sulpakProperties.getCategoriesBlacklist().contains(groupText)) {
                        MenuItemDto groupItem = new MenuItemDto(groupText, groupUrl);
                        log.info("Группа  {}", groupText);
                        MenuItemHandler categoryHandler = groupHandler.handleSubMenu(groupItem);
                        //TODO: use categories from groups page. Do not load another page.
                        Document categoryPage = loadDocument(groupUrl);
                        Elements categoryElements = categoryPage.select(".portal-menu-items>a");
                        for (Element categoryElement : categoryElements) {
                            String categoryLink = categoryElement.absUrl("href");
                            String categoryText = categoryElement.text();
                            log.info("\tКатегория  {}", categoryText);
                            MenuItemDto categoryItem = new MenuItemDto(categoryText, categoryLink);
                            categoryHandler.handleSubMenu(categoryItem);

                        }
                    }
                }
            }
        }
    }

    @Override
    public void parseCities(CityHandler cityHandler) {
        //TODO: correct selector
        Elements cityElements = rootPage.select(".cities-map-block>ul>li");
        log.info("Парсим города:");
        for (Element cityElement : cityElements) {
            String cityName = cityElement.text();
            log.info("\t-{}", cityName);
            cityHandler.handle(new CityDto(cityName, cityElement.attr("data-id")));
        }
    }


    private Map<String, String> prepareCityCookies(CityDto cityDto) throws IOException {
        log.info("Готовим cookies для города {}", cityDto.getName());
        //TODO: use webdriver to change current city (copy from technodom ?)
        //Next - convert set cookies to map<string, string> - <name, value>
        return driver.manage().getCookies().stream().collect(Collectors.toMap(Cookie::getName, Cookie::getValue));
//        Map<String, String> cookies = new HashMap<>();
//        String urlWithCity = String.format("%s%s%s", INFO.getUrl(), pathPart, cityDto.getUrl());
//        Connection.Response response = Jsoup.connect(urlWithCity)
//                .cookies(cookies)
//                .method(Connection.Method.GET)
//                .execute();
//        cookies.putAll(response.cookies());
//        return cookies;
    }

    /*
    Jsoup.connect("https://www.sulpak.kz/Home/SetCity")
        .requestBody("{\"cityId\":60,\"params\":{\"controller\":\"Home\",\"action\":\"Index\",\"cityName\":\"zharkent\"},\"path\":\"/\"}")
//        .cookies(cookies)
                .method(Connection.Method.POST)
        .execute()*/

    @Override
    public void parseItems(CityDto cityDto, MenuItemDto menuItemDto, ProductItemHandler productItemHandler) throws IOException {
        new SingleCategoryProcessor(cityDto, menuItemDto, productItemHandler, prepareCityCookies(cityDto)).run();
    }

    @Override
    public void destroyParser() {
        if (driver != null) {
            driver.quit();
        }
    }

    private Document loadDocument(String url) throws IOException {
        return Jsoup.parse(new URL(url), (int) sulpakProperties.getConnection().getReadTimeoutMs().toMillis());
    }
}
