package ua.tpetrenko.esp.impl.fora;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ua.tpetrenko.esp.api.dto.CityDto;
import ua.tpetrenko.esp.api.dto.MenuItemDto;
import ua.tpetrenko.esp.api.dto.ProductItemDto;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;

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
    private final Map<String, String> cookies;


    @Override
    public void run() {
        try {
            log.warn("Начиначем обработку категории '{}'", menuItemDto.getName());
            String pageUrlFormat = menuItemDto.getUrl() + PAGE_URL_FORMAT;
            String firstPageUrl = String.format(pageUrlFormat, 1);
            Connection.Response result = null;
            synchronized (cookies) {
                result = Jsoup.connect(firstPageUrl)
                              .cookies(cookies)
                              .timeout((int)Duration.ofMinutes(1L).toMillis())
                              .execute();
                cookies.putAll(result.cookies());
            }

            Document firstPage = result.parse();
            if (firstPage != null) {
                int totalPages = getTotalPages(firstPage);
                parseItems(firstPage);
                for (int pageNumber = 2; pageNumber <= totalPages; pageNumber++) {
                    log.info("Получаем список товаров ({}) - страница {}", menuItemDto.getName(), pageNumber);
                    synchronized (cookies) {
                        result = Jsoup.connect(String.format(pageUrlFormat, pageNumber)).cookies(cookies).timeout((int)Duration.ofMinutes(1L).toMillis()).execute();
                        cookies.putAll(result.cookies());
                    }
                    parseItems(result.parse());

                }
            }

        } catch (IOException ioException) {
            log.error("Не получилось распарсить категорию", ioException);
        } finally {
            log.warn("Обработка категории '{}' завершена", menuItemDto.getName());
        }
    }

    private int getTotalPages(Document firstPage) {
        Element itemElement = firstPage.selectFirst(".catalog-container");
        if (itemElement != null) {
            int numberOfPages = 0;

            String quantity = itemElement.select(".product-quantity").text();
            Integer amountOfProducts;
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
            log.error("Используется другой город {}", itemsPage.selectFirst("a.current-city").text());
            return;
        }

        Elements itemElements = itemsPage.select(".catalog-list-item:not(.injectable-banner)");

        for (Element itemElement : itemElements) {
            try {
                processProductItem(itemElement).ifPresent(productItemHandler::handle);
            } catch (Exception e) {
                log.error("Не удалось распарсить продукт", e);
            }

        }
    }

    private boolean isValidCity(Document page) {
        return cityDto.getName().equalsIgnoreCase(page.selectFirst("a.current-city").text());
    }

    private Optional<ProductItemDto> processProductItem(Element itemElement) {
        String itemImageUrl = itemElement.selectFirst(".image img").absUrl("src");
        Element itemLink = itemElement.selectFirst(".item-info>a");
        String itemUrl = itemLink.absUrl("href");
        String itemText = itemLink.text();

        String externalId = URLUtil.extractExternalIdFromUrl(itemUrl);
        if (externalId != null && externalId.isEmpty()) {
            log.warn("Продукт без кода: {}\n{}", itemText, itemUrl);
            return Optional.empty();
        }

        String itemDescription = itemElement.selectFirst(".list-unstyled").text();
        Matcher matcher = PATTERN.matcher(itemDescription);
        String itemCode = matcher.find() ? matcher.group(1) : null;

        String itemUrlWithoutCity = URLUtil.removeCityFromUrl(itemUrl);

        String itemPriceString = itemElement.selectFirst(".price").text();
        Matcher priceMatcher = PRICE_PATTERN.matcher(itemPriceString);
        Double price = priceMatcher.find()
                       ? Double.parseDouble(priceMatcher.group(0).replaceAll("\\s*", ""))
                       : null;

        return Optional.of(new ProductItemDto(itemText, itemUrlWithoutCity)
                                         .setExternalId(externalId)
                                         .setCode(itemCode)
                                         .setDescription(itemDescription)
                                         .setImageUrl(itemImageUrl)
                                         .setPrice(price));
    }
}
