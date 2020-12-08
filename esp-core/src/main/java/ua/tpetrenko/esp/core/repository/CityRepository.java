package ua.tpetrenko.esp.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.tpetrenko.esp.core.model.City;

import java.util.Optional;

/**
 * @author Roman Zdoronok
 */
@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findOneByNameIgnoreCase(String name);
//
//    @Query(value = "select url from MarketCity  " +
//            "where City.name = :cityName")
//    String findUrlByCityName(@Param("cityName") String cityName);
}
