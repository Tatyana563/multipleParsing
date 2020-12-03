package ua.tpetrenko.esp.impl.technodom;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
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
import ua.tpetrenko.esp.api.parser.DifferentItemsPerCityMarketParserSelenium;
import ua.tpetrenko.esp.impl.technodom.properties.TechnodomProperties;

/**
 * @author Roman Zdoronok
 */
//@Slf4j
@Component
//@RequiredArgsConstructor
public class TechnodomParser implements DifferentItemsPerCityMarketParser{
    private static Logger log = LoggerFactory.getLogger(TechnodomParser.class);
    private static final MarketInfo INFO = new MarketInfo("Technodom", "https://technodom.kz/");
    private static final String CATEGORIES_PAGE = INFO.getUrl() + "all";

    private final TechnodomProperties technodomProperties;
    private final String[] whiteList;

    public TechnodomParser(TechnodomProperties technodomProperties) {
        this.technodomProperties = technodomProperties;
        this.whiteList =technodomProperties.getCategoriesWhitelist();
    }

    private WebDriver webDriver;

    //    @Value("${parser.chrome.path}")
    private String path;

    @Override
    public MarketInfo getMarketInfo() {
        return INFO;
    }

    @Override
    public boolean isEnabled() {
        return technodomProperties.isEnabled();
    }

    @Override
    public void prepareParser() {
        log.info("Подготовка " + getMarketInfo());
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.setBinary(path);
        options.addArguments("--headless");
        options.addArguments("window-size=1920x1080");
        webDriver = new ChromeDriver(options);
    }

