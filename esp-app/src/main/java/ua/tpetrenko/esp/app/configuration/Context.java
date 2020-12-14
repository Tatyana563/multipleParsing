package ua.tpetrenko.esp.app.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.tpetrenko.esp.api.dto.MarketInfo;
import ua.tpetrenko.esp.api.parser.MarketParser;
import ua.tpetrenko.esp.core.factories.ParserContextFactory;
import ua.tpetrenko.esp.core.factories.TaskFactory;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Roman Zdoronok
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class Context {

    private final Set<MarketParser> parsers;
    private final ParserContextFactory parserContextFactory;

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {

            for (MarketParser parser : parsers) {
                MarketInfo marketInfo = parser.getMarketInfo();
                log.info("Parser for {} [{}]", marketInfo.getName(), parser.isEnabled() ? "enabled" : "disabled");
            }

            ExecutorService executorService = Executors.newFixedThreadPool(3);
            for (MarketParser parser : parsers) {
                MarketInfo marketInfo = parser.getMarketInfo();

                if (parser.isEnabled()) {
                    log.info("Submitting parser execution task for {}", marketInfo.getName());
                    executorService.submit(() -> parser.doParse(parserContextFactory.getParserContext(marketInfo)));
                }
            }
        };
    }
}
