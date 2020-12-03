package ua.tpetrenko.esp.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.tpetrenko.esp.core.model.MenuItem;
import ua.tpetrenko.esp.core.model.ProductItem;
import ua.tpetrenko.esp.core.model.ProductItemInfo;

import java.util.Optional;

@Repository
public interface ProductItemInfoRepository extends JpaRepository<ProductItemInfo, Long> {
    Optional<ProductItemInfo> findOneByProductItemId(Long id);
}