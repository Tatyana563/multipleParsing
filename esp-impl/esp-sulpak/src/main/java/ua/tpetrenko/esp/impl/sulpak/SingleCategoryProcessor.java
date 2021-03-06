package ua.tpetrenko.esp.impl.sulpak;

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

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
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
    private static final String PAGE_URL_NUMBER_FORMAT = "?page=%d";
    private final CityDto cityDto;
    private final MenuItemDto menuItemDto;
    private final ProductItemHandler productItemHandler;
    private final Map<String, String> cookies;

    //https://www.sulpak.kz/f/monitoriy/karagandy?page=2
    @Override
    public void run() {
        try {
            log.warn("Начиначем обработку категории '{}'", menuItemDto.getName());
            String pageUrlFormat = menuItemDto.getUrl() + PAGE_URL_NUMBER_FORMAT;
            String firstPageUrl = String.format(pageUrlFormat, 1);
            Connection.Response result = null;
            synchronized (cookies) {
                result = Jsoup.connect(firstPageUrl)
                        .cookies(cookies)
                        .timeout((int) Duration.ofMinutes(1L).toMillis())
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
                        result = Jsoup.connect(String.format(pageUrlFormat, pageNumber)).cookies(cookies).timeout((int) Duration.ofMinutes(1L).toMillis()).execute();
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
        //  Elements lastPage = firstPage.select(".pagination.pages-list>a :last-of-type");
        Elements lastPage = firstPage.select(".pagination .pages-list a");
        if (!lastPage.isEmpty()) {
            return Integer.parseInt(lastPage.last().text());
        }
        return 0;
    }

    private void parseItems(Document itemsPage) {
        if (!isValidCity(itemsPage)) {
            log.error("Используется другой город {}", itemsPage.selectFirst("a.current-city").text());
            return;
        }

        Elements itemElements = itemsPage.select(".goods-container .tile-container");

        for (Element itemElement : itemElements) {
            try {
                processProductItem(itemElement).ifPresent(productItemHandler::handle);
            } catch (Exception e) {
                log.error("Не удалось распарсить продукт", e);
            }

        }
    }

    private boolean isValidCity(Document page) {
        return true;
//        return cityDto.getName().equalsIgnoreCase(page.selectFirst("show-map-link").text());
    }

    private Optional<ProductItemDto> processProductItem(Element itemElement) {
        Double itemPrice = Double.valueOf(itemElement.attr("data-price"));
        Element a = itemElement.selectFirst(".goods-tiles .product-container-right-side>a");
        String itemLink = a.absUrl("href");
        String itemText = a.text();
        log.info("Нашли товар {}/{}", itemText, itemPrice);

        String itemAvailability = itemElement.selectFirst("span.availability").text();
        Integer itemCode = Integer.valueOf(itemElement.attr("data-code"));
        String itemPhoto = itemElement.selectFirst(".goods-photo img").absUrl("src");

        ProductItemDto productItemDto = new ProductItemDto(itemText, itemLink);
        productItemDto.setCode(itemCode.toString());
        productItemDto.setPrice(itemPrice);
        productItemDto.setImageUrl(itemLink);
        productItemDto.setImageUrl(itemPhoto);

//        productItemDto.setAvailable(StringUtils.containsIgnoreCase(itemAvailability, "есть в наличии")
//                || StringUtils.containsIgnoreCase(itemAvailability, "товар на витрине"));

        return Optional.of(productItemDto);

    }
}
