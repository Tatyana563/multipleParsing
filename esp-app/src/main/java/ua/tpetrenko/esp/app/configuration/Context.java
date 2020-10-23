package ua.tpetrenko.esp.app.configuration;

import java.util.Set;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.tpetrenko.esp.api.MarketParser;
import ua.tpetrenko.esp.api.dto.MarketInfo;
import ua.tpetrenko.esp.api.parser.ParserContext;
import ua.tpetrenko.esp.core.repository.MarketRepository;
import ua.tpetrenko.esp.core.tasks.SingleMarketParseTask;

/**
 * @author Roman Zdoronok
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public abstract class Context {
    private final Set<MarketParser> parsers;
    private final MarketRepository marketRepository;

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            ExecutorService executorService = Executors.newFixedThreadPool(3);

            for (MarketParser parser : parsers) {

                MarketInfo marketInfo = parser.getMarketInfo();
                log.info("Parser for {}", marketInfo.getName());

                executorService.submit(new SingleMarketParseTask(parser, getParserContext(marketRepository, marketInfo)));
            }
        };
    }

    @Lookup
    public abstract ParserContext getParserContext(MarketRepository marketRepository, MarketInfo marketInfo);
}
