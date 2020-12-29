package ua.tpetrenko.esp.impl.technodom;

import io.github.bonigarcia.wdm.WebDriverManager;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.tpetrenko.esp.api.dto.CityDto;
import ua.tpetrenko.esp.api.dto.MarketInfo;
import ua.tpetrenko.esp.api.dto.MenuItemDto;
import ua.tpetrenko.esp.api.handlers.CityHandler;
import ua.tpetrenko.esp.api.handlers.MenuItemHandler;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;
import ua.tpetrenko.esp.api.parser.DifferentItemsPerCityMarketParser;
import ua.tpetrenko.esp.impl.technodom.properties.TechnodomProperties;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Roman Zdoronok
 */
//@Slf4j
@Component
//@RequiredArgsConstructor
public class TechnodomParser implements DifferentItemsPerCityMarketParser {
    private static Logger log = LoggerFactory.getLogger(TechnodomParser.class);
    private static final MarketInfo INFO = new MarketInfo("Technodom", "https://technodom.kz/");

    private final TechnodomProperties technodomProperties;
    private Object key = new Object();

    public TechnodomParser(TechnodomProperties technodomProperties) {
        this.technodomProperties = technodomProperties;
    }

    private WebDriver webDriver;

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
        options.setBinary(technodomProperties.getChrome().getPath());
//        options.addArguments("--headless");
        options.addArguments("window-size=1920x1080");
        webDriver = new ChromeDriver(options);
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        log.info("Получаем главную страницу...");
        webDriver.get(INFO.getUrl());
        log.info("Готово.");
        closeModals();
    }

    private void closeModals() {
        Wait<WebDriver> wait = new FluentWait<>(webDriver)
                .withMessage("Preset popup not found")
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(200));

        long modalTimeout = technodomProperties.getModalWindowPresentTimeoutMs().toMillis();
        try {
            log.info("Ожидаем возможные модальные окна {} мс...", modalTimeout);
            Thread.sleep(modalTimeout);
            log.info("Дождались");
            WebElement element;
            //TODO: use wait api

        } catch (NoSuchElementException noSuchElementException) {
            // nothing to do.
        } catch (Exception e) {
            log.error("Проблема определения модальных окон", e);
        }


        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".ReactModal__Content.VerifyCityModal")));
        } catch (Exception e) {
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

    }

    @Override
    public void parseMainMenu(MenuItemHandler menuItemHandler) {
        webDriver.get(Constants.CATEGORIES_URL);
        Wait<WebDriver> wait = new FluentWait<>(webDriver)
                .withMessage("Categories not found")
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofMillis(200));

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".CatalogPage-CategorySection")));
        } catch (Exception e) {
            log.error("Не удалось загрузить список категорий", e);
            return;
        }
        Document document = Jsoup.parse(webDriver.getPageSource());
        log.info("Получили главную страницу, ищем секции...");
        Elements sectionElements = document.select(".CatalogPage-CategorySection");
        for (Element sectionElement : sectionElements) {
            Element sectionTitle = sectionElement.selectFirst("h3.CatalogPage-CategoryTitle");
            String sectionName = sectionTitle.text();
            if (technodomProperties.getCategoriesWhitelist().contains(sectionName)) {
                log.info("Секция: {}", sectionName);
                MenuItemDto sectionItem = new MenuItemDto(sectionName, null);
                MenuItemHandler groupHandler = menuItemHandler.handleSubMenu(sectionItem);

                Elements groupElements = sectionElement.select(".CatalogPage-Category");
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
    }

    @Override
    public void parseCities(CityHandler cityHandler) throws Exception {
        synchronized (key) {
            openCitiesPopup();

            Document pageWithCitiesModal = Jsoup.parse(webDriver.getPageSource());
            Elements cityUrls = pageWithCitiesModal.select("a.CitiesModal__List-Item");
            for (Element cityUrl : cityUrls) {
                String cityName = cityUrl.text();
                log.info("Город: {}", cityName);
                String cityLink = URLUtil.extractCityFromUrl(cityUrl.attr("href"), Constants.ALL_SUFFIX);

                cityHandler.handle(new CityDto(cityName, cityLink));
            }

            closeCitiesPopup();
        }
    }

    @Override
    public void parseItems(CityDto cityDto, MenuItemDto menuItemDto, ProductItemHandler productItemHandler) {


        openCitiesPopup();
        List<WebElement> cityLinks = webDriver.findElements(By.cssSelector("a.CitiesModal__List-Item"));
        for (WebElement cityLink : cityLinks) {
            if (cityDto.getName().equalsIgnoreCase(cityLink.getText())) {
                cityLink.click();
                break;
            }
        }

            try {

                new SingleCategoryProcessor(cityDto, menuItemDto, productItemHandler, webDriver, technodomProperties).run();

            } catch (Exception e) {
                log.error("Не удалось распарсить продукт", e);
            }

    }



    @Override
    public void destroyParser() {
        if (webDriver != null) {
            webDriver.close();
        }
    }

    private void openCitiesPopup() {
        Wait<WebDriver> wait = new FluentWait<>(webDriver)
                .withMessage("City popup not found")
                .withTimeout(Duration.ofSeconds(120))
                .pollingEvery(Duration.ofMillis(200));

        try {
            log.info("Ожидаем доступности модального окна выбора городов");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".CitySelector__Button")));
        } catch (Exception e) {
            log.error("Не найдена кнопка отображения списка городов", e);
            return;
        }
        webDriver.findElement(By.cssSelector(".CitySelector__Button")).click();
        log.info("Открываем модальное окно выбора городов...");

        try {
            log.info("Ожидаем доступности конкретного города");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".CitiesModal__More-Btn")));
          //  wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".CitiesModal__List-Item_Bold")));

        } catch (Exception e) {
            log.error("Не найдена кнопка отображения города", e);
            return;
        }

      //  webDriver.findElement(By.cssSelector(".CitiesModal__More-Btn")).click();
//        webDriver.findElement(By.cssSelector(".LinkNext CitiesModal__More-Btn")).click();
//        log.info("Жмем \"Еще...\"");
    }

    private void closeCitiesPopup() {
        log.info("Закрываем модальное окно выбора городов...");
        webDriver.findElement(By.cssSelector(".ReactModal__Content.CitiesModal .ModalNext__CloseBtn")).click();
      //  webDriver.findElement(By.cssSelector(".ModalNext__CloseBtn ModalNext__CloseBtn-Cross")).click();
    }
}
