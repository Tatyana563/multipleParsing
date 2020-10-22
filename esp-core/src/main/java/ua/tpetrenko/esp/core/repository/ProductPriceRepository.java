package ua.tpetrenko.esp.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.tpetrenko.esp.core.model.ProductPrice;

/**
 * @author Roman Zdoronok
 */
@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
}
