package ua.tpetrenko.esp.core.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ua.tpetrenko.esp.core.model.AbstractEndpoint;

/**
 * @author Roman Zdoronok
 */
@NoRepositoryBean
public interface AbstractEndpointRepository<I extends AbstractEndpoint> extends JpaRepository<I, Long> {

    boolean existsByName(String name);
    Optional<I> findOneByName(String name);

    boolean existsByUrl(String url);
    Optional<I> findOneByUrl(String url);

}
