package ua.tpetrenko.esp.impl.mechta;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebDriver;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import ua.tpetrenko.esp.api.dto.CityDto;
import ua.tpetrenko.esp.api.dto.MenuItemDto;
import ua.tpetrenko.esp.api.dto.ProductItemDto;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;
import ua.tpetrenko.esp.impl.mechta.dto.CatalogDto;
import ua.tpetrenko.esp.impl.mechta.dto.ItemDto;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ua.tpetrenko.esp.impl.mechta.MechtaParser.INFO;

/**
 * @author Roman Zdoronok
 */
@Slf4j
@RequiredArgsConstructor
public class SingleCategoryProcessor implements Runnable {

    private static final String PAGE_URL_CONSTANT = "?PAGEN_2=%d&sort=popular&adesc=asc";
    private static final Pattern PRICE_PATTERN = Pattern.compile("(\\d*\\s*\\d*\\s*\\d*\\s*\\d*\\s*\\d*\\s*\\d*)");
    private static final Pattern IMAGE_PATTERN = Pattern.compile("(background-image: url\\()(.*)(\\))");
    private static final Pattern CODE_PATTERN = Pattern.compile("(\\d+)");
    private static final Pattern CATEGORY_PATTERN = Pattern.compile("(https://www.mechta.kz/section/)(.*)([/\\\\\\\\])");
    private final CityDto cityDto;
    private final MenuItemDto menuItemDto;
    private final ProductItemHandler productItemHandler;
    private final Map<String, String> cookies;

    private WebDriver webDriver;

    private final RestTemplate restTemplate;


    @Override
    public void run() {
        try {
            log.warn("Начиначем обработку категории '{}'", menuItemDto.getName());
            String pageUrlFormat = menuItemDto.getUrl() + PAGE_URL_CONSTANT;
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

        } catch (IOException | InterruptedException ioException) {
            log.error("Не получилось распарсить категорию", ioException);
        } finally {
            log.warn("Обработка категории '{}' завершена", menuItemDto.getName());
        }
    }


    private int getTotalPages(Document firstPage) {
        Element lastPage = firstPage.selectFirst(".modern-page-navigation>a:nth-last-of-type(2)");
        if (lastPage != null) {
            String text = lastPage.text();
            return Integer.parseInt(text);
        }
        return 0;
    }

    private void parseItems(Document itemsPage) throws InterruptedException {
        if (!isValidCity(itemsPage)) {
            log.error("Используется другой город {}", itemsPage.selectFirst("a.current-city").text());
            return;
        }

        String currentUrl = itemsPage.location();
        Matcher categoryMatcher = CATEGORY_PATTERN.matcher(currentUrl);
        if (categoryMatcher.find()) {

            String input = String.format("https://www.mechta.kz/api/main/catalog_new/index.php?section=%s&catalog=true&page_element_count", categoryMatcher.group(2));
//            HttpHeaders headers = new HttpHeaders();
//            HttpEntity<String> entity = new HttpEntity<>(headers);
//            CatalogDto catalogDto = restTemplate.exchange(input, HttpMethod.GET, entity, CatalogDto.class).getBody();
             CatalogDto catalogDto = restTemplate.getForObject(input, CatalogDto.class);
            for (ItemDto item : catalogDto.getItems()) {
                productItemHandler.handle(map(item));
            }
        }
    }
    private ProductItemDto map(ItemDto item) {
        ProductItemDto productItemDto = new ProductItemDto(item.getName(), null);
        productItemDto.setCode((item.getCode()));
        productItemDto.setPrice(Double.valueOf(item.getPrice().getPrice()));
        productItemDto.setImageUrl(item.getPhoto().getPhotoUrl().get(0));
        productItemDto.setDescription(null);
        return productItemDto;
    }

    private boolean isValidCity(Document page) {
        return true;
//        return cityDto.getName().equalsIgnoreCase(page.selectFirst("show-map-link").text());
    }

    private Optional<ProductItemDto> processProductItem(Element itemElement) {
        String code = null;
        Double price = null;
        String imageUrl = null;
        String itemUrl = itemElement.selectFirst(".ifont14.j_product_link").absUrl("href");
        String itemText = itemElement.selectFirst(".aa_std_name").text();
        String itemCode = itemElement.selectFirst(".element-table-article.only-desktop").text();
        String itemPrice = itemElement.selectFirst(".aa_std_bigprice.icblack").text();

        Matcher matcher = CODE_PATTERN.matcher(itemCode);
        if (matcher.find()) {
            code = matcher.group(0);
        }
        Matcher priceMatcher = PRICE_PATTERN.matcher(itemPrice);
        if (priceMatcher.find()) {
            price = Double.valueOf(priceMatcher.group(0).replaceAll("\\s*", ""));
        }
        String test = itemElement.selectFirst(".aa_st_imglink.j_product_link").attr("style");

        Matcher imageMatcher = IMAGE_PATTERN.matcher(test);

        if (imageMatcher.find()) {
            String image = imageMatcher.group(2).replace("'", "");
            String root = image.startsWith("/")
                    ? INFO.getUrl().substring(0, INFO.getUrl().length() - 1)
                    : menuItemDto.getUrl();
            imageUrl = root + image;
        }
        return Optional.of(new ProductItemDto(itemText, itemUrl)
                .setCode(code)
                .setImageUrl(imageUrl)
                .setPrice(price));

    }

}
