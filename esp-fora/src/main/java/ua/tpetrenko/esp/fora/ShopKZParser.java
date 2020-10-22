package ua.tpetrenko.esp.fora;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.tpetrenko.esp.api.ShopParser;
import ua.tpetrenko.esp.api.dto.MarketInfo;

@Component
@Slf4j
public class ShopKZParser implements ShopParser {

    @Override
    public MarketInfo getMarketInfo() {
        return new MarketInfo("ShopKZ", "https://shop.kz/");
    }

    @Override
    public void parseData() {
        log.info("Test main method");
    }
}
