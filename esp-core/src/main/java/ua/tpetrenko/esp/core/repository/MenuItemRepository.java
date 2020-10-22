package ua.tpetrenko.esp.core.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.tpetrenko.esp.core.model.MenuItem;

import java.util.Optional;


@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    boolean existsByNameAndParentItem(String name, MenuItem parentItem);
    Optional<MenuItem> findOneByNameAndParentItem(String name, MenuItem parentItem);
}
