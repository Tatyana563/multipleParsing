package ua.tpetrenko.esp.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.tpetrenko.esp.core.model.ProductItem;

@Repository
public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {

}