    @Override
    public void parseMainMenu(MenuItemHandler menuItemHandler) {
        webDriver.get(Constants.CATEGORIES_URL);
        Wait<WebDriver> wait = new FluentWait<>(webDriver)
                .withMessage("Categories not found")
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(200));

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.CatalogPage-CategorySection")));
        } catch (Exception e) {
            log.error("Не удалось загрузить список категорий", e);
            return;
        }
        long loaded = System.currentTimeMillis();
        Document document = Jsoup.parse(webDriver.getPageSource());
        log.info("Получили главную страницу, ищем секции...");
        Elements sectionElements = document.select("div.CatalogPage-CategorySection");
        for (Element sectionElement : sectionElements) {
            Element sectionTitle = sectionElement.selectFirst("h2.CatalogPage-CategoryTitle");
            String sectionName = sectionTitle.text();
            if (Arrays.asList(whiteList).contains(sectionName)) {
                log.info("Секция: {}", sectionName);
                MenuItemDto sectionItem = new MenuItemDto(sectionName, null);
                MenuItemHandler groupHandler = menuItemHandler.handleSubMenu(sectionItem);

                Elements groupElements = sectionElement.select("div.CatalogPage-Category");
                for (Element groupElement : groupElements) {
                    String groupLink = groupElement.selectFirst("h3.CatalogPage-SubcategoryTitle > a").absUrl("href");
                    String groupTitle = groupElement.selectFirst("h3.CatalogPage-SubcategoryTitle > a").text();
                    log.info("\tГруппа: {}", groupTitle);
                    MenuItemDto groupItem = new MenuItemDto(groupTitle, groupLink);
                    log.info("Группа  {}", groupTitle);
                    MenuItemHandler categoryHandler = groupHandler.handleSubMenu(groupItem);
                    Elements categoryLinks = groupElement.select("li.CatalogPage-Subcategory > a");
                    for (Element categoryLink : categoryLinks) {
                        log.info("\t\tКатегория: {}", categoryLink.text());
                        String categoryText = categoryLink.text();
                        String categoryUrl = categoryLink.absUrl("href");
                        MenuItemDto categoryItem = new MenuItemDto(categoryText, categoryUrl);
                        log.info("\tКатегория  {}", categoryText);
                        categoryHandler.handleSubMenu(categoryItem);
                    }
                }
            }
        }

        parseAllCities(loaded);
    }


    public void parseAllCities(long loaded) {
        checkForModalPanels(loaded);
        Wait<WebDriver> wait = new FluentWait<>(webDriver)
                .withMessage(" City list not found")
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(200));

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".ReactModal__Content.VerifyCityModal")));
        }
        catch (Exception e) {
            log.error("Не удалось загрузить список городов", e);
            return;
        }
        try {
            WebElement citySelectModal = webDriver.findElement(By.cssSelector(".ReactModal__Content.VerifyCityModal"));
            if (citySelectModal.isDisplayed()) {
                List<WebElement> buttons = citySelectModal.findElements(By.cssSelector(".ButtonNext__Text.ButtonNext__Text_Size-L"));
                for (WebElement button : buttons) {
                    if ("да".equalsIgnoreCase(button.getText())) {
                        button.click();
                        break;
                    }
                }
            }
        } catch (NoSuchElementException noSuchElementException) {
            // nothing to do.
        }

        openCitiesPopup();

        Document pageWithCitiesModal = Jsoup.parse(webDriver.getPageSource());
        Elements cityUrls = pageWithCitiesModal.select("a.CitiesModal__List-Item");
        for (Element cityUrl : cityUrls) {
            log.info("Город: {}", cityUrl.text());
            String cityLink = URLUtil.extractCityFromUrl(cityUrl.attr("href"), Constants.ALL_SUFFIX);;
            String cityText = cityUrl.text();
            if (!cityRepository.existsByUrlSuffix(cityLink)) {
                cityRepository.save(new City(cityText, cityLink));
            }
        }

        closeCitiesPopup();
    }


    @Override
    public void parseCities(CityHandler cityHandler) throws Exception {
        cityHandler.handle();
    }

    @Override
    public void parseItems(CityDto cityDto, MenuItemDto menuItemDto, ProductItemHandler productItemHandler) {
        // Nothing to do.
    }

    @Override
    public void destroyParser() {
        if (webDriver != null) {
            webDriver.close();
        }
    }

    public void parseData() {

        webDriver.get(CATEGORIES_PAGE);
        List<WebElement> sectionElements = webDriver.findElements(By.cssSelector("div.CatalogPage-CategorySection"));
        for (WebElement sectionElement : sectionElements) {
            WebElement sectionTitle = sectionElement.findElement(By.cssSelector("h2.CatalogPage-CategoryTitle"));
            log.info("Section: {}", sectionTitle.getAttribute("innerText"));
            List<WebElement> groupElements = sectionElement.findElements(By.cssSelector("div.CatalogPage-Category"));
            for (WebElement groupElement : groupElements) {
                WebElement groupLink = groupElement.findElement(By.cssSelector("h3.CatalogPage-SubcategoryTitle > a"));
                log.info("\tGroup: {}", groupLink.getAttribute("innerText"));
                List<WebElement> categoryLinks = groupElement.findElements(By.cssSelector("li.CatalogPage-Subcategory > a"));
                for (WebElement categoryLink : categoryLinks) {
                    log.info("\t\tCategory: {}", categoryLink.getAttribute("innerText"));
                }
            }

        }
    }
        private void checkForModalPanels(long loaded) {
            long now = System.currentTimeMillis();
            long past = now - loaded;
            long left = technodomProperties.getModalWindowTimeout() - past;
            try {
                log.info("Ожидаем возможные модальные окна {} мс...", left);
                Thread.sleep(left);
                log.info("Дождались");
                WebElement element;
                while ((element = webDriver.findElement(
                        By.cssSelector("div[id$='-popup-modal'] [id$='-popup-close']"))).isDisplayed()) {
                    element.click();
                    Thread.sleep(1000L);
                }
            } catch (NoSuchElementException noSuchElementException) {
                // nothing to do.
            } catch (Exception e) {
                log.error("Проблема определения модальыых окон", e);
            }
        }


    private void openCitiesPopup() {
        // TODO: wait for city button

        Wait<WebDriver> wait = new FluentWait<>(webDriver)
                .withMessage("City popup not found")
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(200));

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".CitySelector__Button")));
        }
        catch (Exception e) {
            log.error("Не удалось загрузить список категорий", e);
            return;
        }
        webDriver.findElement(By.cssSelector(".CitySelector__Button")).click();
        webDriver.findElement(By.cssSelector(".CitiesModal__More-Btn")).click();
    }

}
