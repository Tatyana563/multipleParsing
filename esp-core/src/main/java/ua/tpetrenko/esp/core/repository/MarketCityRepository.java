package ua.tpetrenko.esp.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.tpetrenko.esp.core.model.City;
import ua.tpetrenko.esp.core.model.Market;
import ua.tpetrenko.esp.core.model.MarketCity;

/**
 * @author Roman Zdoronok
 */
@Repository
public interface MarketCityRepository extends JpaRepository<MarketCity, Long> {
    boolean existsByMarketAndCity(Market market, City city);
    Page<MarketCity> findAllByMarket(Market market, Pageable pageable);
}
