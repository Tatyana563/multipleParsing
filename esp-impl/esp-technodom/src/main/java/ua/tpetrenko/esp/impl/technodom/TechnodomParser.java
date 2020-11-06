package ua.tpetrenko.esp.impl.technodom;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.tpetrenko.esp.api.parser.DifferentItemsPerCityMarketParser;
import ua.tpetrenko.esp.api.dto.MarketInfo;
import ua.tpetrenko.esp.api.handlers.CityHandler;
import ua.tpetrenko.esp.api.handlers.MenuItemHandler;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;
import ua.tpetrenko.esp.impl.technodom.properties.TechnodomProperties;

/**
 * @author Roman Zdoronok
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TechnodomParser implements DifferentItemsPerCityMarketParser {

    private static final MarketInfo INFO = new MarketInfo("Technodom", "https://technodom.kz/");
    private static final String CATEGORIES_PAGE = INFO.getUrl() + "all";

    private final TechnodomProperties technodomProperties;

    @Value("${parser.chrome.path}")
    private String path;

    @Override
    public MarketInfo getMarketInfo() {
        return INFO;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void prepareParser() {
       log.info("Подготовка " + getMarketInfo());
    }

    @Override
    public void parseMainMenu(MenuItemHandler menuItemHandler) {
        // Nothing to do.
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

    public void parseData() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.setBinary(path);
        options.addArguments("--headless");
        options.addArguments("window-size=1920x1080");

        WebDriver driver = new ChromeDriver(options);
        driver.get(CATEGORIES_PAGE);
        List<WebElement> sectionElements = driver.findElements(By.cssSelector("div.CatalogPage-CategorySection"));
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
}
