package ua.tpetrenko.esp.core.repository;

import org.springframework.stereotype.Repository;
import ua.tpetrenko.esp.core.model.catalog.CatalogSection;

@Repository
public interface SectionRepository extends AbstractEndpointRepository<CatalogSection> {

}
