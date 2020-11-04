package ua.tpetrenko.esp.core.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.tpetrenko.esp.core.model.City;

/**
 * @author Roman Zdoronok
 */
@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findOneByNameIgnoreCase(String name);
}
