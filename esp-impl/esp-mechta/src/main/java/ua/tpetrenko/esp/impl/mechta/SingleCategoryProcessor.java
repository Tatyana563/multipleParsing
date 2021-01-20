package ua.tpetrenko.esp.impl.mechta;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import ua.tpetrenko.esp.api.dto.CityDto;
import ua.tpetrenko.esp.api.dto.MenuItemDto;
import ua.tpetrenko.esp.api.dto.ProductItemDto;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;
import ua.tpetrenko.esp.impl.mechta.dto.CatalogDto;
import ua.tpetrenko.esp.impl.mechta.dto.ItemDescriptionDto;
import ua.tpetrenko.esp.impl.mechta.dto.ItemDto;

import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;
import static ua.tpetrenko.esp.impl.mechta.MechtaParser.INFO;

/**
 * @author Roman Zdoronok
 */
@Slf4j
@RequiredArgsConstructor
public class SingleCategoryProcessor implements Runnable {

    //TODO: cleanup.
    private static final Pattern PRICE_PATTERN = Pattern.compile("(\\d*\\s*\\d*\\s*\\d*\\s*\\d*\\s*\\d*\\s*\\d*)");
    private static final Pattern IMAGE_PATTERN = Pattern.compile("(background-image: url\\()(.*)(\\))");
    private static final Pattern CODE_PATTERN = Pattern.compile("(\\d+)");
    private static final Pattern CATEGORY_PATTERN = Pattern.compile("\\/section\\/(.*)\\/");
    private static final int DEFAULT_PAGE_SIZE = 18;
    private static final String CATEGORY_URL_FORMAT = "https://www.mechta.kz/api/main/catalog_new/index.php?section=%s%s";
    private static final String PAGE_URL_FORMAT = "&page_num=%d&catalog=true&page_element_count=" + DEFAULT_PAGE_SIZE;
    private final CityDto cityDto;
    private final MenuItemDto menuItemDto;
    private final ProductItemHandler productItemHandler;
    // private final Map<String, String> cookies;

    private final WebDriver webDriver;

    private final RestTemplate restTemplate;


    @Override
    public void run() {
        try {
            log.warn("Начиначем обработку категории '{}'", menuItemDto.getName());
            Matcher categoryMatcher = CATEGORY_PATTERN.matcher(menuItemDto.getUrl());
            if (!categoryMatcher.find()) {
                log.error("Не удается получить путь к категории товаров: {}", menuItemDto.getUrl());
                return;
            }


            openCitiesPopup();
            String cityButton = ".aa_htcity_cities .aa_htc_item";
            List<WebElement> cityButtons = webDriver.findElements(By.cssSelector(cityButton));
            for (WebElement button : cityButtons) {
                if (button.getText().equals(cityDto.getName())) {
                    button.click();
                    break;
                }
            }
            String pageUrlFormat = String.format(CATEGORY_URL_FORMAT, categoryMatcher.group(1), PAGE_URL_FORMAT);
            String firstPageUrl = String.format(pageUrlFormat, 1);

            Set<Cookie> cookies = webDriver.manage().getCookies();
            Map<String, String> cookiesMap = webDriver.manage().getCookies().stream().collect(Collectors.toMap(Cookie::getName, Cookie::getValue));
            String cookieHeader = cookiesMap.entrySet()
                    .stream()
                    .map(e -> e.getKey()+"="+e.getValue())
                    .collect(joining(";"));
            //TODO: convert webDriver's cookies to spring's http headers
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cookie", cookieHeader);
//            cookies.stream().collect(Collectors.joining(" "));
//            headers.add("Cookie",cookies);
            //advice use collectors / joiners
//            Cookie: __ddg1=iXDysvA8IX9CeiFXZkNH; MECHTA_SM_SET_SITE_IDZ=rd; MECHTA_SM_GUEST_ID=28921167; MECHTA_SM_LAST_VISIT=19.01.2021+21%3A29%3A41; _fbp=fb.1.1601551526293.1941335454; __ddg2=BZf3h6TFCFbU28q0
//            headers.add("Cookie", cookies.iterator().next().getName() );

            //TODO: debug exchange method (find category which differs in items count per city)
            CatalogDto firstPage = restTemplate.exchange(firstPageUrl,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    CatalogDto.class)
                    .getBody();
            if (cityDto.getName().contains("Нур-Султан") && menuItemDto.getName().equals("Для утюгов")){
                System.out.println("Doesn't coincide");
            }
            if (firstPage != null) {
                int totalPages = getTotalPages(firstPage);
                parseItems(firstPage);
                for (int pageNumber = 2; pageNumber <= totalPages; pageNumber++) {
                    log.info("Получаем список товаров ({}) - страница {}", menuItemDto.getName(), pageNumber);
                    CatalogDto result = restTemplate.exchange(String.format(pageUrlFormat, pageNumber),
                            HttpMethod.GET,
                            new HttpEntity<>(headers),
                            CatalogDto.class)
                            .getBody();
                    if (result!=null) {
                        parseItems(result);
                    }
                }

            }

        } catch (InterruptedException ioException) {
            log.error("Не получилось распарсить категорию", ioException);
        } finally {
            log.warn("Обработка категории '{}' завершена", menuItemDto.getName());
        }
    }


    private int getTotalPages(CatalogDto catalogDto) {
        Integer allItems = catalogDto.getData().getSize();
        if(allItems!=null){
            return (int) Math.ceil((float) allItems / DEFAULT_PAGE_SIZE);
        }
       else return 0;
    }

    private void parseItems(CatalogDto itemsPage) throws InterruptedException {
        List<String> fullDescription = new ArrayList<>();
        if (itemsPage.getData().getItems()!=null) {
            for (ItemDto item : itemsPage.getData().getItems()) {
                ProductItemDto productItemDto = new ProductItemDto(item.getName(), "null");
                List<ItemDto> items = itemsPage.getData().getItems();

                for (ItemDto itemDto : items) {
                        List<ItemDescriptionDto> description = itemDto.getDescription();
                        if (description != null) {
                            for (ItemDescriptionDto itemDescriptionDto : description) {
                                fullDescription.add(itemDescriptionDto.getValue());
                                fullDescription.add(itemDescriptionDto.getName());
                            }
                            String desc = fullDescription.stream().collect(joining(" "));
                            productItemDto.setDescription(desc);
                        }
                        productItemHandler.handle(map(item, productItemDto));
                    }
                }
            }
        }


    private ProductItemDto map(ItemDto item, ProductItemDto productItemDto) {
        productItemDto.setCode((item.getCode()));
        productItemDto.setPrice(Double.valueOf(item.getPrice().getPrice()));
        productItemDto.setImageUrl(item.getPhotos().get(0));
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

    private void openCitiesPopup() {
        Wait<WebDriver> wait = new FluentWait<>(webDriver)
                .withMessage("City popup not found")
                .withTimeout(Duration.ofSeconds(120))
                .pollingEvery(Duration.ofMillis(200));

        try {
            log.info("Ожидаем доступности модального окна выбора городов");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".aa_showcities.jq_ab_review_city.jq_aa_showcities")));
        } catch (Exception e) {
            log.error("Не найдена кнопка отображения списка городов", e);
            return;
        }
        webDriver.findElement(By.cssSelector(".aa_showcities.jq_ab_review_city.jq_aa_showcities")).click();
        log.info("Открываем модальное окно выбора городов...");

        try {
            log.info("Ожидаем доступности конкретного города");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".aa_htcity_cities .aa_htc_item")));

        } catch (Exception e) {
            log.error("Не найдена кнопка отображения города", e);
            return;
        }
    }
}
