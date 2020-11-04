package ua.tpetrenko.esp.core.components;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class CityHandlerImpl implements CityHandler {

    private final Market market;
    private final CityRepository cityRepository;
    private final MarketCityRepository marketCityRepository;

    @Override
    public void handle(CityDto itemDto) {

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
