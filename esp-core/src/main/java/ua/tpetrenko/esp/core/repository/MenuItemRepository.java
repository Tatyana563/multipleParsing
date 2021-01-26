package ua.tpetrenko.esp.core.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.tpetrenko.esp.core.model.Market;
import ua.tpetrenko.esp.core.model.MenuItem;

import java.util.Optional;


@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    boolean existsByNameAndParentItemAndMarket(String name, MenuItem parentItem, Market market);
    Optional<MenuItem> findOneByNameAndParentItemAndMarket(String name, MenuItem parentItem, Market market);

    @Query("select mi from MenuItem mi "
        + "left join MenuItem child on child.parentItem = mi.id "
        + "where mi.market = :market and mi.name='Стационарные телефоны' group by mi having count (child) = 0")
    Page<MenuItem> findAllEndpointMenuItems(Market market, Pageable pageable);

//    menuItemRepository.findAllEndpointMenuItems(market, PageRequest.of(0, 100, Direction.ASC, "id"))
}
