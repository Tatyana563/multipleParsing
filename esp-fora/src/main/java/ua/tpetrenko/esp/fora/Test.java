package ua.tpetrenko.esp.fora;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.tpetrenko.esp.api.ShopParser;
import ua.tpetrenko.esp.api.dto.ShopInfo;

@Component
@Slf4j
public class Test implements ShopParser {

    @Override
    public ShopInfo getShopInfo() {
        return new ShopInfo("New Test Shop", "no url");
    }

    @Override
    public void parseData() {
        log.info("Test main method");
    }
}
