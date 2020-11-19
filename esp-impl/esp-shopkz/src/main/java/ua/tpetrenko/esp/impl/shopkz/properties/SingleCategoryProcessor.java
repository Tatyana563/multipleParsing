package ua.tpetrenko.esp.impl.shopkz.properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.tpetrenko.esp.api.dto.MenuItemDto;
import ua.tpetrenko.esp.api.dto.ProductItemDto;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;
import ua.tpetrenko.esp.impl.shopkz.ShopkzParser;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Roman Zdoronok
 */
@Slf4j
@RequiredArgsConstructor
public class SingleCategoryProcessor implements Runnable {
  //  private static Logger log = LoggerFactory.getLogger("SHOPKZLOGGER");
    //https://shop.kz/materinskie-platy/?PAGEN_1=4
    private static final String PAGE_URL_CONSTANT = "?PAGEN_1=%d";
    private static final Pattern PRODUCT_NUMBER_PATTERN = Pattern.compile("Артикул:\\s+(\\d+)\\s+(.*)");
    private static final Pattern IMAGE_PATTERN = Pattern.compile("(.*)(url)(\\()(\')(.*)(\')\\)");

    private final MenuItemDto menuItemDto;
    private final ProductItemHandler productItemHandler;
    private final CountDownLatch latch;


    @Override
    public void run() {
        try {
            log.warn("Начиначем обработку категории '{}'", menuItemDto.getName());
            String pageUrlFormat = menuItemDto.getUrl() + PAGE_URL_CONSTANT;
            String firstPageUrl = String.format(pageUrlFormat, 1);
            Document firstPage = Jsoup.connect(firstPageUrl).get();
            int totalPages = getTotalPages(firstPage);
            parseItems(firstPage);
            for (int i = 2; i <= totalPages; i++) {
                log.info("Получаем список товаров - страница {}", i);
                parseItems(Jsoup.connect(String.format(menuItemDto.getUrl() + PAGE_URL_CONSTANT, i))
                        .cookie("sectionSort", "new")
                        .get());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            latch.countDown();
        }
    }

    private int getTotalPages(Document firstPage) {
        Element lastPage = firstPage.selectFirst(".bx-pagination-container > ul > li:nth-last-of-type(2)>a");
        if (lastPage != null) {
            String text = lastPage.text();
            return Integer.parseInt(text);
        }
        return 0;
    }

    private void parseItems(Document itemsPage) {


        Elements itemElements = itemsPage.select(".bx_catalog_item");

        for (Element itemElement : itemElements) {
            try {
                processProductItem(itemElement).ifPresent(productItemHandler::handle);
            } catch (Exception e) {
                log.error("Не удалось распарсить продукт", e);
            }

        }
    }


    private Optional<ProductItemDto> processProductItem(Element itemElement) throws JsonProcessingException {
        Element itemContainer = itemElement.selectFirst(".bx_catalog_item_container");

        String itemData = itemContainer.attr("data-product");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(itemData);
        double itemPrice = jsonNode.get("price").asDouble();
        String itemText = jsonNode.get("name").asText();
        Integer itemCode = jsonNode.get("id").asInt();
        Element descriptionContainer = itemElement.selectFirst(".bx_catalog_item_articul");
        String itemDescription = null;
        String imageUrl = null;
        String itemLink = itemElement.selectFirst(".bx_catalog_item_title a").absUrl("href");
        String itemImage = itemElement.select("a.bx_catalog_item_images").attr("style");
        if (descriptionContainer != null) {
            String descriptionText = descriptionContainer.text();
            Matcher matcher = PRODUCT_NUMBER_PATTERN.matcher(descriptionText);
            if (matcher.find()) {
                itemDescription = matcher.group(2);
            }
            Pattern pattern = Pattern.compile("(.*\\:)");
            Matcher urlMatcher = pattern.matcher(ShopkzParser.INFO.getUrl());
            while (urlMatcher.find()) {
                String http = urlMatcher.group(1);
                Matcher imageMather = IMAGE_PATTERN.matcher(itemImage);
                while (imageMather.find()) {
                    String str = imageMather.group(5);
                    imageUrl = http + str;
                }
            }
        }

        return Optional.of(new ProductItemDto(itemText, itemLink)
                .setCode(itemCode.toString())
                .setDescription(itemDescription)
                .setImageUrl(imageUrl)
                .setPrice(itemPrice));
    }
}
