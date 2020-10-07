package ua.tpetrenko.esp.core.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.tpetrenko.esp.core.model.catalog.ItemCategory;


@Repository
public interface ItemCategoryRepository extends AbstractEndpointRepository<ItemCategory> {
    @Query("from ItemCategory category ORDER BY category.id ASC")
    Page<ItemCategory> getPage(PageRequest pageable);
}
