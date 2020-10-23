package ua.tpetrenko.esp.core.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.tpetrenko.esp.core.model.Market;

/**
 * @author Roman Zdoronok
 */
@Repository
public interface MarketRepository extends JpaRepository<Market, Long> {
    Optional<Market> findOneByName(String name);
}
