package ua.tpetrenko.esp.impl.technodom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import ua.tpetrenko.esp.api.dto.CityDto;
import ua.tpetrenko.esp.api.dto.MenuItemDto;
import ua.tpetrenko.esp.api.dto.ProductItemDto;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Roman Zdoronok
 */
@Slf4j
@RequiredArgsConstructor
public class SingleCategoryProcessor implements Runnable {

    private static final String PAGE_URL_FORMAT = "?sort=views&page=%d";
    private static final Integer NUMBER_OF_PRODUCTS_PER_PAGE = 18;
    private static final Pattern PATTERN = Pattern.compile("Артикул:\\s*(\\S*)");
    private static final Pattern PRICE_PATTERN = Pattern.compile("(^([0-9]+\\s*)*)");
    private static final Pattern QUANTITY_PATTERN = Pattern.compile("(\\d+)");

    private final CityDto cityDto;
    private final MenuItemDto menuItemDto;
    private final ProductItemHandler productItemHandler;
    private final WebDriver webDriver;

    //TODO: copy 'technodomkz' implementation
    @Override
    public void run() {
        try {
            log.warn("Начиначем обработку категории '{}'", menuItemDto.getName());
            String categoryUrl;
            String pageUrlFormat;
            synchronized (webDriver) {

                String categorySuffix = URLUtil.getCategorySuffix(menuItemDto.getUrl(), Constants.URL);
                //https://www.technodom.kz/noutbuki-i-komp-jutery/noutbuki-i-aksessuary/noutbuki
                //https://www.technodom.kz/aktobe/noutbuki-i-komp-jutery/noutbuki-i-aksessuary/noutbuki
               if (cityDto.getUrl() == null) {
                    categoryUrl = String.format("%s/%s", Constants.URL, categorySuffix);
                } else {
                    categoryUrl = String.format("%s/%s/%s", Constants.URL, cityDto.getUrl(), categorySuffix);
                }
                pageUrlFormat = categoryUrl + PAGE_URL_FORMAT;
                String firstPageUrl = String.format(pageUrlFormat, 1);
               //https://www.technodom.kz/abai/smartfony-i-gadzhety/aksessuary-dlja-telefonov/podstavki?sort=views&page=1
                webDriver.get(firstPageUrl);
            }
            Document itemsPage = Jsoup.parse(webDriver.getPageSource());
            if (itemsPage != null) {
                int totalPages = getTotalPages(itemsPage);
                parseItems(itemsPage);
                for (int pageNumber = 2; pageNumber <= totalPages; pageNumber++) {
                    log.info("Получаем список товаров ({}) - страница {}", menuItemDto.getName(), pageNumber);
                    String pageUrl = String.format(pageUrlFormat, pageNumber);
                    webDriver.get(pageUrl);
                    Document itemsPages = Jsoup.parse(webDriver.getPageSource());
                    parseItems(itemsPages);
                }

            }

        } finally {
            log.warn("Обработка категории '{}' завершена", menuItemDto.getName());
        }
    }
//CategoryPagination-Arrow CategoryPagination-Arrow_direction_last
    private int getTotalPages(Document firstPage) {
        Element pageElement = firstPage.selectFirst(".CategoryPage-ItemsCount");
        if (pageElement != null) {
            int numberOfPages = 0;
            Integer amountOfProducts;
            String quantity = pageElement.text();
            //Всего 24 продуктов
            Matcher matcher = QUANTITY_PATTERN.matcher(quantity);
            if (matcher.find()) {
                amountOfProducts = Integer.valueOf(matcher.group(1));
                int main = amountOfProducts / NUMBER_OF_PRODUCTS_PER_PAGE;
                if (main != 0) {
                    if ((amountOfProducts % NUMBER_OF_PRODUCTS_PER_PAGE != 0)) {
                        numberOfPages = main + 1;
                    } else {
                        numberOfPages = main;
                    }
                } else {
                    numberOfPages = 1;
                }
            }
            return numberOfPages;
        } else return 0;
    }


    private void parseItems(Document itemsPage) {
        if (!isValidCity(itemsPage)) {
            String text = itemsPage.selectFirst("p.CitySelector__Title").text();
            log.error("Используется другой город {}", text);
            return;
        }

        Elements itemElements = itemsPage.select(".CategoryProductList li.ProductCard");

        for (Element itemElement : itemElements) {
            try {
                processProductItem(itemElement).ifPresent(productItemHandler::handle);
            } catch (Exception e) {
                log.error("Не удалось распарсить продукт", e);
            }

        }
    }

    private boolean isValidCity(Document page) {
        return cityDto.getName().equalsIgnoreCase(page.selectFirst("p.CitySelector__Title").text());
    }

    private Optional<ProductItemDto> processProductItem(Element itemElement) {
      //  String itemPhoto = itemElement.selectFirst(".ProductCard-Image img").absUrl("src");
        String itemUrl = itemElement.selectFirst(".ProductCard-Content").attr("href");
        String itemText = itemElement.selectFirst("h4").text();
        String itemPriceValue = itemElement.selectFirst(".ProductPrice data[value]").attr("value");

        log.info("Продукт: {} {}", itemText, itemPriceValue);
        String externalCode = URLUtil.extractExternalIdFromUrl(itemUrl);
        if (externalCode != null && externalCode.isEmpty()) {
            log.warn("Продукт без кода: {}\n{}", itemText, itemUrl);
          return Optional.empty();
        }

        String itemUrlWithoutCity = URLUtil.removeCityFromUrl(itemUrl);

        return Optional.of(new ProductItemDto(itemText, itemUrlWithoutCity)
                                         .setExternalId(null)
                                         .setCode(externalCode)
                                         .setDescription(null)
                                         .setImageUrl(itemUrl)
                                         .setPrice(Double.valueOf(itemPriceValue)));
          }
}

