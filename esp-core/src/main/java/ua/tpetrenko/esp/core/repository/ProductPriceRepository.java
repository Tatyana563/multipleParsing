package ua.tpetrenko.esp.core.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.tpetrenko.esp.core.model.MarketCity;
import ua.tpetrenko.esp.core.model.ProductItem;
import ua.tpetrenko.esp.core.model.ProductPrice;

/**
 * @author Roman Zdoronok
 */
@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
    Optional<ProductPrice> findOneByProductItemAndMarketCity(ProductItem item, MarketCity marketCity);
}
