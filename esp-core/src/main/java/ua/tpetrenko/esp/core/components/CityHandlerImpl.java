package ua.tpetrenko.esp.core.components;

import org.springframework.transaction.PlatformTransactionManager;
import ua.tpetrenko.esp.api.dto.CityDto;
import ua.tpetrenko.esp.api.handlers.CityHandler;
import ua.tpetrenko.esp.core.model.City;
import ua.tpetrenko.esp.core.model.Market;
import ua.tpetrenko.esp.core.model.MarketCity;
import ua.tpetrenko.esp.core.repository.CityRepository;
import ua.tpetrenko.esp.core.repository.MarketCityRepository;

/**
 * @author Roman Zdoronok
 */
public class CityHandlerImpl extends AbstractHandler<CityDto> implements CityHandler {

    private final Market market;
    private final CityRepository cityRepository;
    private final MarketCityRepository marketCityRepository;

    public CityHandlerImpl(Market market, CityRepository cityRepository, MarketCityRepository marketCityRepository, PlatformTransactionManager transactionManager) {
        super(transactionManager);
        this.market = market;
        this.cityRepository = cityRepository;
        this.marketCityRepository = marketCityRepository;
    }


    @Override
    public void doHandle(CityDto itemDto) {

        City city = cityRepository.findOneByNameIgnoreCase(itemDto.getName())
                                  .orElseGet(() -> {
                                      City newCity = new City();
                                      newCity.setName(itemDto.getName());
                                      return cityRepository.save(newCity);
                                  });

        if (!marketCityRepository.existsByMarketAndCity(market, city)) {
            marketCityRepository.save(new MarketCity(itemDto.getUrl(), market, city));
        }
    }
}
