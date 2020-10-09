package ua.tpetrenko.esp.app.configuration;

import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.tpetrenko.esp.api.ShopParser;

/**
 * @author Roman Zdoronok
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class Context {
    private final Set<ShopParser> parsers;

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            for (ShopParser parser : parsers) {
                log.info("Parser for {}", parser.getShopInfo().getName());
                parser.parseData();
            }
        };
    }
}
