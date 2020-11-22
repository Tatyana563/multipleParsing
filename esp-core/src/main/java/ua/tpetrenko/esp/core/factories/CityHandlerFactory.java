package ua.tpetrenko.esp.core.factories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import ua.tpetrenko.esp.api.handlers.CityHandler;
import ua.tpetrenko.esp.core.components.CityHandlerImpl;
import ua.tpetrenko.esp.core.model.Market;
import ua.tpetrenko.esp.core.repository.CityRepository;
import ua.tpetrenko.esp.core.repository.MarketCityRepository;

/**
 * @author Roman Zdoronok
 */
@Component
@RequiredArgsConstructor
public class CityHandlerFactory {
    private final CityRepository cityRepository;
    private final MarketCityRepository marketCityRepository;
    private final PlatformTransactionManager transactionManager;

    public CityHandler getCityHandler(Market market) {
        return new CityHandlerImpl(market, cityRepository, marketCityRepository, transactionManager);
    }

}
