package ua.tpetrenko.esp.core.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.tpetrenko.esp.core.model.Market;
import ua.tpetrenko.esp.core.model.MenuItem;

import java.util.Optional;


@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    boolean existsByNameAndParentItemAndMarket(String name, MenuItem parentItem, Market market);
    Optional<MenuItem> findOneByNameAndParentItemAndMarket(String name, MenuItem parentItem, Market market);
}
