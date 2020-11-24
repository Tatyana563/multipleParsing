package ua.tpetrenko.esp.impl.shopkz;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import ua.tpetrenko.esp.api.dto.CityDto;

@Slf4j
@RequiredArgsConstructor
public class PerCityCategoriesParser {
    private final CityDto cityDto;

    private WebDriver webDriver;

    public void prepareParser() {
        log.info("Подготовка парсера категорий для города {}", cityDto.getName());
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
//        options.setBinary(path);
        options.addArguments("--headless");
        options.addArguments("window-size=1920x1080");
        webDriver = new ChromeDriver(options);
    }

    public void parseData() {

    }

    public void destroyParser() {
        if (webDriver != null) {
            webDriver.close();
        }
    }

}
