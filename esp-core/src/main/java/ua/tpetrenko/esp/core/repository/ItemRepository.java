package ua.tpetrenko.esp.core.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.tpetrenko.esp.core.model.Item;
import ua.tpetrenko.esp.core.model.catalog.ItemCategory;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    boolean existsByCode(String code);
    Optional<Item> findOneByCode(String code);

    @Modifying
    @Transactional
    @Query("update Item set available = false where itemCategory = :itemCategory")
    void resetItemAvailability(ItemCategory itemCategory);
}
